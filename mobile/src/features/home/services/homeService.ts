import axios from 'axios';
import { API_BASE_URL } from "~/config/api";
import { authStorage } from '~/commons/utils/authStorage';

export const homeService = {
    async getProfile() {
        const token = await authStorage.getToken();
        const response = await axios.get(`${API_BASE_URL}/api/profile`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        return response.data;
    }
}