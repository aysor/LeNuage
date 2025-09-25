package ru.netology.cloudservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.cloudservice.model.dto.AuthRequest;
import ru.netology.cloudservice.model.dto.AuthResponse;
import ru.netology.cloudservice.utils.ConstEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudServiceApplicationTests{
    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private static final String LOGIN_ENDPOINT = "/login";

    public static final int DB_PORT = 5432;
    public static final String DB_NAME = "cloud";
    public static final String DB_USERNAME = "netology_user";
    public static final String DB_PASSWORD = "111";
    public static final Network NETWORK = Network.newNetwork();

    @Container
    public static final PostgreSQLContainer<?> dbContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withReuse(true)
            .withExposedPorts(DB_PORT)
            .withDatabaseName(DB_NAME)
            .withUsername(DB_USERNAME)
            .withPassword(DB_PASSWORD)
            .withNetwork(NETWORK);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", dbContainer::getJdbcUrl);
        registry.add("spring.datasource.username", dbContainer::getUsername);
        registry.add("spring.datasource.password", dbContainer::getPassword);
        registry.add("spring.jpa.generate-ddl", () -> true);
    }


    @Test
    void testPostgresLoads() {
        Assertions.assertTrue(dbContainer.isRunning());
    }

    @Test
    void login() {
        String baseUrl = "http://localhost:" + port + LOGIN_ENDPOINT;
        AuthRequest request = new AuthRequest(ConstEntity.USER.getName(), "123");
        ResponseEntity<?> response = restTemplate.exchange(
                baseUrl, HttpMethod.POST, new HttpEntity<>(request), AuthResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}