package org.paasta.servicebroker.webide.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * DELIVERY-PIPELINE-SERVICE-BROKER
 *
 * Created by user on 2017-10-23.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Rest template rest template.
     *
     * @return the rest template
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
