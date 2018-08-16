package org.paasta.servicebroker.webide.service.impl;

import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.openpaas.servicebroker.service.ServiceInstanceService;
import org.paasta.servicebroker.webide.exception.WebIdeServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class WebIdeAdminServiceInstanceService implements ServiceInstanceService {

    private static final Logger logger = LoggerFactory.getLogger(WebIdeAdminServiceInstanceService.class);

    public static final String TOKEN_SUID = "[SUID]";
    public static final String TOKEN_OWNER = "owner";

    public static final String shared = "a5930564-6212-11e7-907b-b6006ad3dps1";
    public static final String dedicated = "a5930564-6212-11e7-907b-b6006ad3dps2";

    @Value("${service.dashboard.url}")
    private String dashboardUrl;

    @Autowired
    private WebIdeAdminService webIdeAdminService;

    @Override
    public ServiceInstance getServiceInstance(String id) {
        return webIdeAdminService.findById(id);
    }

    @Override
    public ServiceInstance createServiceInstance(CreateServiceInstanceRequest createServiceInstanceRequest) throws ServiceBrokerException {
        return null;
    }

    @Override
    public ServiceInstance deleteServiceInstance(DeleteServiceInstanceRequest deleteServiceInstanceRequest) throws ServiceBrokerException {
        return null;
    }

    @Override
    public ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest updateServiceInstanceRequest) throws ServiceBrokerException {
        return null;
    }

}