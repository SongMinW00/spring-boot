package com.example.demo.domain.entity.answer;

import com.example.demo.domain.entity.basetime.BaseTimeEntity;
import com.example.demo.domain.entity.question.Question;
import com.example.demo.domain.entity.user.Member;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    @ManyToOne
    private Member author;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Member> voter;
    public Answer(Long id, String body, Question question, Member author, Set<Member> voter){
        this.id = id;
        this.body = body;
        this.question = question;
        this.author = author;
        this.voter = voter;
    }

    public void update(String body) {
        this.body = body;
    }
}
