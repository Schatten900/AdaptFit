import axios from 'axios';
import Toast from 'react-native-toast-message';
import { authStorage } from '../commons/utils/authStorage';
import { ApiError } from '../types/api/ApiError';
import {navigationService } from '../navigation/navigationService'

export const API_BASE_URL = 'http://localhost:8080';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

/**
 * ============================
 * REQUEST INTERCEPTOR
 * ============================
 */
api.interceptors.request.use(
  async (config) => {
    const token = await authStorage.getToken();
    console.log('[Axios] Token:', token);

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

/**
 * ============================
 * RESPONSE INTERCEPTOR
 * ============================
 * Normaliza ApiResponse<T> → T
 * Mostra Toast global
 */
api.interceptors.response.use(
  (response) => {
    if (
      response.data &&
      typeof response.data === 'object' &&
      'data' in response.data
    ) {
      return response.data.data;
    }

    return response.data;
  },
  async (error) => {
    const config = error.config;
    const status = error?.response?.status ?? 500;
    const data = error?.response?.data;

    const message = data?.message || 'Erro inesperado';
    const type = data?.type || 'ERROR';

    // Toast global (React Native)
    if (!config?.silent) {
      Toast.show({
        type: type === 'SUCCESS'
          ? 'success'
          : type === 'WARNING'
          ? 'info'
          : 'error',
        text1: message,
      });
    }

    // 🔐 Auth error
    if ((status === 401 || status === 403) && !config?.skipAuth) {
      await authStorage.clear();
      navigationService.goToLogin();
    }

    return Promise.reject(
      new ApiError(message, status, type)
    );
  }
);

export default api;
