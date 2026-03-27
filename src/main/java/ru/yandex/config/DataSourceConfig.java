package ru.yandex.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class DataSourceConfig {

    private final DatabaseProperties databaseProperties;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(databaseProperties.getDriver());
        ds.setUrl(databaseProperties.getUrl());
        ds.setUsername(databaseProperties.getUsername());
        ds.setPassword(databaseProperties.getPassword());
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
