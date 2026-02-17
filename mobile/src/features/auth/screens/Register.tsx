import {
  View,
  TextInput,
  Text,
  TouchableOpacity,
  ActivityIndicator,
  ScrollView,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useState } from 'react';
import { useRegister } from '../hooks/useRegister';
import { useRouter } from 'expo-router';
import { RegisterRequest } from '~/types';

export default function RegisterScreen() {
  const router = useRouter();
  const { register, loading } = useRegister();

  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handleRegister = async () => {
    const registerData: RegisterRequest = {
      email,
      username,
      password,
      confirmPassword,
    };

    try {
      await register(registerData);
      router.replace('/auth/login');
    } catch (error) {
      // Erro tratado pelo interceptor do api.ts com Toast global
    }
  };

  return (
    <ScrollView
      className="flex-1 bg-background"
      contentContainerStyle={{
        flexGrow: 1,
        justifyContent: 'center',
        padding: 24,
      }}
      showsVerticalScrollIndicator={false}
    >
      <View className="items-center mb-8">
        <TouchableOpacity className="w-20 h-20 items-center justify-center rounded-full bg-surface border-thin border-stroke mb-4">
          <Ionicons name="person-add-outline" size={36} color="#00E0A4" />
        </TouchableOpacity>
        <Text className="text-text-primary text-3xl font-bold">
          Criar Conta
        </Text>
        <Text className="text-text-secondary text-base font-sans text-center mt-1">
          Comece sua jornada inteligente no AdaptFit
        </Text>
      </View>

      <View className="gap-y-4">
        <View>
          <Text className="text-text-secondary mb-2 ml-1 font-semibold text-xs uppercase tracking-widest">
            E-mail
          </Text>
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
          <Text className="text-text-secondary mb-2 ml-1 font-semibold text-xs uppercase tracking-widest">
            Nome de Usuário
          </Text>
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
          <Text className="text-text-secondary mb-2 ml-1 font-semibold text-xs uppercase tracking-widest">
            Senha
          </Text>
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
          <Text className="text-text-secondary mb-2 ml-1 font-semibold text-xs uppercase tracking-widest">
            Confirmar Senha
          </Text>
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

      <TouchableOpacity
        onPress={handleRegister}
        disabled={loading}
        activeOpacity={0.8}
        className="bg-primary mt-8 p-4 rounded-premium items-center justify-center"
      >
        {loading ? (
          <ActivityIndicator color="#020617" />
        ) : (
          <Text className="text-primary font-bold text-lg uppercase tracking-wider">
            Finalizar Cadastro
          </Text>
        )}
      </TouchableOpacity>

      <TouchableOpacity
        className="mt-6 mb-10 items-center"
        onPress={() => router.replace('/auth/login')}
      >
        <Text className="text-text-secondary font-sans">
          Já possui conta?{' '}
          <Text className="text-primary font-bold">Fazer Login</Text>
        </Text>
      </TouchableOpacity>
    </ScrollView>
  );
}
