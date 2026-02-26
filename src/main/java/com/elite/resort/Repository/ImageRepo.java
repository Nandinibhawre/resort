package com.elite.resort.Repository;

import com.elite.resort.Model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepo extends MongoRepository<Image, String>
{
    Optional<Image> findByRoomNumber(String roomNumber);
}