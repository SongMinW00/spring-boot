package com.example.demo.domain.dao.question;

import com.example.demo.domain.entity.answer.Answer;
import com.example.demo.domain.entity.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findByTitle(String title);
    Question findByTitleAndBody(String title, String body);
    List<Question> findByTitleLike(String title);
    Page<Question> findAll(Pageable pageable);
    @Query("select distinct q from Question q left outer join Member u1 on q.author=u1 left outer join Answer a on a.question=q left outer join Member u2 on a.author=u2 where q.title like %:kw% or q.body like %:kw% or u1.username like %:kw% or a.body like %:kw% or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
