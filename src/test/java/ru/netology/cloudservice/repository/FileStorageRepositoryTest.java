package ru.netology.cloudservice.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.netology.cloudservice.model.entity.FileEntity;
import ru.netology.cloudservice.utils.ConstEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileStorageRepositoryTest {
    @Autowired
    private FileStorageRepository fileRepository;

    @Autowired
    private AuthRepository authRepository;

    @BeforeAll
    void setUp() {
        fileRepository.save(ConstEntity.FILE);
        authRepository.save(ConstEntity.USER);
    }

    @Test
    void findByFileNameAndUser() {
        FileEntity fileFound = fileRepository.findFileEntityByUser_NameAndFileName(ConstEntity.USER.getName(), ConstEntity.FILE.getFileName());
        assertThat(fileFound).isEqualTo(ConstEntity.FILE);
    }
}
