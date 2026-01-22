package com.AdaptFit.SistemaFitness.profile;

import com.AdaptFit.SistemaFitness.user.User;
import com.AdaptFit.SistemaFitness.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserService userService;

    public Profile getCurrentUserProfile() {
        User user = userService.getCurrentUser();
        return user.getProfile();
    }

    public Profile createOrUpdateProfile(Profile profile) {
        User user = userService.getCurrentUser();
        profile.setUser(user);
        return profileRepository.save(profile);
    }
}
