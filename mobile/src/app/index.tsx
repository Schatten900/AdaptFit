import { useEffect } from 'react';
import { useRouter } from 'expo-router';
import { authStorage } from '~/commons/utils/authStorage';

export default function Index() {
    const router = useRouter();

    useEffect(() => {
        const checkAuth = async () => {
            const userId = await authStorage.getUserId();
            if (userId) {
                router.replace('/home');
            } else {
                router.replace('/auth/login');
            }
        };

        checkAuth();
    }, [router]);

    return null; // Or a loading screen
}