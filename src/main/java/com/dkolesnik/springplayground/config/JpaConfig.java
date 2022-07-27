package com.dkolesnik.springplayground.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Description
 *
 * @author Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version 0.1.1
 * @since 2022-07-25
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = {"com.dkolesnik.springplayground.repository"}
)
@EnableJpaAuditing
@Slf4j
public class JpaConfig {

    /**
     * Creating default Transaction manager
     *
     * @return
     */
    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new JpaTransactionManager();
    }
    
}
