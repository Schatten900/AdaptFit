import { useQuery } from '@tanstack/react-query';
import { profileService } from '../services/ProfileService';

export function useMyProfile(enabled = false) {
  return useQuery({
    queryKey: ['profile'],
    queryFn: profileService.getProfile,
    enabled,
    retry: false,
  });
}
