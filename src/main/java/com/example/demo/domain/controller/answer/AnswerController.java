package com.example.demo.domain.controller.answer;

import com.example.demo.domain.dto.request.AnswerDTO;
import com.example.demo.domain.dto.request.QuestionDTO;
import com.example.demo.domain.entity.answer.Answer;
import com.example.demo.domain.entity.question.Question;
import com.example.demo.domain.entity.user.Member;
import com.example.demo.domain.service.answer.AnswerService;
import com.example.demo.domain.service.question.QuestionService;
import com.example.demo.domain.service.user.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserServiceImpl userService;
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Long id, @Valid AnswerDTO answerDTO, BindingResult bindingResult, Principal principal){
        Question question = this.questionService.getQuestion(id);
        Member member = this.userService.getMember(principal.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("question", question);
            return "content/question/question_detail";
        }
        // 답변을 저장
        this.answerService.create(question, answerDTO.getBody(), member);
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerDTO answerDTO, @PathVariable("id") Long id, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerDTO.setBody(answer.getBody());
        return "content/answer/answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerDTO answerDTO, BindingResult bindingResult,
                               @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "content/answer/answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answer.update(answerDTO.getBody());
        this.answerService.update(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Long id) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Long id) {
        Answer answer = this.answerService.getAnswer(id);
        Member member = this.userService.getMember(principal.getName());
        this.answerService.vote(answer, member);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
}
