package com.dkolesnik.springplayground.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * oAuth2 Authorization Server Implementation
 * 
 * @author   Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version  0.1.1
 * @since    2022-07-26
 */
@Configuration
@EnableAuthorizationServer
@Order(100)
public class AuthorizationServerOAuth2Config extends AuthorizationServerConfigurerAdapter {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private TokenStore tokenStore;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
    }

    // TODO read docs
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer()));
//        endpoints.tokenStore(tokenStore()).tokenEnhancer(tokenEnhancerChain).authenticationManager(authenticationManager);
        endpoints.authenticationManager(authenticationManager)
            .tokenStore(tokenStore)
            .reuseRefreshTokens(false)
            .userDetailsService(userDetailsService)
            .tokenGranter(tokenGranter(endpoints));
    }
    
    // TODO read docs
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
    
    // TODO read docs
//    @Bean
//    public TokenEnhancer tokenEnhancer() {
//        return new TokenEnhancer() {
//            @Override
//            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//                final Map<String, Object> additionalPayload = new HashMap<>();
//                
//                additionalPayload.put("organization", authentication.getName() + randomAlphabetic(4));
//                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalPayload);
//                
//                return accessToken;
//            }
//        };
//    }

    public TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> granters = new ArrayList<>();

        granters.add(new PasswordTokenGranter(endpoints, authenticationManager));
//        granters.add(new PasswordTokenGranter(endpoints, authenticationManager, multiFactorAuthenticationService));
        granters.add(new RefreshTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
//        granters.add(new MultiFactorAuthenticationTokenGranter(endpoints, authenticationManager, multiFactorAuthenticationService));
//        granters.add(new GoogleTokenGranter(endpoints, authenticationManager, multiFactorAuthenticationService));
//        granters.add(new MicrosoftTokenGranter(endpoints, authenticationManager, multiFactorAuthenticationService));
//        granters.add(new FacebookTokenGranter(endpoints, authenticationManager, multiFactorAuthenticationService));


        return new CompositeTokenGranter(granters);
    }
    
}
