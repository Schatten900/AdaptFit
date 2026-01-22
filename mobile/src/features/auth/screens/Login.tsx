import { View, TextInput, Text, TouchableOpacity, ActivityIndicator } from 'react-native';
import { FontAwesome5 } from '@expo/vector-icons';
import { useState } from 'react';
import { useLogin } from '../hooks/useLogin';
import { useRouter } from 'expo-router';

export default function LoginScreen() {
    const { login, loading, error } = useLogin();
    const router = useRouter();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async () => {
        const data = await login(email, password);
        if (data) {
            router.push('/auth/personal');
        }
    };

    return (
        <View className="flex-1 bg-background justify-center p-6">
            {/* Header / Logo Section */}
            <View className="items-center mb-10">
                <View className="w-20 h-20 items-center justify-center rounded-full border-thin border-primary mb-4">
                     <FontAwesome5 name="bolt" size={40} color="#00E0A4" />
                </View>
                <Text className="text-text-primary text-3xl font-bold">AdaptFit</Text>
                <Text className="text-text-secondary text-base font-sans">Visual Design Brief</Text>
            </View>

            {/* Form Section */}
            <View className="space-y-4">
                <View>
                    <Text className="text-text-secondary mb-2 ml-1 font-semibold">E-mail</Text>
                    <TextInput
                        className="bg-surface text-text-primary p-4 rounded-premium border-thin border-stroke"
                        placeholder="seu@email.com"
                        placeholderTextColor="#94A3B8"
                        keyboardType="email-address"
                        autoCapitalize="none"
                        value={email}
                        onChangeText={setEmail}
                    />
                </View>

                <View className="mt-4">
                    <Text className="text-text-secondary mb-2 ml-1 font-semibold">Senha</Text>
                    <TextInput
                        className="bg-surface text-text-primary p-4 rounded-premium border-thin border-stroke"
                        placeholder="••••••••"
                        placeholderTextColor="#94A3B8"
                        secureTextEntry
                        value={password}
                        onChangeText={setPassword}
                    />
                </View>
            </View>

            {/* Error Message */}
            {error && (
                <Text className="text-status-error text-center mt-4 font-sans">
                    {error}
                </Text>
            )}

            {/* Login Button */}
            <TouchableOpacity 
                onPress={handleLogin}
                disabled={loading}
                activeOpacity={0.8}
                className="bg-primary mt-8 p-4 rounded-premium items-center justify-center shadow-lg shadow-primary/20"
            >
                {loading ? (
                    <ActivityIndicator color="#020617" />
                ) : (
                    <Text className="text-primary font-bold text-lg">Entrar</Text>
                )}
            </TouchableOpacity>

            {/* Footer */}
            <TouchableOpacity className="mt-6 items-center" onPress={() => router.push('/auth/register')}>
                <Text className="text-text-secondary font-sans">
                    Não tem uma conta? <Text className="text-primary font-bold">Cadastre-se</Text>
                </Text>
            </TouchableOpacity>
        </View>
    );
}