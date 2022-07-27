package com.dkolesnik.springplayground.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

/**
 * Description
 *
 * @author Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version 0.1.1
 * @since 2022-07-25
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CustomJdbcUserDetailsManager customJdbcUserDetailsManager;
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        // Disable Sessions for API Application
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).sessionFixation().none();


        http
            .antMatcher("/**")
            .authorizeRequests()
            .antMatchers(
                "/",
                "/login**",
                "/swagger**",
                "/swagger/**",
                "/webjars/springfox-swagger-ui/**",
                "/swagger-resources/**",
                "/csrf",
                "/oauth/**"
            )
            .permitAll();

        // Allow actuator enpoints. eg: /health, /info etc.
        http.authorizeRequests().antMatchers("/actuator/**", "/api/info/**", "/api/anonymous/**").permitAll();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Set Authorization with Database Users with BCrypt password encoder.
        auth.apply(new JdbcUserDetailsManagerConfigurer<>(jdbcUserDetailsManager())).passwordEncoder(passwordEncoder);
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() {
        return customJdbcUserDetailsManager;
    }

    @Bean
    // NOTE possibly there could be token duplications (based of past experience)
    public TokenStore tokenStore() { 
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
    
}
