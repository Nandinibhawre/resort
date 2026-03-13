package com.elite.resort.Services;


import java.util.List;
import java.util.Optional;

import com.elite.resort.DTO.ProfileRequest;
import com.elite.resort.DTO.ProfileResponseDTO;
import com.elite.resort.Model.Image;
import com.elite.resort.Model.Profile;
import com.elite.resort.Model.User;
import com.elite.resort.Repository.ProfileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepo profileRepository;
    private final S3Service s3Service;

    public ProfileResponseDTO createOrUpdateProfile(
            String userId,
            String name,
            String email,
            ProfileRequest request,
            MultipartFile idProofImage) {

        Optional<Profile> existing = profileRepository.findByUserId(userId);

        Profile profile;

        if (existing.isPresent()) {
            profile = existing.get();
        } else {
            profile = new Profile();
            profile.setUserId(userId);
        }
        profile.setName(name);
        profile.setEmail(email);

        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());
        profile.setIdProof(request.getIdProof());
        if (idProofImage != null && !idProofImage.isEmpty()) {

            Image image = s3Service.uploadFile(idProofImage, "idProofs");

            profile.setImageUrl(image.getImageUrl()); // FIX
        }

        Profile saved = profileRepository.save(profile);

        return mapToDTO(saved);
    }

    public Profile getProfileByUserId(String userId) {

        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public List<Profile> searchProfilesByName(String name) {
        return profileRepository.findByNameContainingIgnoreCase(name);
    }

    public void deleteProfile(String profileid) {

        Profile profile = profileRepository.findById(profileid)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profileRepository.delete(profile);
    }
    // Entity → DTO mapper
    private ProfileResponseDTO mapToDTO(Profile profile) {

        ProfileResponseDTO dto = new ProfileResponseDTO();

        dto.setProfileId(profile.getProfileid());
        dto.setUserId(profile.getUserId());
        dto.setName(profile.getName());
        dto.setEmail(profile.getEmail());

        dto.setPhone(profile.getPhone());

        dto.setAddress(profile.getAddress());
        dto.setIdProof(profile.getIdProof());
        dto.setImageUrl(profile.getImageUrl());

        return dto;
    }
}