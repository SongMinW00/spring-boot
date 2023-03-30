package com.example.demo;

import com.example.demo.domain.dao.answer.AnswerRepository;
import com.example.demo.domain.dao.question.QuestionRepository;
import com.example.demo.domain.entity.answer.Answer;
import com.example.demo.domain.entity.question.Question;
import com.example.demo.domain.service.question.QuestionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private QuestionService questionService;


    @Test
    void testJpa() {
        for(int i = 1; i <=300; i++){
            String title = String.format("테스트 데이터입니다:[%03d]", i);
            String body = "내용없음";
            this.questionService.create(title, body, null);
        }


    }

}
