package com.elite.resort.Services;


import java.util.Optional;

import com.elite.resort.DTO.ProfileRequest;
import com.elite.resort.Model.Profile;
import com.elite.resort.Repository.ProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepo profileRepository;

    public Profile createOrUpdateProfile(
            String userId,
            String name,
            String email,
            ProfileRequest request) {

        Optional<Profile> existing = profileRepository.findByUserId(userId);

        Profile profile;

        if (existing.isPresent()) {
            profile = existing.get();
        } else {
            profile = new Profile();
            profile.setUserId(userId);
            profile.setName(name);
            profile.setEmail(email);
        }

        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());
        profile.setIdProof(request.getIdProof());
        profile.setPhotoUrl(request.getPhotoUrl());

        return profileRepository.save(profile);
    }

    public Profile getProfileByUserId(String userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }
}
