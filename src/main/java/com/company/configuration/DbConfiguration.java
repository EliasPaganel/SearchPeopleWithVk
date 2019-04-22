package com.company.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DbConfiguration {

    @Value("${dbHost}")
    private String dbHost;

    @Value("${dbUser}")
    private String dbUser;

    @Value("${dbPassword}")
    private String dbPassword;

    @Value("${databaseName}")
    private String databaseName;

    @Value("${dbPortNumber}")
    private String dbPortNumber;

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource()));
    }

    @Bean
    public DSLContext dslContext() {
        return DSL.using(connectionProvider(), SQLDialect.POSTGRES_9_5);
    }

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() {
        final HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setPoolName("SearchPeople");
        hikariDataSource.setMinimumIdle(0);
        hikariDataSource.setMaximumPoolSize(50);
        hikariDataSource.setIdleTimeout(300000);
        hikariDataSource.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");

        Properties properties = new Properties();
        properties.put("user", dbUser);
        properties.put("password", dbPassword);
        properties.put("databaseName", databaseName);
        properties.put("serverName", dbHost);
        properties.put("portNumber", dbPortNumber);
        hikariDataSource.setDataSourceProperties(properties);

        return hikariDataSource;
    }

    @Bean
    public org.jooq.Configuration configuration() {
        return new DefaultConfiguration().set(connectionProvider()).set(SQLDialect.POSTGRES_9_5);
    }
}

