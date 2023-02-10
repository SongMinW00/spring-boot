package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class BoardController {
    @GetMapping("/user/board_write")
    public String boardWrite(){
        return "content/user/board_write";
    }

}
