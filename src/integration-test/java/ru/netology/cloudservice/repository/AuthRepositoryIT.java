package ru.netology.cloudservice.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.cloudservice.model.entity.UserEntity;
import ru.netology.cloudservice.utils.ConstEntity;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthRepositoryIT {
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    @Transactional
    void setUp() {
        authRepository.save(ConstEntity.USER_222);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        authRepository.delete(ConstEntity.USER_222);
    }

    @Autowired
    private AuthRepository authRepository;

    @Test
    void findUserByUsername() {
        UserEntity actualUserEntity = authRepository.findAllByName(ConstEntity.USER_222.getName())
                .orElse(new UserEntity());
        assertThat(actualUserEntity.getName()).isEqualTo(ConstEntity.USER_222.getName());
        assertThat(actualUserEntity.getPassword()).isEqualTo(ConstEntity.USER_222.getPassword());
    }
}
