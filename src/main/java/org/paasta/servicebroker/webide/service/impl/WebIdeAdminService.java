package org.paasta.servicebroker.webide.service.impl;

import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.webide.exception.WebIdeServiceException;
import org.paasta.servicebroker.webide.model.JpaServiceList;
import org.paasta.servicebroker.webide.model.JpaServiceInstance;
import org.paasta.servicebroker.webide.repo.JpaServiceInstanceRepository;
import org.paasta.servicebroker.webide.repo.JpaServiceListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * WebIdeAdminService Property
 *
 * @author sjchoi
 * @version 1.0
 * @since 2018.08.21
 */
@Service
public class WebIdeAdminService {

    private Logger logger = LoggerFactory.getLogger(WebIdeAdminService.class);


    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";


    @Autowired
    private JpaServiceInstanceRepository jpaServiceInstanceRepository;


    @Autowired
    private JpaServiceListRepository jpaServiceListRepository;

    public ServiceInstance findById(String id) {
        JpaServiceInstance newJpaServiceInstance = jpaServiceInstanceRepository.findByServiceInstanceId(id);

        if (newJpaServiceInstance == null) return null;

        return new ServiceInstance(new CreateServiceInstanceRequest(newJpaServiceInstance.getServiceDefinitionId(),
                newJpaServiceInstance.getPlanId(),
                newJpaServiceInstance.getOrganizationGuid(),
                newJpaServiceInstance.getSpaceGuid()).withServiceInstanceId(newJpaServiceInstance.getServiceInstanceId()));
    }


    public ServiceInstance findByIdDelete(String id) {
        JpaServiceInstance newJpaServiceInstance = jpaServiceInstanceRepository.findByServiceInstanceId(id);

        if (newJpaServiceInstance == null) return null;

        return new ServiceInstance(new DeleteServiceInstanceRequest(newJpaServiceInstance.getServiceInstanceId(),
                newJpaServiceInstance.getServiceDefinitionId(),
                newJpaServiceInstance.getPlanId()));
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


    public void delete(String serviceInstanceId) throws WebIdeServiceException {
        try {
//            jpaServiceInstance.setUseYn("N");
//            jpaServiceInstance.setUserId("");
//            jpaServiceInstance.setPlanId("");
//            jpaServiceInstance.setServiceInstanceId("");
//            jpaServiceInstance.setServiceDefinitionId("");
//            jpaServiceInstance.setSpaceGuid("");
//            jpaServiceInstance.setOrganizationGuid("");
            jpaServiceInstanceRepository.delete(serviceInstanceId);
        } catch (Exception e) {
            throw handleException(e);
        }
    }


    public void save(ServiceInstance serviceInstance) throws WebIdeServiceException {
        try {
            JpaServiceInstance jpaServiceInstance = new JpaServiceInstance(serviceInstance);
            jpaServiceInstanceRepository.save(jpaServiceInstance);
        } catch (Exception e) {
            e.printStackTrace();
            throw handleException(e);
        }
    }

    public JpaServiceInstance findByDashboardUrl(String dashboardUrl){
        return jpaServiceInstanceRepository.findByDashboardUrl(dashboardUrl);
    }

    public List<JpaServiceList> findAllDashboardUrls() {
        List<JpaServiceList> jpaServiceIists = jpaServiceListRepository.findAll();
        return jpaServiceIists;
    }


    private WebIdeServiceException handleException(Exception e) {
        logger.warn(e.getLocalizedMessage(), e);
        return new WebIdeServiceException(e.getLocalizedMessage());
    }


    public void saveInfo(JpaServiceList jpaInfo) throws WebIdeServiceException {
        try {
            jpaServiceListRepository.save(jpaInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw handleException(e);
        }
    }
}

