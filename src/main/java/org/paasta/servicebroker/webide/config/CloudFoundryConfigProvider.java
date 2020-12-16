package org.paasta.servicebroker.webide.config;


import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.paasta.servicebroker.webide.common.Common;
import org.paasta.servicebroker.webide.common.PaastaConnectionContext;
import org.paasta.servicebroker.webide.common.PaastaTokenContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class CloudFoundryConfigProvider {

    @Bean
    PaastaConnectionContext connectionContext(@Value("${cloudfoundry.cc.api.url}") String apiTarget, @Value("${cloudfoundry.cc.api.sslSkipValidation}") Boolean sslSkipValidation) {
        Common common = new Common();
        return new PaastaConnectionContext(common.defaultConnectionContextBuild(apiTarget, sslSkipValidation), new Date());
    }

    @Bean
    PaastaTokenContext tokenProvider(@Value("${cloudfoundry.user.admin.username}") String username, @Value("${cloudfoundry.user.admin.password}") String password) {
        return new PaastaTokenContext(PasswordGrantTokenProvider.builder().password(password).username(username).build(), new Date());
    }

    @Bean
    Common common(){
        return new Common();
    }
}
