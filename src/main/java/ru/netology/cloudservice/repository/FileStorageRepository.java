package ru.netology.cloudservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudservice.model.entity.FileEntity;

import java.util.List;

@Repository
public interface FileStorageRepository extends JpaRepository<FileEntity, Long> {
    void deleteByUser_NameAndFileName(String userName, String filename);
    FileEntity findFileEntityByUser_NameAndFileName(String userName, String filename);
    Page<FileEntity> findAllByUser_Name(String userName, Pageable pageable);
}
