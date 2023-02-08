package com.example.demo.controller;
import com.example.demo.Service.UserService;
import com.example.demo.constant.ServiceURIManagement;
import com.example.demo.domain.dto.request.SignUpRequestDTO;
import com.example.demo.security.validate.CheckEmailValidator;
import com.example.demo.security.validate.CheckUsernameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
@Controller // 이 클래스는 컨트롤러 기능을 수행한다고 정의
@RequiredArgsConstructor
public class UserController {
    // 보통의 경우 변수는 private 으로, 함수는 public 으로 지정
    // final 이 붙으면 변수 초기화 후 변경 할 수가 없다.
    private final UserService userService;
    private final CheckUsernameValidator checkUsernameValidator;
    private final CheckEmailValidator checkEmailValidator;
    /* 커스텀 유효성 검증을 위해 추가 */
    @InitBinder // 특정 컨트롤러에서 바인딩 또는 검증 설정을 변경하고 싶을 때 사용
    public void validatorBinder(WebDataBinder binder){
        // HTTP 요청 정보를 컨트롤러 메소드의 파라미터나 모델에 바인딩 할 때 사용되는 바인딩 객체
        binder.addValidators(checkUsernameValidator);
        binder.addValidators(checkEmailValidator);
    }
    /* 유저의 마이페이지 매핑 */
    @GetMapping(ServiceURIManagement.NOW_VERSION + "/user/my-page")
    public String myPage() throws Exception{
        return "/content/user/my-page";
    }
    /* 메인페이지 매핑 */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(){
        return "content/index";
    }
    /* 회원가입페이지 매핑 */
    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String register(){
        return "content/register";
    }
    /* 회원가입하고 회원가입 정보를 보호하기 위해 POST 매핑 */
    @PostMapping(value="/register")
    // @Valid 를 통해 데이터가 전달되는 형식이 유효한지 검사
    public String joinUser(@Valid SignUpRequestDTO signUpRequestDTO, Errors errors, Model model, HttpServletResponse response, BindingResult bindingResult) throws IOException {
        // 만약 에러가 발생하면 회원가입 뷰 페이지에서 에러메시지 출력
        if(errors.hasErrors()){
            /* 회원가입 실패시 입력 데이터 값을 유지 */
            model.addAttribute("signUpRequestDTO", signUpRequestDTO);

            /* 유효성 통과 못한 필드와 메시지 핸들링 */
            // Map 자료구조를 통해 키값과 에러 메시지를 응답
            // 흔히 key 와 value 가 매칭되는 것을 맵핑한다고 일컫는다.
            // * key 의 중복을 허용하지 않아야 한다.
            // Map 자료구조를 사용하는 이유: List 같은 순서보다는 정의된 이름(key)와 사응하는 데이터들을 묶기 위한 자료구조
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for(String key : validatorResult.keySet()){
                // HashMap 에서 key 가 존재하는 key 필드 클래스가 있을때 까지 반복
                /* get() 함수를 통해 key 에 해당하는 value 를 가져옴 value 가 에러메시지니까 해당 오류 필드에 에러메시지 넣겠다.  */
                model.addAttribute(key, validatorResult.get(key));
            }
            return "content/register";
        }
        if(!signUpRequestDTO.getPassword().equals(signUpRequestDTO.getPassword2())){
            bindingResult.rejectValue("password2", "passwordInCorrect", "위의 패스워드와 일치하지 않습니다. ");
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for(String key : validatorResult.keySet()){
                /* 이 행위를 통해 에러메시지를 화면에 보여주니까 다시 Map 을 가져와서 에러메시지 출력 */
                model.addAttribute(key, validatorResult.get(key));
            }
            return "content/register";
        }
        /* 회원가입 수행 */
        userService.joinUser(signUpRequestDTO);
        /* 회원가입이 정상적으로 되면 경고창 띄우면서 로그인창으로 넘어가기 */
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('계정이 등록 되었습니다. 다시 로그인 해주세요.');</script>");
        out.flush();
        return "content/login";
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model){
        /* 로그인 수행시 에러메시지, 오류 예외, 보여질 모델을 매개변수로 설정 */
        userService.login(error, exception, model);
        return "content/login";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        userService.logout(request, response);

        return "content/login";
    }
    @GetMapping("/denied")
    public String accessDenied(
            @RequestParam(value = "exception", required = false) String exception,
            Model model) {
        userService.accessDenied(exception, model);
        return "content/error/denied";
    }
//    @GetMapping("/list")
//    public String list(Model model){
//
//        List<UserVo> users = userService.findMenbers();
//        model.addAttribute("users", users);
//        return "content/list";
//    }
}
