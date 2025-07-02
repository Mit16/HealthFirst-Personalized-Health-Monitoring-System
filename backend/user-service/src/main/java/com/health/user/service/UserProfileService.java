package com.health.user.service;


import com.health.user.model.UserProfile;
import com.health.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfile getUserProfile(Long userId) {
        return repository.findById(userId).orElse(null);
    }

    public UserProfile updateUserProfile(UserProfile updatedProfile) {
        return repository.save(updatedProfile);
    }
}