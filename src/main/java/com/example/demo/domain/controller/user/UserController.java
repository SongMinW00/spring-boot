package com.example.demo.domain.controller.user;

import com.example.demo.domain.dto.request.SignUpRequestDTO;
import com.example.demo.domain.entity.file.FileEntity;
import com.example.demo.domain.entity.user.Member;
import com.example.demo.domain.service.file.FileService;
import com.example.demo.domain.service.user.UserService;
import com.example.demo.global.security.service.CustomUserDetails;
import com.example.demo.global.security.validate.CheckEmailValidator;
import com.example.demo.global.security.validate.CheckUsernameValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;

@Controller // 이 클래스는 컨트롤러 기능을 수행한다고 정의
@RequiredArgsConstructor
public class UserController {
    // 보통의 경우 변수는 private 으로, 함수는 public 으로 지정
    // final 이 붙으면 변수 초기화 후 변경 할 수가 없다.
    private final UserService userService;
    private final FileService fileService;
    private final CheckUsernameValidator checkUsernameValidator;
    private final CheckEmailValidator checkEmailValidator;

    @Autowired
    private AuthenticationManager authenticationManager;

    /* 커스텀 유효성 검증을 위해 추가 */
    @InitBinder // 특정 컨트롤러에서 바인딩 또는 검증 설정을 변경하고 싶을 때 사용
    public void validatorBinder(WebDataBinder binder) {
        // HTTP 요청 정보를 컨트롤러 메소드의 파라미터나 모델에 바인딩 할 때 사용되는 바인딩 객체
        binder.addValidators(checkUsernameValidator);
        binder.addValidators(checkEmailValidator);
    }


    /* 회원가입페이지 매핑 */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(SignUpRequestDTO signUpRequestDTO) {
        return "content/base/register";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/user/updateForm", method = RequestMethod.GET)
    public String infoCheck(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, SignUpRequestDTO signUpRequestDTO) {
        Member member = this.userService.getMember(userDetails.getId());
        if (userDetails != null) {
            model.addAttribute("member", userDetails.getMember());
        }
        signUpRequestDTO.setUsername(member.getUsername());
        signUpRequestDTO.setEmail(member.getEmail());
        return "content/user/myinfo_check";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/user/updateForm")
    public String modify(@Valid SignUpRequestDTO signUpRequestDTO, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletResponse response, HttpServletRequest request) throws IOException {
        Member member = this.userService.getMember(userDetails.getId());
        member.modify(signUpRequestDTO.getPassword(), signUpRequestDTO.getEmail());
        this.userService.modify(member);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('수정되었습니다.');location.href('/login');</script>");
        out.flush();
        /** ========== 변경된 세션 등록 ========== **/
        /* 1. 새로운 UsernamePasswordAuthenticationToken 생성하여 AuthenticationManager 을 이용해 등록 */

        return "content/user/mypage";
    }

    /* 회원가입하고 회원가입 정보를 보호하기 위해 POST 매핑 */
    @PostMapping(value = "/register")
    // @Valid 를 통해 데이터가 전달되는 형식이 유효한지 검사
    public String joinUser(@Valid SignUpRequestDTO signUpRequestDTO, Errors errors, Model model, HttpServletResponse response, BindingResult bindingResult) throws IOException {
        // 만약 에러가 발생하면 회원가입 뷰 페이지에서 에러메시지 출력
        if (errors.hasErrors()) {

            /* 회원가입 실패시 입력 데이터 값을 유지 */
            model.addAttribute("signUpRequestDTO", signUpRequestDTO);

            /* 유효성 통과 못한 필드와 메시지 핸들링 */
            // Map 자료구조를 통해 키값과 에러 메시지를 응답
            // 흔히 key 와 value 가 매칭되는 것을 맵핑한다고 일컫는다.
            // * key 의 중복을 허용하지 않아야 한다.
            // Map 자료구조를 사용하는 이유: List 같은 순서보다는 정의된 이름(key)와 사응하는 데이터들을 묶기 위한 자료구조
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                // HashMap 에서 key 가 존재하는 key 필드 클래스가 있을때 까지 반복
                /* get() 함수를 통해 key 에 해당하는 value 를 가져옴 value 가 에러메시지니까 해당 오류 필드에 에러메시지 넣겠다.  */
                model.addAttribute(key, validatorResult.get(key));
            }
            return "content/base/register";
        }
        if (!signUpRequestDTO.getPassword().equals(signUpRequestDTO.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "위의 패스워드와 일치하지 않습니다. ");
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                /* 이 행위를 통해 에러메시지를 화면에 보여주니까 다시 Map 을 가져와서 에러메시지 출력 */
                model.addAttribute(key, validatorResult.get(key));
            }
            return "content/base/register";
        }
        /* 회원가입 수행 */
        userService.joinUser(signUpRequestDTO);
        /* 회원가입이 정상적으로 되면 경고창 띄우면서 로그인창으로 넘어가기 */
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('계정이 등록 되었습니다. 다시 로그인 해주세요.');</script>");
        out.flush();
        return "content/base/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model, HttpServletRequest request) {
        /* 로그인 수행시 에러메시지, 오류 예외, 보여질 모델을 매개변수로 설정 */
        model.addAttribute("request", request);
        userService.login(error, exception, model);
        return "content/base/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        userService.logout(request, response);

        return "redirect:/login";
    }

    @GetMapping("/user/like_sports")
    public String likeSports() {
        return "content/user/like_sports";
    }

    @GetMapping("/user/myinfo")
    public String myInfo() {
        return "content/user/myinfo";
    }


    @GetMapping("/user/myboard")
    public String myBoard() {
        return "content/user/myboard";
    }

    /* 유저의 마이페이지 매핑 */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/mypage")
    public String myPage(FileEntity file, Model model) {
        file = fileService.getId(13L);
        model.addAttribute("file", file);
        return "content/user/mypage";
    }

    @PostMapping("/user/mypage")
    public void uploadFile(@RequestParam("file") MultipartFile file, Principal principal, SignUpRequestDTO signUpRequestDTO, HttpServletResponse response) throws IOException {
        Member member = userService.getMember(principal.getName(), signUpRequestDTO.getEmail());
        fileService.saveFile(file, member);
        //다중파일 업로드시 사용
//      , @RequestParam("files") List<MultipartFile> files
//        for (MultipartFile multipartFile : files) {
//            fileService.saveFile(multipartFile, member);
//        }

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('파일이 업로드 되었습니다.');location.replace('/user/mypage');</script>");
        out.flush();
    }

    @GetMapping("/view")
    public String view(Model model) {

        List<FileEntity> files = fileService.findAll();
        model.addAttribute("all",files);
        return "view";
    }


    //   이미지 출력
    @GetMapping("/fileupload/{savedNm}")
    @ResponseBody
    public Resource downloadImage(@PathVariable("savedNm") String savedNm, Model model) throws IOException{

        FileEntity file = fileService.getByFileName(savedNm);

        if(file == null) {
            throw new IOException("파일이 존재하지 않음.");
        }

        return new UrlResource("file:" + file.getSavedPath());
    }

    // 첨부 파일 다운로드
    @GetMapping("/attach/{id}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long id) throws MalformedURLException {

        FileEntity file = fileService.getId(id);

        if(file == null) {
            throw new MalformedURLException("파일이 존재하지 않음.");
        }

        UrlResource resource = new UrlResource("file:" + file.getSavedPath());

        String encodedFileName = UriUtils.encode(file.getOrgNm(), StandardCharsets.UTF_8);

        // 파일 다운로드 대화상자가 뜨도록 하는 헤더를 설정해주는 것
        // Content-Disposition 헤더에 attachment; filename="업로드 파일명" 값을 준다.
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition).body(resource);
    }



}
