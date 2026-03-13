package com.elite.resort.Services;

import com.elite.resort.Model.Image;
import com.elite.resort.Repository.ImageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final ImageRepo imageRepo;

    @Value("${aws.bucket-name}")
    private String bucketName;

    public Image uploadFile(MultipartFile file, String folderName) {

        try {

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            String key = "Resort/" + folderName + "/" + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes())
            );

            String imageUrl = "https://" + bucketName + ".s3.amazonaws.com/" + key;

            Image image = new Image();
            image.setFileName(fileName);
            image.setImageUrl(imageUrl);

            return imageRepo.save(image);   // saved image with id

        } catch (Exception e) {

            throw new RuntimeException("Failed to upload file to S3", e);

        }
    }
}