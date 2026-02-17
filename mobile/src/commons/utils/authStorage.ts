// ~/utils/authStorage.ts
import AsyncStorage from '@react-native-async-storage/async-storage';

export const authStorage = {
    async saveUserId(userId: number) {
        await AsyncStorage.setItem('user_id', userId.toString());
    },

    async getUserId(): Promise<number | null> {
        const userId = await AsyncStorage.getItem('user_id');
        return userId ? parseInt(userId, 10) : null;
    },

    async saveToken(token: string) {
        await AsyncStorage.setItem('token', token);
    },

    async getToken(): Promise<string | null> {
        return AsyncStorage.getItem('token');
    },

    async setHasProfile(value: boolean) {
        await AsyncStorage.setItem('hasProfile', value ? 'true' : 'false');
    },

    async getHasProfile(): Promise<boolean> {
        return (await AsyncStorage.getItem('hasProfile')) === 'true';
    },

    async clear() {
        await AsyncStorage.multiRemove([
            'user_id',
            'token',
            'hasProfile',
        ]);
    }
};
