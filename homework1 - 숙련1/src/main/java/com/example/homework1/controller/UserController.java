package com.example.homework1.controller;

import com.example.homework1.dto.LoginRequestDto;
import com.example.homework1.dto.SignupRequestDto;
import com.example.homework1.jwt.JwtUtil;
import com.example.homework1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller // Controller -> MVC(Model View Controller)
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    @ResponseBody //view를 찾지 않고 리턴
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Validated SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/login")
    // HttpServletResponse 사용이유는 Header 넣기 위해
    // 필요한 것만 주고, 요청도 필요한 것만 전달 받아야 결합도가 낮아질 수 있다.
    // 요청, 응답에 대한 처리는 여기서만
    public String login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String generatedToken = userService.login(loginRequestDto);
        // 헤더에 등록
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, generatedToken);
        return "로그인";
    }
}