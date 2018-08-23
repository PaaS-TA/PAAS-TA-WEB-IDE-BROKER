package org.paasta.servicebroker.webide.service.impl;

import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.webide.exception.WebIdeServiceException;
import org.paasta.servicebroker.webide.model.JpaServiceInstance;
import org.paasta.servicebroker.webide.repo.JpaServiceInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.List;


/**
 * WebIdeAdminService Property
 *
 * @author sjchoi
 * @since 2018.08.14
 * @version 1.0
 */
@Service
public class WebIdeAdminService {

    private Logger logger = LoggerFactory.getLogger(WebIdeAdminService.class);

    @Value("${paasta.webide.username}")
    String  PaastaWebideUsername;
    @Value("${paasta.webide.password}")
    String  PaastaWebidePassword;

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";


    @Autowired
    private JpaServiceInstanceRepository jpaServiceInstanceRepository;


    public ServiceInstance findById(String id) {
        JpaServiceInstance newJpaServiceInstance = jpaServiceInstanceRepository.findOne(id);

        if (newJpaServiceInstance == null) return null;

        return new ServiceInstance(new CreateServiceInstanceRequest(newJpaServiceInstance.getServiceDefinitionId(),
                newJpaServiceInstance.getPlanId(),
                newJpaServiceInstance.getOrganizationGuid(),
                newJpaServiceInstance.getSpaceGuid()).withServiceInstanceId(newJpaServiceInstance.getServiceInstanceId()));
    }

    public List<JpaServiceInstance> findByuseYn(String use_yn) {
        List<JpaServiceInstance> newJpaServiceInstance = jpaServiceInstanceRepository.findByUseYn(use_yn);
        return newJpaServiceInstance;
    }

    public ServiceInstance findByOrganizationGuid(String id) {
        JpaServiceInstance newJpaServiceInstance = jpaServiceInstanceRepository.findByOrganizationGuid(id);

        if (newJpaServiceInstance == null) return null;

        return new ServiceInstance(new CreateServiceInstanceRequest(newJpaServiceInstance.getServiceDefinitionId(),
                newJpaServiceInstance.getPlanId(),
                newJpaServiceInstance.getOrganizationGuid(),
                newJpaServiceInstance.getSpaceGuid()).withServiceInstanceId(newJpaServiceInstance.getServiceInstanceId()));
    }


    public void delete(String id) throws WebIdeServiceException {
        try {
            jpaServiceInstanceRepository.delete(id);
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public void save(ServiceInstance serviceInstance) throws WebIdeServiceException {
        try {
            JpaServiceInstance jpaServiceInstance = new JpaServiceInstance(serviceInstance);
            jpaServiceInstance.setUseYn("Y");
            jpaServiceInstanceRepository.save(jpaServiceInstance);
        } catch (Exception e) {
            throw handleException(e);
        }
    }


    private WebIdeServiceException handleException(Exception e) {
        logger.warn(e.getLocalizedMessage(), e);
        return new WebIdeServiceException(e.getLocalizedMessage());
    }

}

