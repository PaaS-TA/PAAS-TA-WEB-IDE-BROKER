package org.paasta.servicebroker.webide.config;

import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.Plan;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class
CatalogConfig {


    @Value("${serviceDefinition.id}")
    String SERVICEDEFINITION_ID;

    @Value("${serviceDefinition.name}")
    String SERVICEDEFINITION_NAME;

    @Value("${serviceDefinition.desc}")
    String SERVICEDEFINITION_DESC;

    @Value("${serviceDefinition.bindable}")
    String SERVICEDEFINITION_BINDABLE_STRING;

    @Value("${serviceDefinition.planupdatable}")
    String SERVICEDEFINITION_PLANUPDATABLE_STRING;

    @Value("${serviceDefinition.plan1.id}")
    String SERVICEDEFINITION_PLAN1_ID;

    @Value("${serviceDefinition.plan1.name}")
    String SERVICEDEFINITION_PLAN1_NAME;

    @Value("${serviceDefinition.plan1.desc}")
    String SERVICEDEFINITION_PLAN1_DESC;

    @Value("${serviceDefinition.plan1.type}")
    String SERVICEDEFINITION_PLAN1_TYPE;

    @Value("${serviceDefinition.plan2.id}")
    String SERVICEDEFINITION_PLAN2_ID;

    @Value("${serviceDefinition.plan2.name}")
    String SERVICEDEFINITION_PLAN2_NAME;

    @Value("${serviceDefinition.plan2.desc}")
    String SERVICEDEFINITION_PLAN2_DESC;

    @Value("${serviceDefinition.plan2.type}")
    String SERVICEDEFINITION_PLAN2_TYPE;


	@Bean
	public Catalog catalog() {
		boolean SERVICEDEFINITION_BINDABLE = false;
		boolean SERVICEDEFINITION_PLANUPDATABLE = false;

		if(SERVICEDEFINITION_BINDABLE_STRING.toUpperCase().trim().equals("TRUE"))
			SERVICEDEFINITION_BINDABLE = true;

		if(SERVICEDEFINITION_PLANUPDATABLE_STRING.toUpperCase().trim().equals("TRUE"))
			SERVICEDEFINITION_PLANUPDATABLE = true;

		return new Catalog(Arrays.asList(
				new ServiceDefinition(
                        SERVICEDEFINITION_ID,
						SERVICEDEFINITION_NAME,
						SERVICEDEFINITION_DESC,
						SERVICEDEFINITION_BINDABLE, // bindable
						SERVICEDEFINITION_PLANUPDATABLE, // updatable
						Arrays.asList(
								new Plan(SERVICEDEFINITION_PLAN1_ID,
										SERVICEDEFINITION_PLAN1_NAME,
										SERVICEDEFINITION_PLAN2_DESC,
										getPlanMetadata(SERVICEDEFINITION_PLAN1_TYPE)),
								new Plan(SERVICEDEFINITION_PLAN2_ID,
										SERVICEDEFINITION_PLAN2_NAME,
										SERVICEDEFINITION_PLAN2_DESC,
										getPlanMetadata(SERVICEDEFINITION_PLAN2_TYPE))),
						Arrays.asList(SERVICEDEFINITION_PLAN1_NAME, SERVICEDEFINITION_PLAN2_NAME),
						getServiceDefinitionMetadata(),
						null,
						null)));
	}

	/* Used by Pivotal CF console */
	private Map<String, Object> getServiceDefinitionMetadata() {
		Map<String, Object> sdMetadata = new HashMap<String, Object>();
		sdMetadata.put("displayName", "delivery-pipeline");
		sdMetadata.put("imageUrl", "");
		sdMetadata.put("longDescription", "Paas-TA Delivery Pipeline");
		sdMetadata.put("providerDisplayName", "PaaS-TA");
		sdMetadata.put("documentationUrl", "https://paas-ta.kr");
		sdMetadata.put("supportUrl", "https://paas-ta.kr");
		return sdMetadata;
	}

	private Map<String, Object> getPlanMetadata(String planType) {
		Map<String, Object> planMetadata = new HashMap<>();
		planMetadata.put("costs", getCosts(planType));
		planMetadata.put("bullets", getBullets(planType));

		return planMetadata;
	}

	private List<Map<String, Object>> getCosts(String planType) {
		Map<String, Object> costsMap = new HashMap<>();
		Map<String, Object> amount = new HashMap<>();

		switch (planType) {
			case "A":
				amount.put("usd", 0.0);
				costsMap.put("amount", amount);
				costsMap.put("unit", "MONTHLY");

				break;
			case "B":
				amount.put("usd", 0.0);
				costsMap.put("amount", amount);
				costsMap.put("unit", "MONTHLY");

				break;
			default:
				amount.put("usd", 0.0);
				costsMap.put("amount", amount);
				costsMap.put("unit", "MONTHLY");
				break;
		}

		return Collections.singletonList(costsMap);
	}

	private List<String> getBullets(String planType) {
		if (planType.equals("A")) {
			return Arrays.asList("Delivery pipeline shared build server use",
					"Deployment pipeline build service using a shared server");
		} else if (planType.equals("B")) {
			return Arrays.asList("Delivery pipeline dedicated build server use",
					"Deployment pipeline build service using a dedicated server");
		}
		return Arrays.asList("Delivery pipeline shared build server use",
				"Deployment pipeline build service using a shared server");
	}
}