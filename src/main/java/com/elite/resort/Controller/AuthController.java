package com.elite.resort.Controller;

import com.elite.resort.DTO.LoginRequest;
import com.elite.resort.DTO.LoginResponse;
import com.elite.resort.DTO.UserRegisterRequest;
import com.elite.resort.Model.User;
import com.elite.resort.Services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService authService;

    @PostMapping("/user/signup")
    public String userSignup(@RequestBody UserRegisterRequest request)
    {
        return authService.registerUser(request);
    }

    @PostMapping("/user/login")
    public LoginResponse
    login(@RequestBody LoginRequest request)
    {
        return authService.userLogin(request);
    }

    @PostMapping("/admin/login")
    public String adminLogin(@RequestBody LoginRequest request)
    {
        return authService.adminLogin(request);
    }
}
