package com.elite.resort.Controller;
import com.elite.resort.Model.Image;
import com.elite.resort.Model.User;
import com.elite.resort.Repository.ImageRepo;
import com.elite.resort.Repository.ProfileRepo;
import com.elite.resort.Repository.UserRepo;
import com.elite.resort.Services.S3Service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileImgController {

    private final S3Service s3Service;
    private final ImageRepo imageRepo;
    private final UserRepo userRepo;
    private final ProfileRepo profileRepo;

    // 🔥 Upload profile image
    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        try {

            String email = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            // 2️⃣ Find user
            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 3️⃣ Upload to S3
            String imageUrl = s3Service.uploadFile(file);

            // 4️⃣ Save in Image collection
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setImageUrl(imageUrl);
            image.setUserId(user.getUserId());

            imageRepo.save(image);

            return ResponseEntity.ok("Image uploaded successfully");

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Upload failed: " + e.getMessage());
        }
    }
}
