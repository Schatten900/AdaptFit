import axios from 'axios';
import { API_BASE_URL } from "~/config/api";
import { authStorage } from '~/commons/utils/authStorage';

export const profileService = {
    async setProfile(profileData: any) {
        const token = await authStorage.getToken();
        const response = await axios.post(`${API_BASE_URL}/api/profile`, profileData, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        return response.data;
    }
};