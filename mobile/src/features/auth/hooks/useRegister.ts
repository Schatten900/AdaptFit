import { useMutation } from '@tanstack/react-query';
import { registerService } from '../services/RegisterService';
import { RegisterRequest } from '~/types';

export const useRegister = () => {
  const mutation = useMutation({
    mutationFn: async (credentials: RegisterRequest) => {
      await registerService.register(credentials);
    },
  });

  return {
    register: mutation.mutateAsync,
    loading: mutation.isPending,
  };
};
