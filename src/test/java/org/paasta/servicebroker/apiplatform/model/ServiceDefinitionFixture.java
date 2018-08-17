package org.paasta.servicebroker.apiplatform.model;

import org.openpaas.servicebroker.model.Plan;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.paasta.servicebroker.apiplatform.common.TestConstants;

import java.util.*;

public class ServiceDefinitionFixture {

    public static ServiceDefinition getService() {

        return new ServiceDefinition(
                TestConstants.SERVICEDEFINITION_ID,
                TestConstants.SERVICEDEFINITION_NAME,
                "A delivery pipeline service for application development.provision parameters : parameters {owner : owner}",
                false, // bindable
                false, // updatable
                Arrays.asList(
                        new Plan(TestConstants.SERVICEDEFINITION_PLAN_ID,
                                "default",
                                "This is a default service plan. All services are created equally.",
                                null)),
                Arrays.asList("delivery-pipeline"),
                getMetadata(),
                null,
                null);
    }

    /* Service Metadata */
    private static Map<String,Object> getMetadata() {
        // Service Metadata
        Map<String,Object> metadata = new HashMap<String,Object>();
        metadata.put("displayName", TestConstants.SERVICEDEFINITION_NAME);
        metadata.put("imageUrl","imageUrl");
        metadata.put("longDescription","longDescription");
        metadata.put("providerDisplayName","providerDisplayName");
        metadata.put("documentationUrl","documentationUrl");
        metadata.put("supportUrl","supportUrl");
        return metadata;
    }

    public static List<ServiceDefinition> getCatalog() {
        List<ServiceDefinition> result = new ArrayList<>();
        result.add(getService());

        return result;
    }
}
