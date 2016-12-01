package com.empyrean.auth.config;

import org.postgresql.jdbc3.Jdbc3PoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * Created by chirdeep on 17/11/2016.
 */

@Configuration
@EnableAuthorizationServer
public class OAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    private DataSource dataSource;
    private AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder passwordEncoder;

    public OAuthServerConfig(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.dataSource = new Jdbc3PoolingDataSource();
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.passwordEncoder(passwordEncoder);
//    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("phoenix-api-client")
                .secret("verysecretivesecret")
                .scopes("READ", "WRITE", "DELETE")
                .authorizedGrantTypes("implicit", "refresh_tokens", "password", "authorization_code")
                .and()
                .withClient("phoenix-mobile-client")
                .secret("verysecretivesecret")
                .scopes("READ")
                .authorizedGrantTypes("implicit", "refresh_tokens", "password", "authorization_code");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authorizationCodeServices(authorizationCodeServices())
                .tokenStore(tokenStore())
                .tokenEnhancer(jwtTokenEnhancer())
                .authenticationManager(authenticationManager);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        return new JwtAccessTokenConverter();
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

}


