// services/ProfileService.ts
import api from '~/config/api';
import { ApiProfile, CreateProfileRequest, UpdateProfileRequest } from '~/types';

export const profileService = {
  getProfile(): Promise<ApiProfile> {
    return api.get('/api/profile');
  },

  setProfile(profileData: CreateProfileRequest): Promise<ApiProfile> {
    return api.post('/api/profile', profileData);
  },

  updateProfile(profileData: UpdateProfileRequest): Promise<ApiProfile> {
    return api.put('/api/profile', profileData);
  },
};
