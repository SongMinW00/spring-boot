package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@RequiredArgsConstructor
@Controller
public class SportsController {
    @GetMapping("/sports/sports_detail")
    public String sportsDetail(){
        return "content/sports/sports_detail";
    }

    @GetMapping("/sports/sports_info")
    public String sportsInfo(){
        return "content/sports/sports_info";
    }
    @GetMapping("/sports/sports_reco")
    public String sportsRecommend(){
        return "content/sports/sports_reco";
    }
    @GetMapping("/sports/sports_recoresult")
    public String sportsRecommendResult(){
        return "content/sports/sports_recoresult";
    }
    @GetMapping("/sports/sports_search")
    public String sportsSearch(){
        return "content/sports/sports_search";
    }
}
