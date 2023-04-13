package com.example.demo.domain.controller.home;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequiredArgsConstructor
@Controller
public class HomeController {
    /* 메인페이지 매핑 */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "content/base/main";
    }

    /* thymeleaf 예시 페이지 */
    @GetMapping("/thymeleaf/ex")
    public String thymeleafExample() {
        return "thymeleafEx";
    }
}
