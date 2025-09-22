package ru.netology.cloudservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EditFileRequest {
    @JsonProperty("filename")
    private String newName;
}
