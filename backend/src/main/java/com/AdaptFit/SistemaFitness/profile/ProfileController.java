package com.AdaptFit.SistemaFitness.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<Profile> getProfile() {
        try {
            Profile profile = profileService.getCurrentUserProfile();
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Profile not found")) {
                return ResponseEntity.notFound().build();
            }
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Profile> createProfile(@RequestBody ProfileRequest request) {
        Profile profile = mapToEntity(request);
        return ResponseEntity.ok(profileService.createOrUpdateProfile(profile));
    }

    @PutMapping
    public ResponseEntity<Profile> updateProfile(@RequestBody ProfileRequest request) {
        Profile profile = mapToEntity(request);
        return ResponseEntity.ok(profileService.createOrUpdateProfile(profile));
    }

    private Profile mapToEntity(ProfileRequest request) {
        Profile profile = new Profile();
        profile.setWeight(request.getWeight());
        profile.setHeight(request.getHeight());
        profile.setAge(request.getAge());
        profile.setGoal(request.getGoal());
        profile.setExperience(request.getExperience());
        profile.setGender(request.getGender());
        profile.setDaysPerWeek(request.getDaysPerWeek() != null ? request.getDaysPerWeek() : 3);
        profile.setSessionDuration(request.getSessionDuration() != null ? request.getSessionDuration() : 45);
        return profile;
    }
}
