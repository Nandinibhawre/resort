package com.elite.resort.Services;


import com.elite.resort.Model.Image;
import com.elite.resort.Repository.ImageRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final ImageRepo imageRepository;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Value("${aws.folderName}")
    private String folderName;

    public S3Service(S3Client s3Client, ImageRepo imageRepository) {
        this.s3Client = s3Client;
        this.imageRepository = imageRepository;
    }

    public String uploadFile(MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 👉 create key with folder
        String key = "Resort/RoomImages";

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

        // 👉 save to MongoDB
        Image image = new Image(fileName, imageUrl);
        imageRepository.save(image);

        return imageUrl;
    }
}
