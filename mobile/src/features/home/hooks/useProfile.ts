import { useState } from 'react';
import { homeService } from '../services/homeService';

export const useProfile = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const getProfile = async () => {
        setLoading(true);
        setError(null);
        try {
            const data = await homeService.getProfile();
            return data;
        } catch (err) {
            setError('get profile failed');
        } finally {
            setLoading(false);
        }
    };

    return { getProfile, loading, error };
};