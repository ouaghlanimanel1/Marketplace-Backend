package com.marketplace.userservice.controller;

import com.marketplace.userservice.interfaces.AuthService;
import com.marketplace.userservice.model.User;
import com.marketplace.userservice.model.dto.LoginDto;
import com.marketplace.userservice.model.dto.RegisterDto;
import com.marketplace.userservice.model.dto.UserDto;
import com.marketplace.userservice.model.response.JwtAuthResponse;
import com.marketplace.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) throws Exception {
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        UserDto userDto = authService.login(loginDto);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setUser(userDto);
        jwtAuthResponse.setAccessToken(userDto.getAccessToken());
        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

}
