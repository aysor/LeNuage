package ru.netology.cloudservice.model.dto;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private static int nextId = 0;

    private String message;

    private int id;

    private String email;

    private String password;

    public ErrorResponse(String message){
        this.id = nextId++;
        this.message = message;
        this.email = "";    // There were no such fields
        this.password = ""; //      in the yaml specification!
    }
}
