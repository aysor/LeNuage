package ru.netology.cloudservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudservice.exception.CloudServiceException;
import ru.netology.cloudservice.model.dto.EditFileRequest;
import ru.netology.cloudservice.model.dto.ErrorResponse;
import ru.netology.cloudservice.model.entity.FileEntity;
import ru.netology.cloudservice.model.entity.UserEntity;
import ru.netology.cloudservice.security.JwtTokenProvider;
import ru.netology.cloudservice.service.AuthService;
import ru.netology.cloudservice.service.FileStorageService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileStorageController {
    private final FileStorageService fileStorageService;
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestHeader(name = "auth-token") String tokenHeader,
                                    @RequestParam("filename") String fileName,
                                    MultipartFile file) {
        UserEntity user;
        try {
            user = getUserFromToken(tokenHeader);
        } catch (Exception ex) {
            ErrorResponse error = new ErrorResponse("Unauthorized error");
            return ResponseEntity.status(401).body(error);
        }
        if (file.isEmpty()) {
            ErrorResponse error = new ErrorResponse("Error input data : Please select a file to upload.");
            return ResponseEntity.status(400).body(error);
        }

        try {
            fileStorageService.upload(fileName, file, user);
            log.info("File was successfully uploaded!!!");
            return ResponseEntity.ok("Success upload");

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Failed to upload file: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/file")
    public ResponseEntity<?> download(@RequestHeader(name = "auth-token") String tokenHeader,
                                      @RequestParam String filename) {
        UserEntity user;
        try {
            user = getUserFromToken(tokenHeader);
        } catch (CloudServiceException ex) {
            ErrorResponse error = new ErrorResponse("Unauthorized error");
            return ResponseEntity.status(401).body(error);
        }
        byte[] data;
        try {
            data = fileStorageService.download(user.getName(), filename);
        } catch (Exception ex) {
            ErrorResponse error = new ErrorResponse("Error upload file");
            return ResponseEntity.status(500).body(error);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> delete(@RequestHeader(name = "auth-token") String tokenHeader,
                                    @RequestParam String filename) {
        UserEntity user;
        try {
            user = getUserFromToken(tokenHeader);
        } catch (CloudServiceException ex) {
            ErrorResponse error = new ErrorResponse("Unauthorized error");
            return ResponseEntity.status(401).body(error);
        }
        try {
            fileStorageService.delete(user.getName(), filename);
        } catch (Exception ex) {
            ErrorResponse error = new ErrorResponse("Error delet file");
            return ResponseEntity.status(500).body(error);
        }
        return ResponseEntity.ok("Success deleted");
    }

    @PutMapping("/file")
    public ResponseEntity<?> editFileName(@RequestHeader(name = "auth-token") String tokenHeader,
                                          @RequestParam String filename,
                                          @RequestBody EditFileRequest editFileRequest) {
        UserEntity user;
        try {
            user = getUserFromToken(tokenHeader);
        } catch (CloudServiceException ex) {
            ErrorResponse error = new ErrorResponse("Unauthorized error");
            return ResponseEntity.status(401).body(error);
        }
        try {
            fileStorageService.editFileName(user.getName(), filename, editFileRequest.getNewName());
        } catch (Exception ex) {
            ErrorResponse error = new ErrorResponse("Error upload file");
            return ResponseEntity.status(500).body(error);
        }
        return ResponseEntity.ok("Success upload");
    }

    @GetMapping("/list")
    public ResponseEntity<?> getFiles(@RequestHeader(name = "auth-token") String tokenHeader,
                                      @RequestParam("limit") Integer limit) {
        UserEntity user;
        try {
            user = getUserFromToken(tokenHeader);
        } catch (CloudServiceException ex) {
            ErrorResponse error = new ErrorResponse("Unauthorized error");
            return ResponseEntity.status(401).body(error);
        }
        List<FileEntity> files;
        try {
             files = fileStorageService.getFiles(user.getName(), limit);
        } catch (Exception ex) {
            ErrorResponse error = new ErrorResponse("Error getting file list");
            return ResponseEntity.status(500).body(error);
        }
        return ResponseEntity.ok(files);
    }

    private UserEntity getUserFromToken(String tokenHeader) {
        String token = tokenProvider.resolveToken(tokenHeader);
        String userName = tokenProvider.getUsernameFromToken(token);
        Optional<UserEntity> user = authService.getUserByName(userName);
        if (user.isEmpty()) {
           throw new CloudServiceException("No such user");
        }
        return user.get();
    }
}
