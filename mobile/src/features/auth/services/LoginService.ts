import api from '~/config/api';
import { LoginRequest, LoginResponse } from '~/types';

export const loginService = {
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    return api.post('/auth/login', credentials);
  },
};
