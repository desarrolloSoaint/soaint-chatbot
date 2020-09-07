/*package com.soaint.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
*/
/*@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Value("${security.oauth2.client_secret}")
    private String CLIENT_SECRET;
    @Value("${security.oauth2.client_id}")
    private String CLIENT_ID;
    @Value("${security.oauth2.client_normal_id}")
    private String CLIENT_NORMAL_ID;
    @Value("${security.oauth2.grant_type_password}")
    private String GRANT_TYPE_CREDENTIALS;
    @Value("${security.oauth2.grant_type_password}")
    private String GRANT_TYPE_PASSWORD;
    @Value("${security.oauth2.grant_type_password}")
    private String AUTHORIZATION_CODE;
    @Value("${security.oauth2.refresh_token}")
    private String REFRESH_TOKEN;
    @Value("${security.oauth2.implicit}")
    private String IMPLICIT;
    @Value("${security.oauth2.scope_read}")
    private String SCOPE_READ;
    @Value("${security.oauth2.scope_write}")
    private String SCOPE_WRITE;
    @Value("${security.oauth2.trust}")
    private String TRUST;
    @Value("${security.oauth2.signin_key}")
    private String SIGNIN_KEY;
    @Value("${security.oauth2.access_tokn_validity_seconds}")
    private int ACCESS_TOKEN_VALIDITY_SECONDS;
    @Value("${security.oauth2.frefresh_tokn_validity_seconds}")
    private int FREFRESH_TOKEN_VALIDITY_SECONDS;

    @Value("${security.oauth2.authoritie_client}")
    private String AUTHORITIE_TRUSTED_CLIENT;
    @Value("${security.oauth2.authoritie_client}")
    private String AUTHORITIE_CLIENT;

	@Value("${resource.id:spring-boot-application}")
    private String resourceId;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNIN_KEY);
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .pathMapping("/oauth/token", "/api/v1/auth/token")
                .authenticationManager(this.authenticationManager)
                .accessTokenConverter(accessTokenConverter());
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority(" + AUTHORITIE_TRUSTED_CLIENT + ")")
            .checkTokenAccess("hasAuthority(" + AUTHORITIE_TRUSTED_CLIENT + ")");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient(CLIENT_NORMAL_ID)
                .authorizedGrantTypes(AUTHORIZATION_CODE, IMPLICIT)
                .authorities(AUTHORITIE_CLIENT)
                .scopes(SCOPE_READ, SCOPE_WRITE)
                .resourceIds(resourceId)
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
        .and()
            .withClient(CLIENT_ID)
                .authorizedGrantTypes(GRANT_TYPE_CREDENTIALS, GRANT_TYPE_PASSWORD)
                .authorities(AUTHORITIE_TRUSTED_CLIENT)
                .scopes(SCOPE_READ, SCOPE_WRITE)
                .resourceIds(resourceId)
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
                .secret(CLIENT_SECRET);
                // .secret("{noop}secret");
    }

}
*/