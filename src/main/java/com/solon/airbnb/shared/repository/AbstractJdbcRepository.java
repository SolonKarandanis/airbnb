package com.solon.airbnb.shared.repository;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractJdbcRepository {

    private static final Logger log = LoggerFactory.getLogger(AbstractJdbcRepository.class);

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected JdbcClient getJdbcClient() {
        return jdbcClient;
    }

    protected JdbcTemplate getJdbcTemplate(){return jdbcTemplate;}

    protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){return namedParameterJdbcTemplate;}

    @PostConstruct
    void init() {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
    }

}
