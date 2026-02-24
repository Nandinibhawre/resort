package com.elite.resort.Services;

import com.elite.resort.DTO.UserResponse;
import com.elite.resort.Model.User;
import com.elite.resort.Repository.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepo userRepository;

    // ✅ Get all users (without password)
    public List<UserResponse> getAllUsers(){

        return userRepository.findAll()
                .stream()
                .map(u -> new UserResponse(
                        u.getUserId(),
                        u.getName(),
                        u.getEmail()))
                .toList();
    }

    // ✅ Delete user by id
    public String deleteUser(String userId){

        if(!userRepository.existsById(userId)){
            return "User not found";
        }

        userRepository.deleteById(userId);
        return "User deleted successfully";
    }
}