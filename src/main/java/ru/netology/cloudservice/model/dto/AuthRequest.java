package ru.netology.cloudservice.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthRequest {
    private String login;
    private String password;
}
