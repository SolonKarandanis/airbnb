package com.solon.airbnb.user.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthorityRepositoryTest {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    JdbcConnectionDetails jdbcConnectionDetails;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:14.15")
            .withDatabaseName("airbnb")
            .withUsername("airbnb")
            .withPassword("airbnb");


    @BeforeAll
    public static void setUp() {
        postgresqlContainer.setWaitStrategy(
                new LogMessageWaitStrategy()
                        .withRegEx(".*database system is ready to accept connections.*\\s")
                        .withTimes(1)
                        .withStartupTimeout(Duration.of(60, ChronoUnit.SECONDS))
        );
        postgresqlContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgresqlContainer.stop();
    }

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgresqlContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgresqlContainer::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.driver-class-name", postgresqlContainer::getDriverClassName);
    }

//    @BeforeEach
//    void setUp() {
//        List<Post> posts = List.of(new Post(1,1,"Hello, World!", "This is my first post!",null));
//        postRepository.saveAll(posts);
//    }

    @Test
    void connectionEstablished() {
        assertThat(postgresqlContainer.isCreated()).isTrue();
        assertThat(postgresqlContainer.isRunning()).isTrue();
    }

//    @Test
//    void shouldReturnPostByTitle() {
//        Post post = postRepository.findByTitle("Hello, World!").orElseThrow();
//        assertEquals("Hello, World!", post.title(), "Post title should be 'Hello, World!'");
//    }
//
//    @Test
//    void shouldNotReturnPostWhenTitleIsNotFound() {
//        Optional<Post> post = postRepository.findByTitle("Hello, Wrong Title!");
//        assertFalse(post.isPresent(), "Post should not be present");
//    }
}
