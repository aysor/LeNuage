package ru.netology.cloudservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservice.model.entity.FileEntity;
import ru.netology.cloudservice.model.entity.UserEntity;
import ru.netology.cloudservice.repository.FileStorageRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final FileStorageRepository fileStorageRepository;

    public void upload(String fileName, MultipartFile file, UserEntity user) throws IOException {
        byte[] fileContent = file.getBytes();
        FileEntity fileEntity = FileEntity.builder()
                .fileData(fileContent)
                .fileSize(file.getSize())
                .fileName(fileName)
                .user(user)
                .build();
        fileStorageRepository.save(fileEntity);
    }

    public byte[] download(String userName, String filename){
        FileEntity fileEntity = fileStorageRepository.findFileEntityByUser_NameAndFileName(userName, filename);
        return fileEntity.getFileData();
    }

    @Transactional
    public void editFileName(String userName, String filename, String newName){
        FileEntity file = fileStorageRepository.findFileEntityByUser_NameAndFileName(userName, filename);
        file.setFileName(newName);
        fileStorageRepository.save(file);
    }

    public List<FileEntity> getFiles(String userName, Integer limit){
        Pageable firstPageWithLimit = PageRequest.of(0, limit);
        Page<FileEntity> allFiles = fileStorageRepository.findAllByUser_Name(userName, firstPageWithLimit);
        return allFiles.getContent();
    }

    @Transactional
    public void delete(String userName, String filename){
        try {
            fileStorageRepository.deleteByUser_NameAndFileName(userName, filename);
        }catch(Exception ex){
            System.out.println(ex);
        }
    }
}
