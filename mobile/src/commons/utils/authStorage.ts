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
        return await AsyncStorage.getItem('token');
    },

    async clear() {
        await AsyncStorage.removeItem('user_id');
        await AsyncStorage.removeItem('token');
    }
};