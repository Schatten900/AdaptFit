import api from '~/config/api';
import { RegisterRequest, RegisterResponse } from '~/types';

export const registerService = {
  async register(credentials: RegisterRequest): Promise<RegisterResponse> {
    return api.post('/auth/register', {
      email: credentials.email,
      username: credentials.username,
      password: credentials.password,
      confirmPassword: credentials.confirmPassword,
    });
  },
};
