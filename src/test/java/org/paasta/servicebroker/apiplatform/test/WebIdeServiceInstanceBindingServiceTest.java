package org.paasta.servicebroker.apiplatform.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.paasta.servicebroker.apiplatform.model.RequestFixture;
import org.paasta.servicebroker.webide.service.impl.WebIdeServiceInstanceBindingService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * DELIVERY-PIPELINE-SERVICE-BROKER
 *
 * 배포파이프라인 - binding 지원 안함.
 *
 * Created by user on 2017-09-12.
 */
public class WebIdeServiceInstanceBindingServiceTest {

    @InjectMocks
    WebIdeServiceInstanceBindingService webIdeServiceInstanceBindingService;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createServiceInstanceBinding() throws Exception {

        CreateServiceInstanceBindingRequest request = RequestFixture.getCreateServiceInstanceBindingRequest();

        assertThatThrownBy(() -> webIdeServiceInstanceBindingService.createServiceInstanceBinding(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Not Supported");
    }

    @Test
    public void test_deleteServiceInstanceBinding() throws Exception {

        DeleteServiceInstanceBindingRequest request = RequestFixture.getDeleteServiceInstanceBindingRequest();

        assertThatThrownBy(() -> webIdeServiceInstanceBindingService.deleteServiceInstanceBinding(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Not Supported");
    }

}
