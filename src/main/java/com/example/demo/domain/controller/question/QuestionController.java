package com.example.demo.domain.controller.question;

import com.example.demo.domain.dao.question.QuestionRepository;
import com.example.demo.domain.dto.request.AnswerDTO;
import com.example.demo.domain.dto.request.QuestionDTO;
import com.example.demo.domain.entity.answer.Answer;
import com.example.demo.domain.entity.question.Question;
import com.example.demo.domain.entity.user.Member;
import com.example.demo.domain.service.question.QuestionService;
import com.example.demo.domain.service.user.UserServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

    private final UserServiceImpl userService;
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
        return "content/question/question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, AnswerDTO answerDTO) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "content/question/question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionDTO questionDTO, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()){
            return "content/question/question_form";
        }
        Member member = this.userService.getMember(principal.getName());
        /* 질문을 저장한다. */
        this.questionService.create(questionDTO.getTitle(), questionDTO.getBody(), member);
        return "redirect:/question/list"; // 질문 저장후 질문 목록으로 이동
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionDTO questionDTO) {
        return "content/question/question_form";
    }
}
