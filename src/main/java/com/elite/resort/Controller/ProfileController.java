package com.elite.resort.Controller;


import com.elite.resort.DTO.ProfileRequest;
import com.elite.resort.DTO.ProfileResponseDTO;
import com.elite.resort.Model.Image;
import com.elite.resort.Model.Profile;
import com.elite.resort.Model.User;
import com.elite.resort.Repository.ImageRepo;
import com.elite.resort.Repository.ProfileRepo;
import com.elite.resort.Repository.UserRepo;
import com.elite.resort.Security.JwtUtil;
import com.elite.resort.Services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {


    private final ProfileService profileService;
    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;
    private final ImageRepo  imageRepo;
    private final ProfileRepo profileRepo;
    // Create or Update Profile

    @PostMapping
    public ResponseEntity<Profile> createProfile(
            @RequestBody ProfileRequest request,
            HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization").substring(7);

        String userId = jwtUtil.extractUserId(token);
        String name = jwtUtil.extractName(token);
        String email = jwtUtil.extractEmail(token);
        System.out.println("NAME FROM TOKEN = " + name);
        System.out.println("USERID FROM TOKEN = " + userId);
        Profile profile = profileService.createOrUpdateProfile(userId, name, email, request);

        return ResponseEntity.ok(profile);
    }

    // Get My Profile
    @GetMapping("/me")
    public ResponseEntity<Profile> getMyProfile(HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }

    @GetMapping("/my-profile")
    public ResponseEntity<?> getMyProfile() {

        try {

            // ✅ Get email from SecurityContext
            String email = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Profile profile = profileRepo.findByUserId(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            Image image = imageRepo.findByUserId(user.getUserId())
                    .orElse(null);

            ProfileResponseDTO response = new ProfileResponseDTO();
            response.setPhone(profile.getPhone());
            response.setAddress(profile.getAddress());
            response.setIdProof(profile.getIdProof());
            response.setImageUrl(image.getImageUrl());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }
    // =============================0============================
    // ================= ADMIN APIs ============================
    // =========================================================

    // ✅ ADMIN → Get all user profiles
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Profile>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    // 🔍 ADMIN → Search profiles by name
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Profile>> searchProfiles(@RequestParam String name) {
        return ResponseEntity.ok(profileService.searchProfilesByName(name));
    }
    // ❌ ADMIN → Delete user profile by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProfile(@PathVariable String profileid) {

        profileService.deleteProfile(profileid);
        return ResponseEntity.ok("User deleted successfully");
    }
}

