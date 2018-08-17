package org.paasta.servicebroker.apiplatform.model;

import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.webide.model.JpaServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by user on 2017-09-14.
 */
public class JpaRepositoryFixture {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static JpaServiceInstance getJpaServiceInstance() {
        CreateServiceInstanceRequest createServiceInstanceRequest = RequestFixture.getCreateServiceInstanceRequest();
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
        JpaServiceInstance jpaServiceInstance = new JpaServiceInstance();
        new JpaServiceInstance(serviceInstance);
        return jpaServiceInstance;

    }

    private void print(JpaServiceInstance jpaServiceInstance) {
        logger.info("serviceInstanceId : " + jpaServiceInstance.getServiceInstanceId());
        logger.info("dashboardUrl : " + jpaServiceInstance.getDashboardUrl());
        logger.info("OrgGuid : " + jpaServiceInstance.getOrganizationGuid());
        logger.info("spaceGuid : " + jpaServiceInstance.getSpaceGuid());
        logger.info("planId : " + jpaServiceInstance.getPlanId());
        logger.info("serviceId : " + jpaServiceInstance.getServiceDefinitionId());

    }
}