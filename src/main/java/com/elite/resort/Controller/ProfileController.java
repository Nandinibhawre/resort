package com.elite.resort.Controller;


import com.elite.resort.DTO.ProfileRequest;
import com.elite.resort.Model.Profile;
import com.elite.resort.Security.JwtUtil;
import com.elite.resort.Services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private JwtUtil jwtUtil;

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

    // =========================================================
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
    public ResponseEntity<String> deleteProfile(@PathVariable String id) {

        profileService.deleteProfile(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}

