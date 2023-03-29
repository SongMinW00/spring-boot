package com.example.demo.domain.dto.request;

import com.example.demo.domain.entity.answer.Answer;
import jakarta.transaction.Transactional;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String body;

    private List<Answer> answerList;

}
