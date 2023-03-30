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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

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
}
