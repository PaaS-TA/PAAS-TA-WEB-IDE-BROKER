//package org.paasta.servicebroker.webide.config;
//
//
//import org.paasta.servicebroker.webide.model.CiInfo;
//import org.paasta.servicebroker.webide.service.impl.DeliveryPipelineAdminService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//public class InitializationConfig {
//
//    private Logger logger = LoggerFactory.getLogger(InitializationConfig.class);
//
//    @Value("${ci.server.shared.urls}")
//    String SHARED_URLS;
//
//    @Value("${ci.server.dedicated.urls}")
//    String DEDICATED_URLS;
//
//    private final String SHARED = "Shared";
//    private final String DEDICATED = "Dedicated";
//
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    @Autowired
//    private DeliveryPipelineAdminService deliveryPipelineAdminService;
//
//    @Bean
//    public Boolean initCiServer() {
//        List<CiInfo> ciInfos = new ArrayList<>();
//
//        List<CiInfo> sharedList = parsingCiInfo(SHARED_URLS, SHARED);
//        List<CiInfo> dedicatedList = parsingCiInfo(DEDICATED_URLS, DEDICATED);
//
//        ciInfos.addAll(sharedList);
//        ciInfos.addAll(dedicatedList);
//
//        Map result = deliveryPipelineAdminService.initCiInfos(ciInfos);
//
//        List<Map> register = (List<Map>) result.get("register");
//        logger.info("Init Ci_Server info......Result");
//        for (Map map : register) {
//            logger.info("Result :: " + map.get("serverUrl") + " :: " + map.get("type") + " :: " + map.get("process") + " :: " + map.get("status"));
//        }
//
//        List<Map> remove = (List<Map>) result.get("remove");
//        if (remove.size() > 0) {
//            logger.info("Remove Ci_Server info......Result");
//            for (Map map : remove) {
//                logger.info("Result :: " + map.get("serverUrl") + " :: " + map.get("type") + " :: " + map.get("process") + " :: " + map.get("status"));
//            }
//        }
//
//        return true;
//    }
//
//
//    private List<CiInfo> parsingCiInfo(String data, String type) {
//        List<CiInfo> ciInfos = new ArrayList<>();
//        String str = data.replace("[", "").replace("]", "").replace("\"", "");
//        String[] strArray = str.split(",");
//        for (String url : strArray) {
//            url = url.trim();
//            if (url.length() > 0) {
//                CiInfo ciInfo = new CiInfo();
//                ciInfo.setServerUrl(url);
//                ciInfo.setType(type);
//                ciInfos.add(ciInfo);
//            }
//        }
//        return ciInfos;
//    }
//}
