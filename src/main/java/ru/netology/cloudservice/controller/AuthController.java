package ru.netology.cloudservice.controller;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudservice.exception.CloudServiceException;
import ru.netology.cloudservice.model.dto.AuthRequest;
import ru.netology.cloudservice.model.dto.AuthResponse;
import ru.netology.cloudservice.model.dto.ErrorResponse;
import ru.netology.cloudservice.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, ServletRequest servletRequest){
        AuthResponse result;
        try {
             result = authService.login(request);
        }catch(CloudServiceException ex){
            ErrorResponse error = new ErrorResponse("Bad credentials: " + ex);
            return ResponseEntity.status(400).body(error);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader(name = "auth-token") String tokenHeader){

    }
}
