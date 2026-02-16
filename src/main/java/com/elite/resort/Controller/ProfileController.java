package com.elite.resort.Controller;


import com.elite.resort.DTO.ProfileRequest;
import com.elite.resort.Model.Profile;
import com.elite.resort.Security.JwtUtil;
import com.elite.resort.Services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import jakarta.servlet.http.HttpServletRequest;

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

        String userId = jwtUtil.extractId(token);
        String name = jwtUtil.extractName(token);
        String email = jwtUtil.extractEmail(token);

        Profile profile = profileService.createOrUpdateProfile(userId, name, email, request);

        return ResponseEntity.ok(profile);
    }

    // Get My Profile
    @GetMapping("/me")
    public ResponseEntity<Profile> getMyProfile(HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractId(token);

        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }
}

