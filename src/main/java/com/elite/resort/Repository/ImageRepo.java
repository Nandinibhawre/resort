package com.elite.resort.Repository;


import com.elite.resort.Model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepo extends MongoRepository<Image, String> {
}