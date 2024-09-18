/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solon.airbnb.infrastructure.config.persistence;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;

import com.zaxxer.hikari.HikariDataSource;

/**
 *
 * @author solon
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
        		"com.solon.airbnb.booking.repository",
        		"com.solon.airbnb.listing.repository",
        		"com.solon.airbnb.user.repository"
        },
        entityManagerFactoryRef = "airbnbManagerFactory",
        transactionManagerRef = "airbnbTransactionManager"
)
public class DatasourceConfiguration {
    
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dutDatasourceProperties(){
        return new DataSourceProperties();
    }
    
    @Bean(name="dut")
    @Primary
    //spring.datasource.configuration
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dutDatasource(){
        HikariDataSource datasource = dutDatasourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
        
        datasource.setAutoCommit(false); //Default
        datasource.addDataSourceProperty("rewriteBatchedStatements", true);
        datasource.addDataSourceProperty("dataSource.cachePrepStmts", "true");
        datasource.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
        datasource.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        datasource.addDataSourceProperty("dataSource.useServerPrepStmts", "true");
        return datasource;
    }
    
    @Primary
    @Bean(name = "airbnbManagerFactory")
    public LocalContainerEntityManagerFactoryBean dutManagerFactory(EntityManagerFactoryBuilder builder){
        return builder
                .dataSource(dutDatasource())
                .packages("com.solon.dut.entity")
                .build();
    }
    
    @Primary
    @Bean
    public PlatformTransactionManager dutTransactionManager(
            final @Qualifier("airbnbTransactionManager") LocalContainerEntityManagerFactoryBean dutManagerFactory
    ){
    	Assert.notNull(dutManagerFactory,"The class must not be null");
        return new JpaTransactionManager(dutManagerFactory.getObject());
    }
}
