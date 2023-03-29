package com.example.demo.domain.dao.question;

import com.example.demo.domain.entity.answer.Answer;
import com.example.demo.domain.entity.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
