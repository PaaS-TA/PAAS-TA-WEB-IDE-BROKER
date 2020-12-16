package org.paasta.servicebroker.webide.config;


import org.openpaas.paasta.bosh.director.BoshDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@ComponentScan(value = "org.openpaas.paasta.ondemand.service.impl")
public class BoshConfig {

    @Value("${bosh.client_id}")
    public String client_id;
    @Value("${bosh.client_secret}")
    public String client_secret;
    @Value("${bosh.url}")
    public String url;
    @Value("${bosh.oauth_url}")
    public String oauth_url;
    @Value("${bosh.deployment_name}")
    public String deployment_name;
    @Value("${bosh.instance_name}")
    public String instance_name;

//    @Value("${bosh.version}")
    public String bosh_version;

    private static final Logger LOGGER = LoggerFactory.getLogger(BoshConfig.class);



    @Bean
    BoshDirector boshDirector(){
        LOGGER.info("client_id    = " + client_id);
        LOGGER.info("client_secret= " + client_secret);
        LOGGER.info("url          = " + url);
        LOGGER.info("oauth_url    = " + oauth_url);
        LOGGER.info("bosh_version = " + bosh_version);

        BoshDirector boshDirector = null;
        if (StringUtils.isEmpty(bosh_version)){
            LOGGER.info("bosh_versionX= " + bosh_version);
            boshDirector = new BoshDirector(client_id,client_secret,url,oauth_url);
        }else {

            LOGGER.info("bosh_versionO= " + bosh_version);

            String[] versions = bosh_version.split("\\.");
            String minor = null;
            if (versions.length >1){
                minor = padLeftZeros(versions[1] ,3);
            }
            String new_version = "";
            for(int i = 0  ; i < versions.length ; i++){
                new_version += (i == 1) ? minor : versions[i];
            }
            LOGGER.info("bosh_versionN= " + new_version);

            boshDirector = new BoshDirector(client_id,client_secret,url,oauth_url, new_version);
        }

        return boshDirector;
    }

    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
