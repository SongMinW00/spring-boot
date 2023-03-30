package com.example.demo.domain.service.answer;

import com.example.demo.domain.dao.answer.AnswerRepository;
import com.example.demo.domain.entity.answer.Answer;
import com.example.demo.domain.entity.question.Question;
import com.example.demo.domain.entity.user.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class AnswerService {
    private final AnswerRepository answerRepository;


    public void create(Question question, String content, Member author){
        Answer answer;
        answer = Answer.builder()
                .body(content)
                .question(question)
                .author(author)
        .build();
        this.answerRepository.save(answer);
    }
}
