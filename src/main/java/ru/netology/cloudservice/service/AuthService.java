package ru.netology.cloudservice.service;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.netology.cloudservice.exception.CloudServiceException;
import ru.netology.cloudservice.model.dto.AuthRequest;
import ru.netology.cloudservice.model.dto.AuthResponse;
import ru.netology.cloudservice.model.entity.UserEntity;
import ru.netology.cloudservice.repository.AuthRepository;
import ru.netology.cloudservice.security.JwtTokenProvider;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenProvider tokenProvider;

    public AuthResponse login(AuthRequest request){
        Optional<UserEntity> user = authRepository.findAllByName(request.getLogin());
        if(user.isEmpty()){
            throw new CloudServiceException("No such user");
        }

        String sha256hex = Hashing.sha256()
                .hashString(request.getPassword(), StandardCharsets.UTF_8)
                .toString();

        if(!user.get().getPassword().equals(sha256hex)){
            throw new CloudServiceException("Password incorrect");
        }
        String token = tokenProvider.generateToken(user.get());

        AuthResponse response = new AuthResponse(token);
        return response;
    }

    public Optional<UserEntity> getUserByName(String name){
        return authRepository.findAllByName(name);
    }
}
