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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

        // ServiceInstance 정보를 저장
        webIdeAdminService.save(instance);

        return instance;
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