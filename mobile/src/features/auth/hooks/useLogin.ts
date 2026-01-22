import { useState } from 'react';
import { loginService } from '../services/LoginService';
import { authStorage } from '../../../commons/utils/authStorage';

export const useLogin = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const login = async (email: string, password: string) => {
        setLoading(true);
        setError(null);
        try {
            const data = await loginService.login(email, password);

            console.log('Login response:', data); // Debug

            // Verifica se data e userId existem (agora vem dentro de data.data devido ao ApiResponse)
            if (!data || !data.data || !data.data.userId) {
                throw new Error('Invalid login response: missing userId');
            }

            // Aqui você pode salvar o token ou navegar
            await authStorage.saveUserId(data.data.userId);
            await authStorage.saveToken(data.data.token);

            return data;
        } catch (err) {
            console.error('Login error:', err);
            setError('Login failed: ' + (err.response?.data?.message || err.message || 'Unknown error'));
        } finally {
            setLoading(false);
        }
    };

    return { login, loading, error };
};