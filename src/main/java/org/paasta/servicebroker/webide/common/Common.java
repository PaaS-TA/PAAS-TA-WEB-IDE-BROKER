package org.paasta.servicebroker.webide.common;

import java.util.Calendar;
import java.util.Date;

import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.paasta.servicebroker.webide.config.TokenGrantTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class Common {

    @Value("${cloudfoundry.cc.api.url}")
    public String apiTarget;

    @Value("${cloudfoundry.cc.api.uaaUrl}")
    public String uaaTarget;

    @Value("${cloudfoundry.cc.api.sslSkipValidation}")
    public boolean cfskipSSLValidation;

    @Value("${cloudfoundry.user.admin.username}")
    public String adminUserName;

    @Value("${cloudfoundry.user.admin.password}")
    public String adminPassword;

    @Autowired
    PaastaConnectionContext paastaConnectionContext;

    @Autowired
    PaastaTokenContext paastaTokenContext;


    public ReactorCloudFoundryClient cloudFoundryClient() {
        return ReactorCloudFoundryClient.builder().connectionContext(connectionContext()).tokenProvider(tokenProvider()).build();
    }


    /**
     * DefaultConnectionContext 가져온다.
     *
     * @return DefaultConnectionContext
     */
    public DefaultConnectionContext connectionContext() {
        if (paastaConnectionContext == null) {
            paastaConnectionContext = new PaastaConnectionContext(defaultConnectionContextBuild(apiTarget, cfskipSSLValidation), new Date());
        } else {
            if (paastaConnectionContext.getCreate_time() != null) {
                if(ContextAndTokenTimeOut(paastaConnectionContext, 10)) {
                    paastaConnectionContext.getConnectionContext().dispose();
                    paastaConnectionContext = null;
                    paastaConnectionContext = new PaastaConnectionContext(defaultConnectionContextBuild(apiTarget, cfskipSSLValidation), new Date());
                }
            } else {
                paastaConnectionContext = null;
                paastaConnectionContext = new PaastaConnectionContext(defaultConnectionContextBuild(apiTarget, cfskipSSLValidation), new Date());
            }
        }
        return paastaConnectionContext.getConnectionContext();
    }

    public DefaultConnectionContext defaultConnectionContextBuild(String cfApiUrl, boolean cfskipSSLValidation) {
        return DefaultConnectionContext.builder().apiHost(cfApiUrl.replace("https://", "").replace("http://", "")).skipSslValidation(cfskipSSLValidation).keepAlive(true).build();
    }

    /**
     * TokenGrantTokenProvider 생성하여, 반환한다.
     *
     * @param token
     * @return DefaultConnectionContext
     * @throws Exception
     */
    public TokenProvider tokenProvider(String token) {

        if (token.indexOf("bearer") < 0) {
            token = "bearer " + token;
        }
        TokenGrantTokenProvider tokenProvider = new TokenGrantTokenProvider(token);
        return tokenProvider;

    }

    public PasswordGrantTokenProvider tokenProvider() {
        if (paastaTokenContext == null) {
            paastaTokenContext = new PaastaTokenContext(PasswordGrantTokenProvider.builder().password(adminPassword).username(adminUserName).build(), new Date());
        } else if(paastaTokenContext.getCreate_time() != null && ContextAndTokenTimeOut(paastaTokenContext, 5)){
            paastaTokenContext = new PaastaTokenContext(PasswordGrantTokenProvider.builder().password(adminPassword).username(adminUserName).build(), new Date());
        }
        return paastaTokenContext.tokenProvider();
    }

    public boolean ContextAndTokenTimeOut(PaastaContextInterface paastaContextInterface, int timelimit){
        Calendar now = Calendar.getInstance();
        Calendar create_time = Calendar.getInstance();
        create_time.setTime(paastaContextInterface.getCreate_time());
        create_time.add(Calendar.MINUTE, timelimit);
        return create_time.getTimeInMillis() < now.getTimeInMillis();
    }
}
