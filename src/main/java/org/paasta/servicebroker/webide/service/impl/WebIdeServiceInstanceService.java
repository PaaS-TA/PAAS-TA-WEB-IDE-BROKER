package org.paasta.servicebroker.webide.service.impl;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.cloudfoundry.client.v2.applications.ApplicationsV2;
import org.cloudfoundry.client.v2.securitygroups.SecurityGroups;
import org.cloudfoundry.client.v2.servicebindings.ServiceBindingsV2;
import org.openpaas.paasta.bosh.director.BoshDirector;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.openpaas.servicebroker.exception.ServiceInstanceExistsException;
import org.openpaas.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.openpaas.servicebroker.service.ServiceInstanceService;
import org.paasta.servicebroker.webide.common.Common;
import org.paasta.servicebroker.webide.exception.OndemandServiceException;
import org.paasta.servicebroker.webide.model.DeploymentInstance;
import org.paasta.servicebroker.webide.model.JpaOnDemandServiceInstance;
import org.paasta.servicebroker.webide.model.JpaServiceList;
import org.paasta.servicebroker.webide.repo.JpaOnDemandServiceInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 이 서비스 브로커에서 접근하는 WebIde 대한 서비스를 위한 클래스
 *
 * @author sjchoi
 * @version 1.0
 * @since 2018.08.21
 */
@Service
public class WebIdeServiceInstanceService implements ServiceInstanceService {

    private static final Logger logger = LoggerFactory.getLogger(WebIdeServiceInstanceService.class);
    
    @Value("${bosh.deployment_name}")
    public String deploymentName;

    @Value("${bosh.instance_name}")
    public String instanceName;

    @Value("${serviceDefinition.org_limitation}")
    public int org_limitation;

    @Value("${serviceDefinition.space_limitation}")
    public int space_limitation;

    private int unlimited = -1;
    
    ObjectMapper objectMapper = new ObjectMapper();

    ReentrantLock lock = new ReentrantLock();

    @Autowired
    private WebIdeAdminService webIdeAdminService;
    
    @Autowired
    OnDemandDeploymentService onDemandDeploymentService;
    
    @Autowired
    JpaOnDemandServiceInstanceRepository jpaServiceInstanceRepository;
    
    @Autowired
    CloudFoundryService cloudFoundryService;
    
    @Autowired
    Common common;


    @Override
    public ServiceInstance createServiceInstance(CreateServiceInstanceRequest request) throws ServiceInstanceExistsException, ServiceBrokerException {
        logger.debug("WebIdeServiceInstanceService CLASS createServiceInstance");
        
        String dashboardUrl = "";
        JpaOnDemandServiceInstance jpaServiceInstance = new JpaOnDemandServiceInstance(request);
        jpaServiceInstanceRepository.save(jpaServiceInstance);
        
        try {
        	List<JpaOnDemandServiceInstance> orgInstanceList = jpaServiceInstanceRepository.findAllByOrganizationGuid(request.getOrganizationGuid());
            if (orgInstanceList.size() > org_limitation && org_limitation != unlimited) {
                throw new OndemandServiceException("Currently, only " + org_limitation + " service instances can be created in this organization.");
            }
            List<JpaOnDemandServiceInstance> spaceInstanceList = jpaServiceInstanceRepository.findAllBySpaceGuid(request.getSpaceGuid());
            if (spaceInstanceList.size() > space_limitation && space_limitation != unlimited) {
                throw new OndemandServiceException("Currently, only " + space_limitation + " service instances can be created in this space.");
            }
            List<DeploymentInstance> deploymentInstances = onDemandDeploymentService.getVmInstance(deploymentName, instanceName);
            if (deploymentInstances == null) {
                throw new ServiceBrokerException(deploymentName + " is Working");
            }
            
            // BoshDirector.INSTANCE_STATE_START : started
            List<DeploymentInstance> startedDeploymentInstances = deploymentInstances.stream().filter((x) -> x.getState().equals(BoshDirector.INSTANCE_STATE_START) && x.getJobState().equals("running")).collect(Collectors.toList());
            
            for(DeploymentInstance dep : startedDeploymentInstances){
            	
                if(jpaServiceInstanceRepository.findByVmInstanceId(dep.getId()) == null){
                    jpaServiceInstance.setVmInstanceId(dep.getId());
                    jpaServiceInstance.setDashboardUrl(dep.getIps().substring(1,dep.getIps().length()-1));
                    jpaServiceInstanceRepository.save(jpaServiceInstance);
                    jpaServiceInstance.withAsync(true);
                    SecurityGroups securityGroups = common.cloudFoundryClient().securityGroups();
                    cloudFoundryService.SecurityGurop(request.getSpaceGuid(), jpaServiceInstance.getDashboardUrl(), securityGroups);
                    logger.info("서비스 인스턴스 생성");
                    return jpaServiceInstance;
                }
            }
            logger.info("LOCK CHECKING!!!");
            //여기 지나치면 무조건 생성또는 시작해야하기 때문에 deployment 작업 여부 조회해야함
            if (onDemandDeploymentService.getLock(deploymentName)) {
                throw new ServiceBrokerException(deploymentName + " is Working");
            }
            
            // BoshDirector.INSTANCE_STATE_DETACHED : detached
            List<DeploymentInstance> detachedDeploymentInstances = deploymentInstances.stream().filter(x -> x.getState().equals(BoshDirector.INSTANCE_STATE_DETACHED)).collect(Collectors.toList());
            String taskID = "";
            String staticIp = "";
            
            for (DeploymentInstance dep : detachedDeploymentInstances) {
            	// BoshDirector.INSTANCE_STATE_START : started
                onDemandDeploymentService.updateInstanceState(deploymentName, instanceName, dep.getId(), BoshDirector.INSTANCE_STATE_START);
                while (true) {
                    Thread.sleep(1000);
                    taskID = onDemandDeploymentService.getTaskID(deploymentName);
                    if (taskID != null) {
                        logger.info("taskID : " + taskID);
                        break;
                    }
                }
                String ips = "";
                
                while (true) {
                    Thread.sleep(1000);
                    ips = onDemandDeploymentService.getStartInstanceIPS(taskID, instanceName, dep.getId());
                    if (ips != null) {
                        break;
                    }
                }
                
                dashboardUrl = "http://" + ips + ":8080";
                jpaServiceInstance.setVmInstanceId(dep.getId());
                jpaServiceInstance.setDashboardUrl(dashboardUrl);
                jpaServiceInstanceRepository.save(jpaServiceInstance);
                jpaServiceInstance.withAsync(true);
                SecurityGroups securityGroups = common.cloudFoundryClient().securityGroups();
                cloudFoundryService.SecurityGurop(request.getSpaceGuid(), jpaServiceInstance.getDashboardUrl(), securityGroups);
                return jpaServiceInstance;
            }
            
            // 현재 사용 중인 정보 전체 조회
            List<JpaOnDemandServiceInstance> onDemandAllList = jpaServiceInstanceRepository.findAll();
            // 등록된 STATIC IP 목록
            List<JpaServiceList> serviceList = webIdeAdminService.findAllDashboardUrls();
            
            for(JpaServiceList serviceInfo : serviceList) {
            	boolean isUsed = false;
            	for(JpaOnDemandServiceInstance onDemandServiceInstance : onDemandAllList) {
            		if(serviceInfo.getWebIdeService().equals(onDemandServiceInstance.getDashboardUrl())) {
            			isUsed = true;
            			break;
            		}
            	}
            	
            	// 미사용중
            	if(!isUsed) {
            		dashboardUrl = serviceInfo.getWebIdeService();
            	}
            }
            
            // 	사용 가능한 URL 이 없을 경우 메세지 처리
            if("".equals(dashboardUrl)) {
            	throw new ServiceBrokerException("There is no STATIC IP available.");
            }
            

            staticIp = new URL(dashboardUrl).getHost();
            onDemandDeploymentService.createInstance(deploymentName, instanceName, staticIp);
            while (true) {
                Thread.sleep(1000);
                taskID = onDemandDeploymentService.getTaskID(deploymentName);
                if (taskID != null) {
                    logger.info("Create Instance taskID : " + taskID);
                    break;
                }
            }
            String ips = "";
            while (true) {
                Thread.sleep(1000);
                ips = onDemandDeploymentService.getUpdateInstanceIPS(taskID);
                if (ips != null) {
                    break;
                }
            }
            String instanceId = "";
            while (true) {
                Thread.sleep(1000);
                instanceId = onDemandDeploymentService.getUpdateVMInstanceID(taskID, instanceName);
                if (instanceId != null) {
                    break;
                }
            }
            
            jpaServiceInstance.setDashboardUrl(dashboardUrl);
            jpaServiceInstance.setVmInstanceId(instanceId);
            jpaServiceInstanceRepository.save(jpaServiceInstance);
            jpaServiceInstance.withAsync(true);
            SecurityGroups securityGroups = common.cloudFoundryClient().securityGroups();
            cloudFoundryService.SecurityGurop(request.getSpaceGuid(), jpaServiceInstance.getDashboardUrl(), securityGroups);
            return jpaServiceInstance;
        } catch (Exception e) {
            throw new ServiceBrokerException(e.getMessage());

        }
    }


    @Override
    public ServiceInstance getServiceInstance(String instanceId) {
    	return jpaServiceInstanceRepository.findByServiceInstanceId(instanceId);
    }

    @Override
    public ServiceInstance deleteServiceInstance(DeleteServiceInstanceRequest request)
            throws ServiceBrokerException {
    	
    	JpaOnDemandServiceInstance instance = jpaServiceInstanceRepository.findByServiceInstanceId(request.getServiceInstanceId());
        jpaServiceInstanceRepository.delete(instance);
        if (instance.getVmInstanceId() != null && !jpaServiceInstanceRepository.existsAllByVmInstanceId(instance.getVmInstanceId())) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            CompletableFuture.runAsync(() -> {
                lock.lock();
                try {
                while (true) {
                    if (onDemandDeploymentService.getLock(deploymentName)) {
                        Thread.sleep(15000);
                        continue;
                    }
                    
                    // BoshDirector.INSTANCE_STATE_DETACHED : detached
                    onDemandDeploymentService.updateInstanceState(deploymentName, instanceName, instance.getVmInstanceId(), BoshDirector.INSTANCE_STATE_DETACHED);
                    cloudFoundryService.DelSecurityGurop(common.cloudFoundryClient().securityGroups(), instance.getSpaceGuid(), instance.getDashboardUrl());
                    logger.info("VM DETACHED SUCCEED : VM_ID : " + instance.getVmInstanceId());
                    break;
                }
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
                lock.unlock();
            }, executor);

        }
        return instance;
    }

    @Override
    public ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest request)
            throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException, ServiceInstanceDoesNotExistException {
        throw new ServiceBrokerException("Not Supported");
    }


	@Override
	public ServiceInstance getOperationServiceInstance(String instanceId) {
		JpaOnDemandServiceInstance instance = jpaServiceInstanceRepository.findByServiceInstanceId(instanceId);
        if (onDemandDeploymentService.runningTask(deploymentName, instance)) {
            logger.info("인스턴스 생성완료");
            ExecutorService executor = Executors.newSingleThreadExecutor();
            CompletableFuture.runAsync(() -> {
                try {
                    if (instance.getAppGuid() != null) {
                        ServiceBindingsV2 serviceBindingsV2 = common.cloudFoundryClient().serviceBindingsV2();
                        ApplicationsV2 applicationsV2 = common.cloudFoundryClient().applicationsV2();
                        cloudFoundryService.ServiceInstanceAppBinding(instance.getAppGuid(), instance.getServiceInstanceId(), (Map) this.objectMapper.readValue(instance.getApp_parameter(), Map.class), serviceBindingsV2, applicationsV2);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }, executor);
            return instance;
        }
        logger.info("인스턴스 생성중");
        return null;
	}

}