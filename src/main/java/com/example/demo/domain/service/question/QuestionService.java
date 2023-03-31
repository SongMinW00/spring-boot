package com.example.demo.domain.service.question;

import com.example.demo.domain.dao.question.QuestionRepository;
import com.example.demo.domain.dto.request.QuestionDTO;
import com.example.demo.domain.entity.answer.Answer;
import com.example.demo.domain.entity.question.Question;
import com.example.demo.domain.entity.user.Member;
import com.example.demo.global.error.DataNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

//    private Specification<Question> search(String kw) {
//        return new Specification<Question>() {
//            @Override
//            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                query.distinct(true);
//                Join<Question, Member> u1 = q.join("author", JoinType.LEFT);
//                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
//                Join<Answer, Member> u2 = a.join("author", JoinType.LEFT);
//                return cb.or(cb.like(q.get("title"), "%" + "%"), // 제목
//                        cb.like(q.get("body"), "%" + kw + "%"), // 내용
//                        cb.like(u1.get("username"), "%" + kw + "%"), // 질문 작성자
//                        cb.like(a.get("body"), "%" + kw + "%"), // 답변 내용
//                        cb.like(u2.get("username"), "%" + kw + "%")); // 답변 작성자
//            }
//        };
//    }

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }
}
