package com.dkolesnik.springplayground.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Description
 *
 * @author Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version 0.1.1
 * @since 2022-07-26
 */
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService, ApplicationContextAware {
    
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
    
    public CustomUserDetailsService() {
        log.info("#### CustomUserDetailsService. Initialize.");
    }

    /**
     * Load User Details by its Primary username
     *
     * @param userName
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // We must recreate User Properly. Like it is done in Spring.
        CustomJdbcUserDetailsManager userDetailsManager = context.getBean("customJdbcUserDetailsManager", CustomJdbcUserDetailsManager.class);
        UserDetails userDetails = userDetailsManager.loadUserByUsername(userName);

        return userDetails;
    }
}
