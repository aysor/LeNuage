package ru.netology.cloudservice.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.cloudservice.model.entity.FileEntity;
import ru.netology.cloudservice.model.entity.UserEntity;
import ru.netology.cloudservice.repository.AuthRepository;
import ru.netology.cloudservice.repository.FileStorageRepository;
import ru.netology.cloudservice.utils.ConstEntity;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileStorageRepositoryIT {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private FileStorageRepository fileRepository;

    @Autowired
    private AuthRepository authRepository;

    private long filedId;
    private UserEntity user;

    @BeforeAll
    @Transactional
    void setUp() {
        user = authRepository.save(ConstEntity.USER_333);
        filedId = fileRepository.save(ConstEntity.FILE_333).getId();
    }

    @AfterAll
    @Transactional
    void tearDown() {
        fileRepository.delete(ConstEntity.FILE_333);
        authRepository.delete(ConstEntity.USER_333);
    }

    @Test
    void findByFileNameAndUser() {
        FileEntity fileFound = fileRepository.findFileEntityByUser_NameAndFileName(ConstEntity.USER_333.getName(), ConstEntity.FILE_333.getFileName());
        assertThat(fileFound).isEqualTo(ConstEntity.FILE_333);
    }
}
