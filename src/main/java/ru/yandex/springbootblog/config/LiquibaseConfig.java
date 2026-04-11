package ru.yandex.springbootblog.config;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LiquibaseConfig {

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase lq = new SpringLiquibase();
        lq.setDataSource(dataSource);
        lq.setChangeLog("classpath:db/changelog/master.xml");
        return lq;
    }
}
