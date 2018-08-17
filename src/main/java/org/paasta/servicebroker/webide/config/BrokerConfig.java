package org.paasta.servicebroker.webide.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * Force the base spring boot packages to be searched for dependencies.
 *
 * @author sgreenberg
 *
 */

@Configuration
@EnableJpaRepositories("org.paasta.servicebroker.webide.repo")
@EntityScan(value = "org.paasta.servicebroker.webide.model")
@ComponentScan(basePackages = { "org.paasta.servicebroker", "org.openpaas.servicebroker"})
public class BrokerConfig {
}
