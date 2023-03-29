package com.example.demo.domain.entity.question;

import com.example.demo.domain.entity.answer.Answer;
import com.example.demo.domain.entity.basetime.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Builder
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 200)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String body;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnoreProperties({"question"})
    private List<Answer> answerList;


    public Question(Long id, String title, String body, List<Answer> answerList){
        this.id = id;
        this.body = body;
        this.title = title;
        this.answerList = answerList;
    }
}
