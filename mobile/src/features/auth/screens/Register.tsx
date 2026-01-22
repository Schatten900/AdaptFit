import { View, TextInput, Text, TouchableOpacity, ActivityIndicator, ScrollView } from 'react-native';
import { FontAwesome5, Ionicons } from '@expo/vector-icons';
import { useState } from 'react';
import { useRegister } from '../hooks/useRegister';
import { useLogin } from '../hooks/useLogin';
import { useRouter } from 'expo-router'; // Caso esteja usando Expo Router

export default function RegisterScreen() {
    const router = useRouter();
    const { register, loading: registerLoading, error: registerError } = useRegister();
    const { login, loading: loginLoading, error: loginError } = useLogin();

    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const handleRegister = async () => {
        const registerResult = await register(email, username, password, confirmPassword);
        if (registerResult) {
            const loginResult = await login(email, password);
            if (loginResult) {
                router.push('/personal');
            }
        }
    };

    const loading = registerLoading || loginLoading;
    const error = registerError || loginError;

    return (
        // Usamos ScrollView para garantir que usuários com telas menores consigam rolar os campos
        <ScrollView 
            className="flex-1 bg-background" 
            contentContainerStyle={{ flexGrow: 1, justifyContent: 'center', padding: 24 }}
            showsVerticalScrollIndicator={false}
        >
            {/* Header / Logo Section */}
            <View className="items-center mb-8">
                <TouchableOpacity className="w-20 h-20 items-center justify-center rounded-full bg-surface border-thin border-stroke mb-4">
                    <Ionicons name="person-add-outline" size={36} color="#00E0A4" />
                </TouchableOpacity>
                <Text className="text-text-primary text-3xl font-bold">Criar Conta</Text>
                <Text className="text-text-secondary text-base font-sans text-center mt-1">
                    Comece sua jornada inteligente no AdaptFit
                </Text>
            </View>

            {/* Form Section */}
            <View className="gap-y-4">
                <View>
                    <Text className="text-text-secondary mb-2 ml-1 font-semibold text-xs uppercase tracking-widest">E-mail</Text>
                    <TextInput
                        className="bg-surface text-text-primary p-4 rounded-premium border-thin border-stroke focus:border-primary"
                        placeholder="seu@email.com"
                        placeholderTextColor="#94A3B8"
                        keyboardType="email-address"
                        autoCapitalize="none"
                        value={email}
                        onChangeText={setEmail}
                    />
                </View>

                <View>
                    <Text className="text-text-secondary mb-2 ml-1 font-semibold text-xs uppercase tracking-widest">Nome de Usuário</Text>
                    <TextInput
                        className="bg-surface text-text-primary p-4 rounded-premium border-thin border-stroke focus:border-primary"
                        placeholder="como quer ser chamado?"
                        placeholderTextColor="#94A3B8"
                        autoCapitalize="none"
                        value={username}
                        onChangeText={setUsername}
                    />
                </View>

                <View>
                    <Text className="text-text-secondary mb-2 ml-1 font-semibold text-xs uppercase tracking-widest">Senha</Text>
                    <TextInput
                        className="bg-surface text-text-primary p-4 rounded-premium border-thin border-stroke focus:border-primary"
                        placeholder="••••••••"
                        placeholderTextColor="#94A3B8"
                        secureTextEntry
                        value={password}
                        onChangeText={setPassword}
                    />
                </View>

                <View>
                    <Text className="text-text-secondary mb-2 ml-1 font-semibold text-xs uppercase tracking-widest">Confirmar Senha</Text>
                    <TextInput
                        className="bg-surface text-text-primary p-4 rounded-premium border-thin border-stroke focus:border-primary"
                        placeholder="••••••••"
                        placeholderTextColor="#94A3B8"
                        secureTextEntry
                        value={confirmPassword}
                        onChangeText={setConfirmPassword}
                    />
                </View>
            </View>

            {/* Error Message */}
            {error && (
                <View className="mt-4 p-3 bg-status-error/10 border border-status-error/20 rounded-lg">
                    <Text className="text-status-error text-center font-sans text-sm">
                        {error}
                    </Text>
                </View>
            )}

            {/* Register Button */}
            <TouchableOpacity 
                onPress={handleRegister}
                disabled={loading}
                activeOpacity={0.8}
                className="bg-primary mt-8 p-4 rounded-premium items-center justify-center"
            >
                {loading ? (
                    <ActivityIndicator color="#020617" />
                ) : (
                    <Text className="text-primary font-bold text-lg uppercase tracking-wider">Finalizar Cadastro</Text>
                )}
            </TouchableOpacity>

            {/* Footer */}
            <TouchableOpacity 
                className="mt-6 mb-10 items-center" 
                onPress={() => router.back()} // Volta para a tela de Login
            >
                <Text className="text-text-secondary font-sans">
                    Já possui conta? <Text className="text-primary font-bold">Fazer Login</Text>
                </Text>
            </TouchableOpacity>
        </ScrollView>
    );
}