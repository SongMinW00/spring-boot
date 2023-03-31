package com.example.demo.domain.service.question;

import com.example.demo.domain.dao.question.QuestionRepository;
import com.example.demo.domain.dto.request.QuestionDTO;
import com.example.demo.domain.entity.answer.Answer;
import com.example.demo.domain.entity.question.Question;
import com.example.demo.domain.entity.user.Member;
import com.example.demo.global.error.DataNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> getList(){
        return this.questionRepository.findAll();
    }


    public Question getQuestion(Long id){
        Optional<Question> question = this.questionRepository.findById(id);
        if(question.isPresent()){
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String title, String body, Member author) {
        Question q = Question.builder()
                .title(title)
                .body(body)
                .author(author)
                .build();
        this.questionRepository.save(q);
    }

    public Page<Question> getList(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAll(pageable);
    }

    public void update(Question question) {
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, Member member) {
        question.getVoter().add(member);
        this.questionRepository.save(question);
    }
}
