import axios from 'axios'; 
import { API_BASE_URL } from "~/config/api"

export const loginService = {
    async login(email: string, password: string) {
        const response = await axios.post(`${API_BASE_URL}/auth/login`, { email, password });
        return response.data;
    }
};