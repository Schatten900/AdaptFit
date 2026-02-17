import { useMutation, useQueryClient } from "@tanstack/react-query";
import { profileService } from "../services/ProfileService";
import { ProfileFormData } from "~/types";
import { transformFormToApi } from "../../home/utils/profileMapper";
import { GoalType, ExperienceLevel, Gender } from "~/types";

const PROFILE_KEY = ["profile"];

export const useSetProfile = () => {
  const queryClient = useQueryClient();

  const setProfileMutation = useMutation({
    mutationFn: async (formData: ProfileFormData) => {
      const apiData = {
        weight: formData.weight ?? 0,
        height: formData.height ?? 0,
        age: formData.age ?? 0,
        goal: (formData.goal ?? "ENDURANCE") as GoalType,
        experience: (formData.experience ?? "BEGINNER") as ExperienceLevel,
        gender: (formData.gender ?? "OTHER") as Gender,
      };

      return profileService.setProfile(apiData);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: PROFILE_KEY });
    },
  });

  const updateProfileMutation = useMutation({
    mutationFn: async (formData: Partial<ProfileFormData>) => {
      const apiData = transformFormToApi(formData as ProfileFormData);
      return profileService.updateProfile(apiData);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: PROFILE_KEY });
    },
  });

  return {
    setProfile: setProfileMutation.mutateAsync,
    updateProfile: updateProfileMutation.mutateAsync,
    loading:
      setProfileMutation.isPending ||
      updateProfileMutation.isPending,
  };
};
