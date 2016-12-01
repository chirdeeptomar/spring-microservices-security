package com.empyrean.profile.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * Created by chirdeep on 29/11/2016.
 */

@Configuration
public class ServiceConfig {

    @Autowired
    private ResourceServerProperties sso;

    @Bean
    @Primary
    public ResourceServerTokenServices customUserInfoTokenServices(){
        return new CustomUserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
    }

    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context){
        return new OAuth2RestTemplate(resource, context);
    }
}
