package com.example.demo.domain.entity.answer;

import com.example.demo.domain.entity.basetime.BaseTimeEntity;
import com.example.demo.domain.entity.question.Question;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@Entity
@Transactional
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    private Question question;
    public Answer(Long id, String body, Question question){
        this.id = id;
        this.body = body;
        this.question = question;
    }
}
