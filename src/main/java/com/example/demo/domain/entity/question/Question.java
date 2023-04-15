package com.example.demo.domain.entity.question;

import com.example.demo.domain.entity.answer.Answer;
import com.example.demo.domain.entity.basetime.BaseTimeEntity;
import com.example.demo.domain.entity.user.Member;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Set;

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

    @ManyToOne
    private Member author;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Member> voter;
    public Question(Long id, String title, String body, List<Answer> answerList, Member author, Set<Member> voter){
        this.id = id;
        this.body = body;
        this.title = title;
        this.answerList = answerList;
        this.author = author;
        this.voter = voter;
    }
    public void update(String title, String body){
        this.title = title;
        this.body = body;
    }
}
