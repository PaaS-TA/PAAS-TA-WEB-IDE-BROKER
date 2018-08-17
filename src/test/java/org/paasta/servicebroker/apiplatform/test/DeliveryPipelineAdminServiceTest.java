package org.paasta.servicebroker.apiplatform.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.paasta.servicebroker.apiplatform.model.JpaRepositoryFixture;
import org.paasta.servicebroker.apiplatform.model.RequestFixture;
import org.paasta.servicebroker.apiplatform.model.ServiceInstanceFixture;
import org.paasta.servicebroker.webide.exception.WebIdeServiceException;
import org.paasta.servicebroker.webide.model.JpaServiceInstance;
import org.paasta.servicebroker.webide.repo.JpaServiceInstanceRepository;
import org.paasta.servicebroker.webide.service.impl.WebIdeAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * DELIVERY-PIPELINE-SERVICE-BROKER
 *
 * Created by user on 2017-09-13.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:test.properties")
public class DeliveryPipelineAdminServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Mock
    JpaServiceInstanceRepository jpaServiceInstanceRepository;

    @InjectMocks
    WebIdeAdminService deliveryPipelineAdminService;


    @Value("${paasta.delivery.pipeline.api.url}")
    private String apiUrl;
    @Value("${paasta.delivery.pipeline.api.username}")
    private String apiUsername;
    @Value("${paasta.delivery.pipeline.api.password}")
    private String apiPassword;
    @Value("${service.dashboard.url}")
    private String dashboardUrl;


    @Mock
    private MockRestServiceServer mockServer;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        print();

        mockServer = MockRestServiceServer.createServer(restTemplate);

        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiUrl", apiUrl);
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiUsername", apiUsername);
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiPassword", apiPassword);
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "restTemplate", restTemplate);

    }



    @Test
    public void test_findById_case() throws Exception {
        JpaServiceInstance jpaRepositoryFixture = JpaRepositoryFixture.getJpaServiceInstance();
        JpaServiceInstance jpaServiceInstance = new JpaServiceInstance();
        jpaServiceInstance.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        jpaServiceInstance.setPlanId(jpaRepositoryFixture.getPlanId());
        jpaServiceInstance.setServiceDefinitionId(jpaRepositoryFixture.getPlanId());
        jpaServiceInstance.setSpaceGuid(jpaRepositoryFixture.getSpaceGuid());
        jpaServiceInstance.setOrganizationGuid(jpaRepositoryFixture.getOrganizationGuid());
        jpaServiceInstance.setDashboardUrl(jpaRepositoryFixture.getDashboardUrl());

        when(jpaServiceInstanceRepository.findOne(anyString())).thenReturn(jpaServiceInstance);
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findById(jpaServiceInstance.getServiceInstanceId());
        assertThat(serviceInstance.getServiceInstanceId(), is(TestConstants.SV_INSTANCE_ID_001));
    }


    @Test
    public void test_findById_case_null() throws Exception {
        when(jpaServiceInstanceRepository.findOne(anyString())).thenReturn(null);
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findById(TestConstants.SV_INSTANCE_ID_001);
        assertThat(serviceInstance, is(nullValue()));
    }

    @Test
    public void test_findByOrgGuid() throws Exception {
        JpaServiceInstance jpaRepositoryFixture = JpaRepositoryFixture.getJpaServiceInstance();
        JpaServiceInstance jpaServiceInstance = new JpaServiceInstance();
        jpaServiceInstance.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        jpaServiceInstance.setPlanId(jpaRepositoryFixture.getPlanId());
        jpaServiceInstance.setServiceDefinitionId(jpaRepositoryFixture.getPlanId());
        jpaServiceInstance.setSpaceGuid(jpaRepositoryFixture.getSpaceGuid());
        jpaServiceInstance.setOrganizationGuid(jpaRepositoryFixture.getOrganizationGuid());
        jpaServiceInstance.setDashboardUrl(jpaRepositoryFixture.getDashboardUrl());

        when(jpaServiceInstanceRepository.findByOrganizationGuid(anyString())).thenReturn(jpaServiceInstance);
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findByOrganizationGuid(jpaServiceInstance.getOrganizationGuid());
        assertThat(serviceInstance.getServiceInstanceId(), is(jpaServiceInstance.getServiceInstanceId()));
        assertThat(serviceInstance.getPlanId(), is(jpaServiceInstance.getPlanId()));
        assertThat(serviceInstance.getOrganizationGuid(), is(jpaServiceInstance.getOrganizationGuid()));
        assertThat(serviceInstance.getSpaceGuid(), is(jpaServiceInstance.getSpaceGuid()));
    }


    @Test
    public void test_findByOrgGuid_case_null() throws Exception {
        JpaServiceInstance jpaRepositoryFixture = JpaRepositoryFixture.getJpaServiceInstance();
        JpaServiceInstance jpaServiceInstance = new JpaServiceInstance();
        jpaServiceInstance.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        jpaServiceInstance.setPlanId(jpaRepositoryFixture.getPlanId());
        jpaServiceInstance.setServiceDefinitionId(jpaRepositoryFixture.getPlanId());
        jpaServiceInstance.setSpaceGuid(jpaRepositoryFixture.getSpaceGuid());
        jpaServiceInstance.setOrganizationGuid(jpaRepositoryFixture.getOrganizationGuid());
        jpaServiceInstance.setDashboardUrl(jpaRepositoryFixture.getDashboardUrl());

        when(jpaServiceInstanceRepository.findByOrganizationGuid(anyString())).thenReturn(null);
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findByOrganizationGuid(jpaServiceInstance.getOrganizationGuid());
        assertThat(serviceInstance, is(nullValue()));
    }

    @Test
    public void test_createDashboard1() throws Exception {
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
        ResponseEntity responseEntity = new ResponseEntity<Map>(HttpStatus.OK);
        when(restTemplate.exchange(
                Matchers.anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                Matchers.<Class<Map>>any())).thenReturn(responseEntity);
        boolean result =  deliveryPipelineAdminService.createDashboard(serviceInstance, TestConstants.PARAM_KEY_OWNER,TestConstants.PARAM_SERVICE_TYPE_SHARED);
        assertEquals(result,true);

    }
    @Test
    public void test_createDashboard2() throws Exception {
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiUsername", "");
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
        ResponseEntity responseEntity = new ResponseEntity<Map>(HttpStatus.OK);
        when(restTemplate.exchange(
                Matchers.anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                Matchers.<Class<Map>>any())).thenReturn(responseEntity);

        boolean result =  deliveryPipelineAdminService.createDashboard(serviceInstance, TestConstants.PARAM_KEY_OWNER,TestConstants.PARAM_SERVICE_TYPE_DEDICATED);
        assertEquals(result,true);

    }

    @Test
    public void test_createDashboard3() throws Exception {
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);


        request.setParameters(null);
        assertThatThrownBy(() -> deliveryPipelineAdminService.createDashboard(serviceInstance, TestConstants.PARAM_KEY_OWNER,TestConstants.PARAM_SERVICE_TYPE_SHARED))
                .isInstanceOf(WebIdeServiceException.class);
    }


    @Test
    public void test_save() throws Exception{
        JpaServiceInstance jpaRepositoryFixture = JpaRepositoryFixture.getJpaServiceInstance();
        JpaServiceInstance jpaServiceInstance = new JpaServiceInstance();
        jpaServiceInstance.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        jpaServiceInstance.setPlanId(jpaRepositoryFixture.getPlanId());
        jpaServiceInstance.setServiceDefinitionId(jpaRepositoryFixture.getPlanId());
        jpaServiceInstance.setSpaceGuid(jpaRepositoryFixture.getSpaceGuid());
        jpaServiceInstance.setOrganizationGuid(jpaRepositoryFixture.getOrganizationGuid());
        jpaServiceInstance.setDashboardUrl(jpaRepositoryFixture.getDashboardUrl());

        when(jpaServiceInstanceRepository.save(any(JpaServiceInstance.class))).thenReturn(jpaServiceInstance);
        deliveryPipelineAdminService.save(ServiceInstanceFixture.getServiceInstance());
        verify(jpaServiceInstanceRepository).save(any(JpaServiceInstance.class));

    }

    @Test
    public void test_deleteDashboard1() throws Exception {
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);

        ResponseEntity responseEntity = new ResponseEntity<Map>(HttpStatus.OK);
        when(restTemplate.exchange(
                Matchers.anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                Matchers.<Class<Map>>any())).thenReturn(responseEntity);
        boolean result =  deliveryPipelineAdminService.deleteDashboard(serviceInstance);
        assertEquals(result,true);
    }


    @Test
    public void test_deleteDashboard2() throws Exception {
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);

        ResponseEntity responseEntity = new ResponseEntity<Map>(HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(
                Matchers.anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                Matchers.<Class<Map>>any())).thenReturn(responseEntity);
        boolean result =  deliveryPipelineAdminService.deleteDashboard(serviceInstance);
        assertEquals(result,false);
    }

    @Test
    public void test_deleteDashboard3() throws Exception {
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);


        request.setParameters(null);
        assertThatThrownBy(() -> deliveryPipelineAdminService.deleteDashboard(serviceInstance))
                .isInstanceOf(WebIdeServiceException.class);
    }



    @Test
    public void test_delete() throws Exception{

        doNothing().when(jpaServiceInstanceRepository).delete(TestConstants.SV_INSTANCE_ID_001);
        deliveryPipelineAdminService.delete(TestConstants.SV_INSTANCE_ID_001);
        verify(jpaServiceInstanceRepository).delete(TestConstants.SV_INSTANCE_ID_001);

    }

    private void print() {
        logger.info("apiUrl : " + apiUrl);
        logger.info("apiUsername : " + apiPassword);
        logger.info("apiPassword : " + apiPassword);
    }
}
