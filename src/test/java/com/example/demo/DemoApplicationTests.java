package com.example.demo;

import com.example.demo.domain.dao.answer.AnswerRepository;
import com.example.demo.domain.dao.question.QuestionRepository;
import com.example.demo.domain.entity.answer.Answer;
import com.example.demo.domain.entity.question.Question;
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
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void testJpa() {
//        Optional<Question> oq = this.questionRepository.findById(1L);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//
//        Answer a1 = Answer.builder()
//                .body("네 자동으로 생성됩asdf니다.")
//                .question(q)
//                .build();
//        this.answerRepository.save(a1);
//

    }

}
