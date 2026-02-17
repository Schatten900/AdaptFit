import { useQuery } from "@tanstack/react-query";
import { profileService } from "../../profile/services/ProfileService";
import { mapApiProfileToForm } from "../../profile/utils/profileMapper";
import { ProfileFormData } from "~/types";

const PROFILE_KEY = ["profile"];

export function useProfile(enabled = true) {
  return useQuery<ProfileFormData>({
    queryKey: PROFILE_KEY,
    queryFn: async () => {
      const apiProfile = await profileService.getProfile();
      return mapApiProfileToForm(apiProfile);
    },
    enabled,
    retry: false,
  });
}
