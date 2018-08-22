package org.paasta.servicebroker.webide.service.impl;

import net.minidev.json.JSONObject;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.openpaas.servicebroker.exception.ServiceInstanceExistsException;
import org.openpaas.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.openpaas.servicebroker.service.ServiceInstanceService;
import org.paasta.servicebroker.webide.exception.WebIdeServiceException;

import org.paasta.servicebroker.webide.model.JpaServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author whalsrn0710@bluedigm.com
 */
@Service
public class WebIdeServiceInstanceService implements ServiceInstanceService {

    private static final Logger logger = LoggerFactory.getLogger(WebIdeServiceInstanceService.class);


    @Autowired
    private WebIdeAdminService webIdeAdminService;

    @Override
    public ServiceInstance createServiceInstance(CreateServiceInstanceRequest request)
            throws ServiceInstanceExistsException, ServiceBrokerException {
        System.out.printf("WebIdeServiceInstanceService CLASS createServiceInstance");
        logger.debug("WebIdeServiceInstanceService CLASS createServiceInstance");

        logger.info(request.getServiceDefinitionId());
        logger.info(request.getPlanId());
        logger.info(request.getOrganizationGuid());
        logger.info(request.getSpaceGuid());
        logger.info(request.getServiceInstanceId());
        logger.info(request.getParameters().toString());

        // 서비스 인스턴스 체크
        ServiceInstance serviceInstance = webIdeAdminService.findById(request.getServiceInstanceId());

        if (serviceInstance != null) {
            throw new ServiceInstanceExistsException(new ServiceInstance(request));
        }

        // 서비스 인스턴스 Guid Check
        ServiceInstance instance = webIdeAdminService.findByOrganizationGuid(request.getOrganizationGuid());

        if (instance != null) {
            logger.debug("This organization already has one or more service instances.", request.getServiceInstanceId());
            throw new ServiceBrokerException("This organization already has one or more service instances.");
        }

        List<JpaServiceInstance> jpaServiceInstanceList =  webIdeAdminService.findByuseYn("N");

        if (jpaServiceInstanceList == null || jpaServiceInstanceList.size() == 0) {
            logger.debug("This organization not any more service instances.", request.getServiceInstanceId());
            throw new ServiceBrokerException("This organization already has one or more service instances.");
        }

        ServiceInstance result = new ServiceInstance(request).withDashboardUrl(jpaServiceInstanceList.get(0).getDashboardUrl());

        logger.info("1 " + result.getDashboardUrl());
        logger.info("2 " + result.getOrganizationGuid());
        logger.info("3 " + result.getPlanId());
        logger.info("4 " + result.getServiceDefinitionId());
        logger.info("5 " + result.getSpaceGuid());
        logger.info("6 " + result.getServiceInstanceId());

        // ServiceInstance 정보를 저장
        webIdeAdminService.save(result);

        return result;
    }


    @Override
    public ServiceInstance getServiceInstance(String id) {
        return webIdeAdminService.findById(id);
    }


    @Override
    public ServiceInstance deleteServiceInstance(DeleteServiceInstanceRequest request) throws WebIdeServiceException {
        ServiceInstance instance = webIdeAdminService.findById(request.getServiceInstanceId());
        try {
            // 조회된 ServiceInstance가 없을경우 예외처리
            if (instance == null) {
                return null;
            }
            // 조회된 ServiceInstance정보로 해당 Database를 삭제
//            webIdeAdminService.deleteDashboard(instance);
            // 조회된 ServiceInstance정보로 해당 ServiceInstance정보를 삭제
            webIdeAdminService.delete(instance.getServiceInstanceId());
        } catch (Exception e) {

        }
        return instance;
    }

    @Override
    public ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest request)
            throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException, ServiceInstanceDoesNotExistException {
        throw new ServiceBrokerException("Not Supported");
    }


}