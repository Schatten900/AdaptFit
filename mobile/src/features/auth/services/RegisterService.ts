import axios from 'axios';
import { API_BASE_URL } from '~/config/api';


export const registerService = {
    async register(email: string, username: string, password: string, confirmPassword: string) {
        const response = await axios.post(`${API_BASE_URL}/auth/register`, { email, username, password, confirmPassword });
        return response.data;
    }
};