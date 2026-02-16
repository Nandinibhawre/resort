package com.elite.resort.Services;

import com.elite.resort.DTO.LoginRequest;
import com.elite.resort.DTO.LoginResponse;
import com.elite.resort.DTO.UserRegisterRequest;
import com.elite.resort.Model.Admin;
import com.elite.resort.Model.User;
import com.elite.resort.Repository.AdminRepo;
import com.elite.resort.Repository.UserRepo;
import com.elite.resort.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService
{

    private final UserRepo userRepository;
    private final AdminRepo adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private  final EmailService emailService;


    // USER REGISTER
    public String registerUser(UserRegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);


        // ✅ SEND EMAIL AFTER REGISTER
        emailService.sendAccountCreatedEmail(
                user.getEmail(),
                user.getName()
        );
        return "User Registered Successfully";
    }

    public LoginResponse userLogin(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String role = "USER";

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getName(),
                user.getUserId(),
                role

        );

        return new LoginResponse(
                token,

                user.getName(),
                user.getEmail(),
                role
        );
    }
    public String adminLogin(LoginRequest request) {

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return "token:" + jwtUtil.generateToken(
                admin.getEmail(),
                admin.getName(),
                admin.getAdminId(),   // id second
                "ADMIN"               // role third
        );
    }
}
