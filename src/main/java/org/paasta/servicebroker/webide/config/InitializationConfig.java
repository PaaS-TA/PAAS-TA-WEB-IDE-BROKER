package org.paasta.servicebroker.webide.config;

import org.paasta.servicebroker.webide.model.JpaServiceList;
import org.paasta.servicebroker.webide.service.impl.WebIdeAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
@ConfigurationProperties(prefix = "webide")
public class InitializationConfig {


    private List<String> servers = new ArrayList<String>();

    public List<String> getServers() {
        return this.servers;
    }

    private Logger logger = LoggerFactory.getLogger(InitializationConfig.class);


    @Autowired
    private WebIdeAdminService webIdeAdminService;

    @Bean
    public List<JpaServiceList> initCiServer() {

        List<JpaServiceList> jpaServiceLists = new ArrayList<>();

        int i = 0;
        for (String url : servers) {
            logger.info(url + "save");
            try {
                url = url.trim();
                if (url.length() > 0) {
                    JpaServiceList jpaInfo = new JpaServiceList();
                    jpaInfo.setNo(++i);
                    jpaInfo.setWebIdeService(url);
                    webIdeAdminService.saveInfo(jpaInfo);
                }

            } catch (Exception e) {
            }
        }
        return jpaServiceLists;
    }

}
