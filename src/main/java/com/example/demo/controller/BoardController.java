package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class BoardController {
    @GetMapping("/board/board_write")
    public String boardWrite() {
        return "content/board/board_write";
    }

    @GetMapping("/board/board_result")
    public String boardResult() {
        return "content/board/board_result";
    }

    @GetMapping("/board/board")
    public String board(){
        return "content/board/board";
    }

    @GetMapping("/board/board_view")
    public String viewBoard(){
        return "content/board/board_view";
    }
}
