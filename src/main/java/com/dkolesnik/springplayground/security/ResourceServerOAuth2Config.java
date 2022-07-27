package com.dkolesnik.springplayground.security;

import com.dkolesnik.springplayground.http.CORSFilter;
import com.dkolesnik.springplayground.http.DownloadAuthorizationFilter;
import com.dkolesnik.springplayground.http.JsonToUrlEncodedAuthenticationFilter;
import com.dkolesnik.springplayground.model.jpa.domain.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * oAuth2 Authorization Server Implementation
 *
 * @author   Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version  0.1.1
 * @since    2022-07-26
 */
@Configuration
@EnableResourceServer
@Order(103)
public class ResourceServerOAuth2Config extends ResourceServerConfigurerAdapter {

    @Autowired
    private JsonToUrlEncodedAuthenticationFilter jsonFilter;
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        
        // Applying filters
        http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class);
        http.addFilterBefore(new DownloadAuthorizationFilter(), AbstractPreAuthenticatedProcessingFilter.class);
        http.addFilterAfter(jsonFilter, BasicAuthenticationFilter.class);
        
        // Public endpoints
        http.authorizeRequests()
            .antMatchers("/api/info/**", "/api/signup/**", "/api/public/**")
            .permitAll();
        
        // Admin only endpoints
        http.authorizeRequests()
            .antMatchers("/api/admin/**")
            .hasAnyAuthority(RoleType.ADMIN.toString());
        
        // Authorized endpoints
        http.authorizeRequests()
            .antMatchers("/api/users/logout", "/api/users/self", "/api/users/filter")
            .authenticated();
        
        http.requestMatchers()
                .antMatchers("/api/**")
            .and()
            .authorizeRequests()
                .antMatchers("/api/**")
                .authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);

        // resources.authenticationEntryPoint(new AuthExceptionEntryPoint());
    }
    
}
