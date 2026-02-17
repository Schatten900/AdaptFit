import { useMutation } from '@tanstack/react-query';
import { loginService } from '../services/LoginService';
import { authStorage } from '~/commons/utils/authStorage';
import { LoginRequest, LoginResponse } from '~/types';

export const useLogin = () => {
  const mutation = useMutation({
    mutationFn: async (credentials: LoginRequest) => {
      return await loginService.login(credentials);
    },

    onSuccess: async (data) => {
      await authStorage.saveUserId(data.userId);
      await authStorage.saveToken(data.token);
    },
  });

  return {
    login: mutation.mutateAsync,
    loading: mutation.isPending,
  };
};
