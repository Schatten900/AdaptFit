import { useMutation, useQueryClient } from '@tanstack/react-query';
import { CreatePreferenceRequest } from '~/types';
import { preferenceService } from '../services/PreferencesService';

export function useSetPreference() {
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (data: CreatePreferenceRequest) =>
      preferenceService.setPreference(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['preference'] });
    },
  });

  return {
    setPreference: mutation.mutateAsync,
    loading: mutation.isPending,
  };
}
