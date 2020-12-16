package org.paasta.servicebroker.webide.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.cloudfoundry.client.v2.serviceinstances.ListServiceInstancesResponse;
import org.cloudfoundry.client.v2.serviceinstances.ServiceInstanceEntity;
import org.cloudfoundry.client.v2.serviceinstances.ServiceInstanceResource;
import org.cloudfoundry.client.v2.serviceplans.ListServicePlansResponse;
import org.cloudfoundry.client.v2.serviceplans.ServicePlanResource;
import org.cloudfoundry.client.v2.services.ListServicesResponse;
import org.cloudfoundry.client.v2.services.ServiceResource;
import org.cloudfoundry.client.v2.spaces.GetSpaceResponse;
import org.openpaas.paasta.bosh.director.BoshDirector;
import org.paasta.servicebroker.webide.model.DeploymentInstance;
import org.paasta.servicebroker.webide.model.JpaOnDemandServiceInstance;
import org.paasta.servicebroker.webide.model.JpaServiceList;
import org.paasta.servicebroker.webide.repo.JpaOnDemandServiceInstanceRepository;
import org.paasta.servicebroker.webide.service.impl.CloudFoundryService;
import org.paasta.servicebroker.webide.service.impl.OnDemandDeploymentService;
import org.paasta.servicebroker.webide.service.impl.WebIdeAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "webide")
public class InitializationConfig {


    private List<String> servers = new ArrayList<String>();

    public List<String> getServers() {
        return this.servers;
    }

    private Logger logger = LoggerFactory.getLogger(InitializationConfig.class);


    @Autowired
    private WebIdeAdminService webIdeAdminService;
    
    @Autowired
    OnDemandDeploymentService onDemandDeploymentService;
    
    @Autowired
    CloudFoundryService cloudFoundryService;
    
    @Autowired
    JpaOnDemandServiceInstanceRepository jpaServiceInstanceRepository;
    
    @Value("${bosh.deployment_name}")
    public String deploymentName;

    @Value("${bosh.instance_name}")
    public String instanceName;

    @Bean
    public List<JpaServiceList> initCiServer() {

        List<JpaServiceList> jpaServiceLists = new ArrayList<>();

        int i = 0;
        for (String url : servers) {
            logger.info(url + "save");
            try {
                url = url.trim();
                if (url.length() > 0) {
                    JpaServiceList jpaInfo = new JpaServiceList();
                    jpaInfo.setNo(++i);
                    jpaInfo.setWebIdeService(url);
                    webIdeAdminService.saveInfo(jpaInfo);
                }

            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        
        /* 기 설치된 eclipse-che 의 OnDemand Service 정보 등록 시작 */
        List<DeploymentInstance> deploymentInstanceList = onDemandDeploymentService.getVmInstance(deploymentName, instanceName);
//        List<DeploymentInstance> startedDeploymentInstances = deploymentInstanceList.stream().filter((x) -> x.getState().equals(BoshDirector.INSTANCE_STATE_START) && x.getJobState().equals("running")).collect(Collectors.toList());
        // public ip 추가를 위한 재 배포시 모든 vm 의 jobState 상태가 running 상태가 아님, 그래서 jobState 상태는 일단 제거 
        List<DeploymentInstance> startedDeploymentInstances = deploymentInstanceList.stream().filter((x) -> x.getState().equals(BoshDirector.INSTANCE_STATE_START)).collect(Collectors.toList());
        ListServiceInstancesResponse listServiceInstancesResponse = cloudFoundryService.getServiceList();
        
        Map<String, String> serviceInfoMap = new HashMap<String, String>();
        
        ListServicesResponse serviceResponse = cloudFoundryService.getServices();
        for(ServiceResource serviceResource : serviceResponse.getResources()) {
        	serviceInfoMap.put("service-id-" + serviceResource.getMetadata().getId(), serviceResource.getEntity().getUniqueId());
        }
        
        ListServicePlansResponse servicePlansResponse = cloudFoundryService.getServicePlans();
        for(ServicePlanResource servicePlanResource : servicePlansResponse.getResources()) {
        	serviceInfoMap.put("plan-id-" + servicePlanResource.getMetadata().getId(), servicePlanResource.getEntity().getUniqueId());
        }
        
        for(ServiceInstanceResource instanceResource : listServiceInstancesResponse.getResources()) {
        	ServiceInstanceEntity instanceEntity = instanceResource.getEntity();
        	String serviceInstanceId = instanceResource.getMetadata().getId();
        	String dashboardUrl = instanceEntity.getDashboardUrl();
        	String serviceId = instanceEntity.getServiceId();
        	String servicePlanId = instanceEntity.getServicePlanId();
        	String spaceId = instanceEntity.getSpaceId();
        	String dashboardIp = "";
        	try {
				dashboardIp = new URL(dashboardUrl).getHost();
			} catch (MalformedURLException e) {
				continue;
			}
        	
        	GetSpaceResponse spaceResponse = cloudFoundryService.getSpace(spaceId);
        	String orgId = spaceResponse.getEntity().getOrganizationId();
        	
        	for(DeploymentInstance deploymentInstance : startedDeploymentInstances) {
        		String vmId = deploymentInstance.getId();
        		String vmIps = deploymentInstance.getIps();
        		String jobName = deploymentInstance.getJobName();
        		
        		// JOB_NAME 이 'eclipse-che' 이고 vm 에 등록된 IP 가 SERVICE 의 DASHBOARD IP 와 같으면 서비스에서 사용되는 VM 이다 라고 판단함.
        		if(instanceName.equals(jobName) && vmIps.indexOf(dashboardIp) > -1) {
        			JpaOnDemandServiceInstance onDemandServiceInstance = new JpaOnDemandServiceInstance();
        			onDemandServiceInstance.setServiceInstanceId(serviceInstanceId);
        			onDemandServiceInstance.setDashboardUrl(dashboardUrl);
        			onDemandServiceInstance.setPlanId(serviceInfoMap.get("plan-id-" + servicePlanId));
        			onDemandServiceInstance.setServiceDefinitionId(serviceInfoMap.get("service-id-" + serviceId));
        			onDemandServiceInstance.setSpaceGuid(spaceId);
        			onDemandServiceInstance.setOrganizationGuid(orgId);
        			onDemandServiceInstance.setVmInstanceId(vmId);
        			
        			jpaServiceInstanceRepository.save(onDemandServiceInstance);
        		}
        	}
        }
        /* 기 설치된 eclipse-che 의 OnDemand Service 정보 등록 종료 */
        
        return jpaServiceLists;
    }

}
