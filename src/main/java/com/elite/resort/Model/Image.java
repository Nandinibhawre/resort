package com.elite.resort.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "images")
@NoArgsConstructor
public class Image {

    @Id
    private String imageId;
    private String fileName;
    private String imageUrl;
    private String userId;

    public Image(String fileName, String imageUrl) {
        this.fileName = fileName;
        this.imageUrl = imageUrl;
    }
}