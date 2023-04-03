package com.example.demo.domain.dto.request;

import com.example.demo.domain.entity.answer.Answer;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    @NotEmpty(message = "제목은 필수항목입니다.🥵")
    @Size(max = 200)
    private String title;
    @NotEmpty(message = "내용은 필수항목입니다.🥵")
    private String body;
    private List<Answer> answerList;

}
