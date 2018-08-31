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
import org.paasta.servicebroker.webide.model.JpaServiceInstance;
import org.paasta.servicebroker.webide.model.JpaServiceList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


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


    @Autowired
    private WebIdeAdminService webIdeAdminService;


    /**
     * 1.서비스 인스턴스 유뮤를 확인
     * 2.서비스 인스턴스 guid를 확인
     * 3.서비스 인스턴스를 생성 및 저장
     *
     * @author sjchoi
     * @since 2018.08.23
     */
    @Override
    public ServiceInstance createServiceInstance(CreateServiceInstanceRequest request)
            throws ServiceInstanceExistsException, ServiceBrokerException {
        logger.debug("WebIdeServiceInstanceService CLASS createServiceInstance");

        ServiceInstance serviceInstance = webIdeAdminService.findById(request.getServiceInstanceId());

        if (serviceInstance != null) {
            throw new ServiceInstanceExistsException(new ServiceInstance(request));
        }

        ServiceInstance instance = webIdeAdminService.findByOrganizationGuid(request.getOrganizationGuid());

        if (instance != null) {
            logger.debug("This organization already has one or more service instances.", request.getServiceInstanceId());
            throw new ServiceBrokerException("This organization already has one or more service instances.");
        }

        List<JpaServiceList> jpaServiceInstanceList = webIdeAdminService.findAllDashboardUrls();
        ServiceInstance result = null;
        String dashboardUrl = "";
        int cnt = 0;
        for (JpaServiceList jpaServiceList : jpaServiceInstanceList) {
            JpaServiceInstance service = webIdeAdminService.findByDashboardUrl(jpaServiceList.getWebIdeService());

            if (service == null) {
                logger.info("1COUNT :: " + cnt);
                dashboardUrl = jpaServiceList.getWebIdeService();
                ++cnt;
                break;
            }
        }
        logger.info("0COUNT :: " + cnt);
        if (cnt == 0) {
            logger.debug("This organization not anymore service instances.", request.getServiceInstanceId());
            throw new ServiceBrokerException("This organization not anymore service instances");
        }
        result = new ServiceInstance(request).withDashboardUrl(dashboardUrl);

        logger.info("1 " + result.getDashboardUrl());
        logger.info("2 " + result.getOrganizationGuid());
        logger.info("3 " + result.getPlanId());
        logger.info("4 " + result.getServiceDefinitionId());
        logger.info("5 " + result.getSpaceGuid());
        logger.info("6 " + result.getServiceInstanceId());

        webIdeAdminService.save(result);

        return result;
    }


    @Override
    public ServiceInstance getServiceInstance(String id) {
        return webIdeAdminService.findById(id);
    }

    /**
     * 1. 조회된 서비스 인스턴스가 없을 경우 예외처리
     * 2. 조회된 서비스 인스턴스 정보로 해당 서비스 인스턴스를 삭제
     * 3. 대시보드 URL을 생성
     *
     * @author sjchoi
     * @since 2018.08.24
     */
    @Override
    public ServiceInstance deleteServiceInstance(DeleteServiceInstanceRequest request)
            throws ServiceBrokerException {
        ServiceInstance instance = webIdeAdminService.findById(request.getServiceInstanceId());
        try {
            logger.info(":::deleteServiceInstance:::findByIdDelete");
            if (instance == null) return null;
            logger.info(":::deleteServiceInstance delete");
            webIdeAdminService.delete(instance.getServiceInstanceId());
        }catch (Exception e){

        }
        return instance;
    }

    @Override
    public ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest request)
            throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException, ServiceInstanceDoesNotExistException {
        throw new ServiceBrokerException("Not Supported");
    }

}