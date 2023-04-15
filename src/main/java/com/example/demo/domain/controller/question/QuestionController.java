package com.example.demo.domain.controller.question;

import com.example.demo.domain.dao.question.QuestionRepository;
import com.example.demo.domain.dto.request.AnswerDTO;
import com.example.demo.domain.dto.request.QuestionDTO;
import com.example.demo.domain.dto.request.SignUpRequestDTO;
import com.example.demo.domain.entity.answer.Answer;
import com.example.demo.domain.entity.question.Question;
import com.example.demo.domain.entity.user.Member;
import com.example.demo.domain.service.question.QuestionService;
import com.example.demo.domain.service.user.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

    private final UserServiceImpl userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
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
    public String questionCreate(@Valid QuestionDTO questionDTO, BindingResult bindingResult, Principal principal, SignUpRequestDTO signUpRequestDTO) {
        if (bindingResult.hasErrors()) {
            return "content/question/question_form";
        }
        Member member = this.userService.getMember(principal.getName(), signUpRequestDTO.getEmail());
        /* 질문을 저장한다. */
        this.questionService.create(questionDTO.getTitle(), questionDTO.getBody(), member);

        return "redirect:/question/list"; // 질문 저장후 질문 목록으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionDTO questionDTO) {
        return "content/question/question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionDTO questionDTO, @PathVariable("id") Long id, Principal principal, HttpServletResponse response) throws IOException {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('수정권한이 없습니다.');location.replace('/question/list');</script>");
            out.flush();
        }
        questionDTO.setTitle(question.getTitle());
        questionDTO.setBody(question.getBody());
        return "content/question/question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")    // {id} 주소로 파라미터를 전송받을수 있음
    public String questionModify(@Valid QuestionDTO questionDTO, BindingResult bindingResult, HttpServletResponse response,
                                 Principal principal, @PathVariable("id") Long id) throws IOException {
        if (bindingResult.hasErrors()) {
            return "content/question/question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('수정권한이 없습니다.');location.replace('/question/list');</script>");
            out.flush();
            return "redirect:/user/mypage";
        }
        question.update(questionDTO.getTitle(), questionDTO.getBody());
        questionService.update(question);
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Long id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Long id, SignUpRequestDTO signUpRequestDTO) {
        Question question = this.questionService.getQuestion(id);
        Member member = this.userService.getMember(principal.getName(), signUpRequestDTO.getEmail());
        this.questionService.vote(question, member);
        return String.format("redirect:/question/detail/%s", id);
    }
}
