package org.paasta.servicebroker.apiplatform.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceExistsException;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.paasta.servicebroker.apiplatform.model.RequestFixture;
import org.paasta.servicebroker.apiplatform.model.ServiceInstanceFixture;
import org.paasta.servicebroker.webide.service.impl.WebIdeAdminService;
import org.paasta.servicebroker.webide.service.impl.WebIdeServiceInstanceService;
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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * DELIVERY-PIPELINE-SERVICE-BROKER
 *
 * Created by user on 2017-09-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:test.properties")
public class DeliveryPipelineServiceInstanceServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @InjectMocks
    WebIdeServiceInstanceService deliveryPipelineServiceInstanceService;

    @Mock
    WebIdeAdminService deliveryPipelineAdminService;


    @Value("${paasta.delivery.pipeline.api.url}")
    private String apiUrl;
    @Value("${paasta.delivery.pipeline.api.username}")
    private String apiUsername;
    @Value("${paasta.delivery.pipeline.api.password}")
    private String apiPassword;
    @Value("${service.dashboard.url}")
    private String dashboardUrl;


    private MockRestServiceServer mockServer;

    public static final String TOKEN_SUID = "[SUID]";

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);


        ReflectionTestUtils.setField(deliveryPipelineServiceInstanceService, "dashboardUrl", dashboardUrl);

        mockServer = MockRestServiceServer.createServer(restTemplate);

        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiUrl", apiUrl);
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiUsername", apiUsername);
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiPassword", apiPassword);
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "restTemplate", restTemplate);

    }

    //----------------[ createServiceInstance Test]

    @Test
    public void test_validation_case1() throws Exception {
        // CreateServiceInstanceRequest Parameter 유효성 체크
        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);

        // case 1. request.getParameters() == null
        logger.info("case 1. request.getParameters() == null");
        request.setParameters(null);
        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Required");


        // case 2. request.getParameters().isEmpty()
        logger.info("case 2. request.getParameters().isEmpty()");
        request.setParameters(new HashMap<>());
        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Required");

        // case3.
        //!(request.getParameters().containsKey(param_owner) && request.getParameters().containsKey(param_orgname))
        logger.info("case 3. !(request.getParameters().containsKey(param_owner)");
        Map testParam = new HashMap<>();
        testParam.put("testparam", "testparam");
        request.setParameters(testParam);
        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Required");
    }

    @Test
    public void test_validation_case2() throws Exception {

        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();

        when(deliveryPipelineAdminService.findById(anyString())).thenReturn(serviceInstance);
        when(deliveryPipelineAdminService.findByOrganizationGuid(anyString())).thenReturn(null);

        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceInstanceExistsException.class).hasMessageContaining("ServiceInstance with the given ID already exists");

    }

    @Test
    public void test_validation_case3() throws Exception {

        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();

        when(deliveryPipelineAdminService.findById(anyString())).thenReturn(null);
        when(deliveryPipelineAdminService.findByOrganizationGuid(anyString())).thenReturn(serviceInstance);


        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("This organization already has one or more service instances.");

    }

    @Test
    public void test_getServiceInstance_case1() throws Exception {

        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();

        when(deliveryPipelineAdminService.findById(anyString())).thenReturn(serviceInstance);
        ServiceInstance result = deliveryPipelineServiceInstanceService.getServiceInstance(serviceInstance.getServiceInstanceId());

        assertThat(result.getServiceInstanceId(), is(serviceInstance.getServiceInstanceId()));
        assertThat(result.getServiceDefinitionId(), is(serviceInstance.getServiceDefinitionId()));
        assertThat(result.getOrganizationGuid(), is(serviceInstance.getOrganizationGuid()));
        assertThat(result.getPlanId(), is(serviceInstance.getPlanId()));
        assertThat(result.getSpaceGuid(), is(serviceInstance.getSpaceGuid()));

    }


    @Test
    public void test_createServiceInstance_case() throws Exception {
        /*
        * 인스턴스가 있을 때 삭제 요청을 할 경우
        */

        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);

        when(deliveryPipelineAdminService.findByOrganizationGuid(anyString())).thenReturn(null);
        when(deliveryPipelineAdminService.findById(anyString())).thenReturn(null);
        ResponseEntity responseEntity = new ResponseEntity<Map>(HttpStatus.OK);
        when(restTemplate.exchange(
                Matchers.anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                Matchers.<Class<Map>>any())).thenReturn(responseEntity);
        when(deliveryPipelineAdminService.createDashboard(any(ServiceInstance.class),anyString(),anyString())).thenReturn(true);

        doNothing().when(deliveryPipelineAdminService).save(any(ServiceInstance.class));


        deliveryPipelineServiceInstanceService.createServiceInstance(request);



        verify(deliveryPipelineAdminService).findById(request.getServiceInstanceId());
        verify(deliveryPipelineAdminService).findByOrganizationGuid(request.getOrganizationGuid());
        verify(deliveryPipelineAdminService).createDashboard(any(ServiceInstance.class), anyString(),anyString());
        verify(deliveryPipelineAdminService).save(any(ServiceInstance.class));

    }


    @Test
    public void test_deleteServiceInstance_case1() throws Exception {
       /*
        * 인스턴스가 있을때 삭제요청
        */

        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
        DeleteServiceInstanceRequest request = RequestFixture.getDeleteServiceInstanceRequest();
        when(deliveryPipelineAdminService.findById(anyString())).thenReturn(serviceInstance);
        ResponseEntity responseEntity = new ResponseEntity<Map>(HttpStatus.OK);
        when(restTemplate.exchange(
                Matchers.anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                Matchers.<Class<Map>>any())).thenReturn(responseEntity);
        when(deliveryPipelineAdminService.deleteDashboard(any(ServiceInstance.class))).thenReturn(true);
        doNothing().when(deliveryPipelineAdminService).delete(anyString());


        ServiceInstance result =  deliveryPipelineServiceInstanceService.deleteServiceInstance(request);

        assertThat(result.getServiceInstanceId(), is(serviceInstance.getServiceInstanceId()));
        assertThat(result.getServiceDefinitionId(), is(serviceInstance.getServiceDefinitionId()));
        assertThat(result.getPlanId(), is(serviceInstance.getPlanId()));
        assertThat(result.getOrganizationGuid(), is(serviceInstance.getOrganizationGuid()));
        assertThat(result.getSpaceGuid(), is(serviceInstance.getSpaceGuid()));


        verify(deliveryPipelineAdminService).findById(request.getServiceInstanceId());
        verify(deliveryPipelineAdminService).deleteDashboard(serviceInstance);
        verify(deliveryPipelineAdminService).delete(anyString());

    }

    @Test
    public void test_deleteServiceInstance_case2() throws Exception {
        /*
        * 인스턴스가 없는데 삭제 요청을 할 경우
        */

        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
        DeleteServiceInstanceRequest request = RequestFixture.getDeleteServiceInstanceRequest();
        when(deliveryPipelineAdminService.findById(anyString())).thenReturn(null);
        ResponseEntity responseEntity = new ResponseEntity<Map>(HttpStatus.OK);
        when(restTemplate.exchange(
                Matchers.anyString(),
                any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                Matchers.<Class<Map>>any())).thenReturn(responseEntity);
        when(deliveryPipelineAdminService.deleteDashboard(any(ServiceInstance.class))).thenReturn(true);
        doNothing().when(deliveryPipelineAdminService).delete(anyString());


        ServiceInstance result =  deliveryPipelineServiceInstanceService.deleteServiceInstance(request);

        assertThat(result, is(nullValue()));


        verify(deliveryPipelineAdminService).findById(request.getServiceInstanceId());
        verify(deliveryPipelineAdminService,times(0)).deleteDashboard(serviceInstance);
        verify(deliveryPipelineAdminService,times(0)).delete(anyString());

    }






    //----------------[ updateServiceInstance Test]
    @Test
    public void test_updateServiceInstance_case1() throws Exception {


        UpdateServiceInstanceRequest request = RequestFixture.getUpdateServiceInstanceRequest();

        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.updateServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Not Supported");
    }

}
