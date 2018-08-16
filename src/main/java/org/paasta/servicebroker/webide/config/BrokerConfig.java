package org.paasta.servicebroker.webide.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


/**
 * 기본 Spring boot packages의 component 검색하고,
 * jpa사용을 위해서 @EntityScan, @EnableJpaRepositories를 추가
 *
 * @author sjchoi
 * @since 2018.08.14
 * @version 1.0
 */

@Configuration
@EnableJpaRepositories("org.paasta.servicebroker.webide.repo")
@EntityScan(value = "org.openpaas.servicebroker.webide.model")
@ComponentScan(basePackages = { "org.openpaas.servicebroker", "org.openpaas.servicebroker"})
public class BrokerConfig {

}
