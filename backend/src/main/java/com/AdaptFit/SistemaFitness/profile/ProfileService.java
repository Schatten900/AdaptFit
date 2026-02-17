package com.AdaptFit.SistemaFitness.profile;

import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
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
        return profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Perfil não encontrado"));
    }

    public Profile createOrUpdateProfile(Profile data) {
        User user = userService.getCurrentUser();

        Profile profile = profileRepository
                .findByUserId(user.getId())
                .orElse(new Profile());

        profile.setUserId(user.getId());
        profile.setAge(data.getAge());
        profile.setHeight(data.getHeight());
        profile.setWeight(data.getWeight());
        profile.setGoal(data.getGoal());
        profile.setExperience(data.getExperience());
        profile.setGender(data.getGender());

        return profileRepository.save(profile);
    }
}
