package org.paasta.servicebroker.webide.service.impl;

import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.webide.exception.WebIdeServiceException;
import org.paasta.servicebroker.webide.model.JpaServiceInstance;
import org.paasta.servicebroker.webide.model.WebIde;
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
import java.util.HashMap;
import java.util.Map;

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


    public void delete(String id) throws WebIdeServiceException {
        try {
            jpaServiceInstanceRepository.delete(id);
        } catch (Exception e) {
            throw handleException(e);
        }
    }


    public void save(ServiceInstance serviceInstance) throws WebIdeServiceException {
        try {
            jpaServiceInstanceRepository.save(new JpaServiceInstance(serviceInstance));
        } catch (Exception e) {
            throw handleException(e);
        }
    }


    public boolean createDashboard(ServiceInstance serviceInstance, String owner, String serviceType) throws WebIdeServiceException {
        try {
            Map params = new HashMap();

            params.put("id", serviceInstance.getServiceInstanceId());
            params.put("owner", owner);
            params.put("serviceType", serviceType);

            String reqUrl = PaastaWebideUrl + V2_URL + "/serviceInstance/" +  serviceInstance.getServiceInstanceId();

            if (PaastaWebideUsername.isEmpty()) this.authorization = "";
            else
                this.authorization = "Basic " + Base64Utils.encodeToString((PaastaWebideUsername + ":" + PaastaWebidePassword).getBytes(StandardCharsets.UTF_8));

            HttpHeaders reqHeaders = new HttpHeaders();
            if (!"".equals(authorization)) reqHeaders.add(AUTHORIZATION_HEADER_KEY, authorization);
            reqHeaders.add(CONTENT_TYPE_HEADER_KEY, "application/json");

            HttpEntity<Object> reqEntity = new HttpEntity<>(params, reqHeaders);

            logger.info("POST >> Request: {}, {baseUrl} : {}, Content-Type: {}", HttpMethod.POST, reqUrl, reqHeaders.get(CONTENT_TYPE_HEADER_KEY));
            ResponseEntity<Map> resEntity = restTemplate.exchange(reqUrl, HttpMethod.POST, reqEntity, Map.class);
            logger.info("send :: Response Status: {}", resEntity.getStatusCode());

            if (resEntity.getStatusCode().equals(HttpStatus.OK)) {

                try {
                    logger.info("send :: Response Body: {}", resEntity.getBody());
                } catch (Exception e) {

                }
                if (resEntity.getBody() != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            throw handleException(e);
        }
    }


    public boolean deleteDashboard(ServiceInstance serviceInstance) throws WebIdeServiceException {
        try {

            String reqUrl = PaastaWebideUrl + V2_URL + "/serviceInstance/" + serviceInstance.getServiceInstanceId();

            if (PaastaWebideUsername.isEmpty()) this.authorization = "";
            else
                this.authorization = "Basic " + Base64Utils.encodeToString((PaastaWebideUsername + ":" + PaastaWebidePassword).getBytes(StandardCharsets.UTF_8));

            HttpHeaders reqHeaders = new HttpHeaders();
            if (!"".equals(authorization)) reqHeaders.add(AUTHORIZATION_HEADER_KEY, authorization);
            reqHeaders.add(CONTENT_TYPE_HEADER_KEY, "application/json");

            HttpEntity<Object> reqEntity = new HttpEntity<>(reqHeaders);

            logger.info("POST >> Request: {}, {baseUrl} : {}, Content-Type: {}", HttpMethod.DELETE, reqUrl, reqHeaders.get(CONTENT_TYPE_HEADER_KEY));
            ResponseEntity<String> resEntity = restTemplate.exchange(reqUrl, HttpMethod.DELETE, reqEntity, String.class);
            logger.info("send :: Response Status: {}", resEntity.getStatusCode());

            if (resEntity.getStatusCode().equals(HttpStatus.OK)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            throw handleException(e);
        }
    }


    private WebIdeServiceException handleException(Exception e) {
        logger.warn(e.getLocalizedMessage(), e);
        return new WebIdeServiceException(e.getLocalizedMessage());
    }

}

