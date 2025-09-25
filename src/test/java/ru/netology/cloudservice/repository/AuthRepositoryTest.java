package ru.netology.cloudservice.repository;

import org.hibernate.cache.spi.support.CollectionNonStrictReadWriteAccess;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.netology.cloudservice.model.entity.UserEntity;
import ru.netology.cloudservice.utils.ConstEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthRepositoryTest {
    @BeforeEach
    void setUp() {
        authRepository.save(ConstEntity.USER);
    }

    @AfterEach
    void tearDown() {
        authRepository.delete(ConstEntity.USER);
    }

    @Autowired
    private AuthRepository authRepository;

    @Test
    void findUserByUsername() {
        UserEntity actualUserEntity = authRepository.findAllByName(ConstEntity.USER.getName())
                .orElse(new UserEntity());
        assertThat(actualUserEntity.getName()).isEqualTo(ConstEntity.USER.getName());
        assertThat(actualUserEntity.getPassword()).isEqualTo(ConstEntity.USER.getPassword());
    }
}
