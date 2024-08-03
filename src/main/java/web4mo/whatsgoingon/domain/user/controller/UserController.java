package web4mo.whatsgoingon.domain.user.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web4mo.whatsgoingon.domain.user.dto.LogInRequestDto;
import web4mo.whatsgoingon.domain.user.dto.SignUpRequestDto;
import web4mo.whatsgoingon.domain.user.dto.TokenDto;
import web4mo.whatsgoingon.domain.user.entity.Member;
import web4mo.whatsgoingon.domain.user.service.UserService;
import web4mo.whatsgoingon.response.Response;
import static web4mo.whatsgoingon.response.Message.*;

import static web4mo.whatsgoingon.response.Response.success;

@Tag(name = "UserController", description = "사용자 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseBody
public class UserController {

    private final UserService userService;


    @GetMapping("/home")
    public String home(){
        return "Home";
    }

    //회원가입
    @PostMapping("/auth/signup")
    public Response signup(@RequestBody SignUpRequestDto userFormDTO){
        Member member=userService.signup(userFormDTO);
        //Member member=userService.getCurrentMember();
       return success(SIGN_UP,member);
    }

    //로그인
    @PostMapping("/auth/login")
    public Response login(@RequestBody LogInRequestDto logInRequestDto){
        TokenDto jwtToken = userService.login(logInRequestDto);
        return success(LOG_IN,jwtToken);
    }

    //로그아웃
    @PostMapping("/logout")
    public Response logout(@RequestHeader("Authorization") String token){
        userService.logout(token);
        return success(LOG_OUT);
    }

    //엑세스 토큰 재발급
    @PatchMapping("/reissue")
    public Response reIssue(@RequestBody TokenDto tokenDto){
        userService.tokenReissue(tokenDto);
        return success(REISSUE);
    }



}
