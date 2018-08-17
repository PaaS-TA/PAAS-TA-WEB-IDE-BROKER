package org.paasta.servicebroker.apiplatform.test;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.paasta.servicebroker.apiplatform.model.ServiceDefinitionFixture;
import org.paasta.servicebroker.webide.service.impl.WebIdeCatalogService;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class WebIdeCatalogServiceTest {

    @Spy
    WebIdeCatalogService webIdeCatalogService;

    @Mock
    Catalog catalog;


    @Test
    public void test_getCatalog() throws Exception {

        catalog = new Catalog(ServiceDefinitionFixture.getCatalog());
        webIdeCatalogService = new WebIdeCatalogService(catalog);

        Catalog result = webIdeCatalogService.getCatalog();

        assertThat(result.getServiceDefinitions().get(0).getId(), is(TestConstants.SERVICEDEFINITION_ID));
        assertThat(result.getServiceDefinitions().get(0).getName(), is(TestConstants.SERVICEDEFINITION_NAME));
        assertThat(result.getServiceDefinitions().get(0).isBindable(), is(false));
        assertThat(result.getServiceDefinitions().get(0).isPlanUpdatable(), is(false));

        ServiceDefinition serviceDefinition = webIdeCatalogService.getServiceDefinition(TestConstants.SERVICEDEFINITION_ID);
        assertThat(serviceDefinition.getId(), is(TestConstants.SERVICEDEFINITION_ID));
        assertThat(serviceDefinition.getName(), is(TestConstants.SERVICEDEFINITION_NAME));
        assertThat(serviceDefinition.isBindable(), is(false));
        assertThat(serviceDefinition.isPlanUpdatable(), is(false));

    }
}
