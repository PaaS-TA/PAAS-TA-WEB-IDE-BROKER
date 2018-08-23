package org.paasta.servicebroker.webide.service.impl;

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
import org.springframework.stereotype.Service;


import java.util.List;


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
        logger.debug("WebIdeServiceInstanceService CLASS createServiceInstance");

        // 서비스 인스턴스 유무 확인
        ServiceInstance serviceInstance = webIdeAdminService.findById(request.getServiceInstanceId());

        if (serviceInstance != null) {
            throw new ServiceInstanceExistsException(new ServiceInstance(request));
        }

        // 서비스 인스턴스 Guid 확인
        ServiceInstance instance = webIdeAdminService.findByOrganizationGuid(request.getOrganizationGuid());

        if (instance != null) {
            logger.debug("This organization already has one or more service instances.", request.getServiceInstanceId());
            throw new ServiceBrokerException("This organization already has one or more service instances.");
        }

        List<JpaServiceInstance> jpaServiceInstanceList =  webIdeAdminService.findByuseYn("N");

        if (jpaServiceInstanceList == null || jpaServiceInstanceList.size() == 0) {
            logger.debug("This organization not anymore service instances.", request.getServiceInstanceId());
            throw new ServiceBrokerException("This organization not anymore service instances");
        }

        ServiceInstance result = new ServiceInstance(request).withDashboardUrl(jpaServiceInstanceList.get(0).getDashboardUrl());

        logger.info("1 " + result.getDashboardUrl());
        logger.info("2 " + result.getOrganizationGuid());
        logger.info("3 " + result.getPlanId());
        logger.info("4 " + result.getServiceDefinitionId());
        logger.info("5 " + result.getSpaceGuid());
        logger.info("6 " + result.getServiceInstanceId());

        // 서비스인스턴스 정보를 저장
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

        if (instance == null) return null;
        // 조회된 ServiceInstance정보로 해당 ServiceInstance정보를 삭제
        webIdeAdminService.delete(instance.getServiceInstanceId());

        return instance;
    }

    @Override
    public ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest request)
            throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException, ServiceInstanceDoesNotExistException {
        throw new ServiceBrokerException("Not Supported");
    }


}