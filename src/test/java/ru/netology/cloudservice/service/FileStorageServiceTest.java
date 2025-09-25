package ru.netology.cloudservice.service;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservice.model.entity.FileEntity;
import ru.netology.cloudservice.repository.AuthRepository;
import ru.netology.cloudservice.repository.FileStorageRepository;
import ru.netology.cloudservice.utils.ConstEntity;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Transactional
public class FileStorageServiceTest {
    @InjectMocks
    private FileStorageService fileService;

    @Mock
    private FileStorageRepository fileRepository;

    @Mock
    private AuthRepository authRepository;

    @BeforeEach
    void setUp() {
        when(authRepository.findAllByName(any())).thenReturn(Optional.ofNullable(ConstEntity.USER));
    }

    @SneakyThrows
    @Rollback
    @Test
    public void uploadFile() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        fileService.upload("tmp.txt", multipartFile, ConstEntity.USER);
        verify(fileRepository, times(1)).save(any(FileEntity.class));
    }

    @Rollback
    @Test
    public void deleteFile() {
        when(fileRepository.findFileEntityByUser_NameAndFileName(any(), any())).thenReturn(ConstEntity.FILE);
        fileService.delete(ConstEntity.FILE.getUser().getName(), ConstEntity.FILE.getFileName());
        String fileName = ConstEntity.FILE.getFileName();
        assertThat(fileRepository.findById(ConstEntity.FILE.getId())).isEmpty();
    }
    @Test
    public void downloadFile() {
        when(fileRepository.findFileEntityByUser_NameAndFileName(any(), any())).thenReturn(ConstEntity.FILE);
        byte[] file = fileService.download(ConstEntity.FILE.getUser().getName(), ConstEntity.FILE.getFileName());
        assertThat(file).isNotNull();
        assertThat((long) file.length).isEqualTo(ConstEntity.FILE.getFileSize());
        assertThat(file).isEqualTo(ConstEntity.FILE.getFileData());
    }

    @Test
    @Rollback
    public void editFileName() {
        String newFileName = "newFileName";
        when(fileRepository.findFileEntityByUser_NameAndFileName(any(), any())).thenReturn(ConstEntity.FILE);
        fileService.editFileName(ConstEntity.USER.getName(), ConstEntity.FILE.getFileName(), newFileName);
        verify(fileRepository, times(1)).save(any());
    }
}
