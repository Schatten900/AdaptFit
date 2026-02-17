import { router } from 'expo-router';

export const navigationService = {
  goToLogin() {
    router.replace('/auth/login');
  },
};