package com.elite.resort.Config;

import com.elite.resort.Model.Admin;
import com.elite.resort.Repository.AdminRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final AdminRepo adminRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (adminRepository.findByEmail("admin@resort.com").isEmpty()) {

            Admin admin = new Admin();
            admin.setName("Resort Owner");
            admin.setEmail("admin@resort.com");
            admin.setPassword(passwordEncoder.encode("admin123"));

            adminRepository.save(admin);

            System.out.println("Default Admin Created!");
        }
    }
}
