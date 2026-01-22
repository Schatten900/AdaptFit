import { useState } from 'react';
import { registerService } from '../services/RegisterService';


export const useRegister = () => {

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const register = async (email:string,username:string,password:string,confirm:string) => {
        setLoading(true);
        setError(null);
        try {
            const data = await registerService.register(email, username, password,confirm);
        
            return data;
        } 
        catch (err) {
            setError('Register failed');
        } 
        finally{
            setLoading(false);
        }

    }
    return { register, loading, error }
}
