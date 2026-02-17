package com.AdaptFit.SistemaFitness.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Profile> createProfile(@RequestBody Profile profile) {

        System.out.println(profile);
        return ResponseEntity.ok(profileService.createOrUpdateProfile(profile));
    }

    @PutMapping
    public ResponseEntity<Profile> updateProfile(@RequestBody Profile profile) {
        System.out.println(profile);
        return ResponseEntity.ok(profileService.createOrUpdateProfile(profile));
    }
}
