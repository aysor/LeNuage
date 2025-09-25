package ru.netology.cloudservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.netology.cloudservice.model.dto.AuthRequest;
import ru.netology.cloudservice.model.dto.AuthResponse;
import ru.netology.cloudservice.model.entity.UserEntity;
import ru.netology.cloudservice.repository.AuthRepository;
import ru.netology.cloudservice.security.JwtTokenProvider;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {
    private static String USERNAME = "user1";
    private static String PASSWORD = "123";
    private static String TOKEN = "auth-token";

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private  AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Test
    void login() {
        UserEntity user = new UserEntity(1L, USERNAME, "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
        Mockito.when(authService.getUserByName(USERNAME)).thenReturn(Optional.of(user));
        Mockito.when(tokenProvider.generateToken(user)).thenReturn(TOKEN);
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                USERNAME,
                PASSWORD
        ))).thenReturn(new UsernamePasswordAuthenticationToken(
                        USERNAME,
                        PASSWORD
        ));
        AuthRequest authRequest = new AuthRequest(USERNAME, PASSWORD);
        AuthResponse authResponse = new AuthResponse(TOKEN);
        assertEquals(authResponse, authService.login(authRequest));
        Mockito.verify(authRepository, Mockito.times(1)).findAllByName(authRequest.getLogin());
    }
}