package com.elite.resort.Repository;


import java.util.List;
import java.util.Optional;

import com.elite.resort.Model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProfileRepo extends MongoRepository<Profile, String> {
    Optional<Profile> findByUserId(String userId);

    List<Profile> findByNameContainingIgnoreCase(String name);

    Optional<Profile> findByEmail(String email);
}