import { useQuery } from '@tanstack/react-query';
import { preferenceService } from '../services/PreferencesService';

export function useMyPreference(enabled = false) {
  return useQuery({
    queryKey: ['preference'],
    queryFn: preferenceService.getPreference,
    enabled,
    retry: false,
  });
}