package org.paasta.servicebroker.webide.service.impl;

import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.service.ServiceInstanceService;
import org.paasta.servicebroker.webide.exception.WebIdeServiceException;
import org.paasta.servicebroker.webide.model.JpaServiceInstance;
import org.paasta.servicebroker.webide.repo.JpaServiceInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;


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

    /**
     * The web-ide master host url.
     */
    @Value("${paasta.webide.url}")
    private String PaastaWebideUrl;
    @Value("${paasta.webide.username}")
    String  PaastaWebideUsername;
    @Value("${paasta.webide.password}")
    String  PaastaWebidePassword;

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";

    public static final String V2_URL = "/v2";

    @Autowired
    private JpaServiceInstanceRepository jpaServiceInstanceRepository;

    @Autowired
    RestTemplate restTemplate;

    private String authorization;

    public ServiceInstance findById(String id) {
        JpaServiceInstance newJpaServiceInstance = jpaServiceInstanceRepository.findOne(id);

        if (newJpaServiceInstance == null) return null;

        return new ServiceInstance(new CreateServiceInstanceRequest(newJpaServiceInstance.getServiceDefinitionId(),
                newJpaServiceInstance.getPlanId(),
                newJpaServiceInstance.getOrganizationGuid(),
                newJpaServiceInstance.getSpaceGuid()).withServiceInstanceId(newJpaServiceInstance.getServiceInstanceId()));
    }

    public ServiceInstance findByOrganizationGuid(String id) {
        JpaServiceInstance newJpaServiceInstance = jpaServiceInstanceRepository.findByOrganizationGuid(id);

        if (newJpaServiceInstance == null) return null;

        return new ServiceInstance(new CreateServiceInstanceRequest(newJpaServiceInstance.getServiceDefinitionId(),
                newJpaServiceInstance.getPlanId(),
                newJpaServiceInstance.getOrganizationGuid(),
                newJpaServiceInstance.getSpaceGuid()).withServiceInstanceId(newJpaServiceInstance.getServiceInstanceId()));
    }


    /**
     * 서비스 브로커를 조회한다.
     */
    public boolean webideList(ServiceInstance serviceInstance) throws WebIdeServiceException {
        try{

            String reqUrl = PaastaWebideUrl + V2_URL + "/catalog";

            if (PaastaWebideUsername.isEmpty()) this.authorization = "";
            else
                this.authorization = "Basic " + Base64Utils.encodeToString((PaastaWebideUsername + ":" + PaastaWebidePassword).getBytes(StandardCharsets.UTF_8));

            HttpHeaders reqHeaders = new HttpHeaders();
            if (!"".equals(authorization)) reqHeaders.add(AUTHORIZATION_HEADER_KEY, authorization);
            reqHeaders.add(CONTENT_TYPE_HEADER_KEY, "application/json");
            HttpEntity<Object> reqEntity = new HttpEntity<>(reqHeaders);
            ResponseEntity<String> resEntity = restTemplate.exchange(reqUrl, HttpMethod.GET, reqEntity, String.class);
            if(resEntity.getStatusCode().equals(HttpStatus.OK)){
                return true;
            }else{
                return  false;
            }

        }catch (Exception e) {
            throw handleException(e);
        }
    }


    private WebIdeServiceException handleException(Exception e) {
        logger.warn(e.getLocalizedMessage(), e);
        return new WebIdeServiceException(e.getLocalizedMessage());
    }

}
