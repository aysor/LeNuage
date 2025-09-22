package ru.netology.cloudservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthResponse {
    @JsonProperty("auth-token")
    private String token;
}
