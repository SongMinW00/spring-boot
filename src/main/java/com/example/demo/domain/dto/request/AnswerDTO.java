package com.example.demo.domain.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AnswerDTO {
    @NotEmpty(message = "내용은 필수항목입니다.🥵")
    private String body;
}
