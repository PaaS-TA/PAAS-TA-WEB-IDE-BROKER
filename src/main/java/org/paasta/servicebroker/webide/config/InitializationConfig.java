package org.paasta.servicebroker.webide.config;

import org.paasta.servicebroker.webide.model.JpaServiceList;
import org.paasta.servicebroker.webide.service.impl.WebIdeAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class InitializationConfig {
    private Logger logger = LoggerFactory.getLogger(InitializationConfig.class);

    @Value("${webide.servers}")
    String SERVICE_URLS;

    @Autowired
    private WebIdeAdminService webIdeAdminService;

    @Bean
    public List<JpaServiceList> initCiServer() {

        List<JpaServiceList> jpaServiceLists = new ArrayList<>();

        String str = SERVICE_URLS.replace("[", "").replace("]", "").replace("\"", "");
        String[] strArray = str.split(",");

        int i = 0;
        for (String url : strArray) {
            logger.info(url);
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
