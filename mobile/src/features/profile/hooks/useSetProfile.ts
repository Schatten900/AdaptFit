import { useState } from 'react';
import { profileService } from '../services/ProfileService';


export const useSetProfile = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const setProfile = async (profileData: any) => {
        setLoading(true);
        setError(null);
        try {
            const response = await profileService.setProfile(profileData);
            return response;
        } catch (err) {
            setError('Set profile failed');
        } finally {
            setLoading(false);
        }
    }

    return { setProfile, loading, error };

}