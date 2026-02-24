package com.elite.resort.Controller;

import com.elite.resort.DTO.UserResponse;
import com.elite.resort.Model.User;
import com.elite.resort.Repository.UserRepo;
import com.elite.resort.Services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminController
{

    private final AdminService adminService;

    // ✅ View users (only name + email)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<UserResponse> getUsers(){
        return adminService.getAllUsers();
    }

    // ✅ Delete user
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public String deleteUser(@PathVariable String userId){
        return adminService.deleteUser(userId);
    }
}