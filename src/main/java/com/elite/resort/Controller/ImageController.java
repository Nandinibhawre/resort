package com.elite.resort.Controller;


import com.elite.resort.Model.Image;
import com.elite.resort.Repository.ImageRepo;
import com.elite.resort.Services.S3Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

    @RestController
    @RequestMapping("/api/images")
    @RequiredArgsConstructor
    public class ImageController {

        private final S3Service s3Service;
        private final ImageRepo imageRepo;

        // 🔥 Upload image with roomNumber
        @PostMapping("/upload")
        public ResponseEntity<?> uploadImage(
                @RequestParam("file") MultipartFile file,
                @RequestParam("roomNumber") String roomNumber) {

            try {

                // 1️⃣ Upload to S3
                String imageUrl = s3Service.uploadFile(file);

                // 2️⃣ Save in MongoDB
                Image image = new Image(
                        file.getOriginalFilename(),
                        imageUrl,
                        roomNumber
                );

                imageRepo.save(image);

                return ResponseEntity.ok(image);

            } catch (Exception e) {

                return ResponseEntity.internalServerError()
                        .body("Upload failed: " + e.getMessage());
            }
        }
    }