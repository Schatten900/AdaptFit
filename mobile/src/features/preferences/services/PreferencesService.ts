import api from '~/config/api';
import { CreatePreferenceRequest, UpdatePreferenceRequest } from '~/types';
import { ApiPreferences } from '~/types/preferences/apiTypes';


export const preferenceService = {
  getPreference(): Promise<ApiPreferences> {
    return api.get('/api/preferences');
  },

  setPreference(preferenceData: CreatePreferenceRequest): Promise<ApiPreferences> {
    return api.post('/api/preferences', preferenceData);
  },

  updatePreference(preferenceData: UpdatePreferenceRequest): Promise<ApiPreferences> {
    return api.put('/api/preferences', preferenceData);
  },
};