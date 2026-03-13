package com.elite.resort.Controller;

import com.elite.resort.DTO.ProfileRequest;
import com.elite.resort.DTO.ProfileResponseDTO;
import com.elite.resort.Model.Profile;
import com.elite.resort.Security.JwtUtil;
import com.elite.resort.Services.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final JwtUtil jwtUtil;


    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProfileResponseDTO> createProfile(

            @RequestPart("data") String data,
            @RequestPart(value = "idProofImage", required = false) MultipartFile idProofImage,
            HttpServletRequest httpRequest
    ) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ProfileRequest request = mapper.readValue(data, ProfileRequest.class);

        String token = httpRequest.getHeader("Authorization").substring(7);

        String userId = jwtUtil.extractUserId(token);
        String name = jwtUtil.extractName(token);
        String email = jwtUtil.extractEmail(token);

        ProfileResponseDTO profile = profileService.createOrUpdateProfile(
                userId,
                name,
                email,
                request,
                idProofImage
        );

        return ResponseEntity.ok(profile);
    }

    // ⭐ Get My Profile
    @GetMapping("/me")
    public ResponseEntity<Profile> getMyProfile(HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("Authorization").substring(7);
        String userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }


    // ================= ADMIN APIs =================

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Profile>> getAllProfiles() {

        return ResponseEntity.ok(profileService.getAllProfiles());
    }


    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Profile>> searchProfiles(@RequestParam String name) {

        return ResponseEntity.ok(profileService.searchProfilesByName(name));
    }


    @DeleteMapping("/{profileid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProfile(@PathVariable String profileid) {

        profileService.deleteProfile(profileid);

        return ResponseEntity.ok("User deleted successfully");
    }
}