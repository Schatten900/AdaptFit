import { View, Text, ActivityIndicator, ScrollView, RefreshControl, TouchableOpacity } from "react-native";
import { useState, useEffect, useCallback } from 'react';
import { useProfile } from "../hooks/useProfile";
import { useRouter } from 'expo-router';
import { authStorage } from '~/commons/utils/authStorage';
import { Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';

export default function HomeScreen() {
    const [profileUser, setProfileUser] = useState<any | null>(null);
    const [refreshing, setRefreshing] = useState(false);
    const [userId, setUserId] = useState<number | null>(null);
    
    const { getProfile, loading, error } = useProfile();
    const router = useRouter();

    useEffect(() => {
        const fetchUserId = async () => {
            try {
                const storedUserId = await authStorage.getUserId();
                if (storedUserId) setUserId(storedUserId);
                else router.replace('/auth/login');
            } catch (err) {
                router.replace('/auth/login');
            }
        };
        fetchUserId();
    }, []);

    const handleUser = useCallback(async () => {
        if (!userId) return;
        try {
            const userData = await getProfile();
            if (userData) {
                setProfileUser(userData);
                // Check if profile is incomplete (first time)
                if (!userData.weight || !userData.height || !userData.goal || !userData.experience) {
                    router.replace('/personal');
                }
            }
        } catch (err) { console.error(err); }
    }, [userId, getProfile, router]);

    useEffect(() => { if (userId) handleUser(); }, [userId, handleUser]);

    const onRefresh = useCallback(async () => {
        setRefreshing(true);
        await handleUser();
        setRefreshing(false);
    }, [handleUser, router]);

    if (loading || !userId) {
        return (
            <View className="flex-1 bg-background items-center justify-center">
                <ActivityIndicator size="large" color="#00E0A4" />
                <Text className="mt-4 text-text-secondary font-sans italic">Sincronizando dados biofísicos...</Text>
            </View>
        );
    }

    const imc = profileUser ? (profileUser.weight / ((profileUser.height/100) ** 2)).toFixed(1) : "0";

    return (
        <ScrollView 
            className="flex-1 bg-background"
            refreshControl={
                <RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor="#00E0A4" />
            }
        >
            <View className="p-6 pt-12">
                {/* Header AdaptFit */}
                <View className="flex-row justify-between items-center mb-8">
                    <View>
                        <Text className="text-text-secondary text-sm font-sans uppercase tracking-widest">Performance</Text>
                        <Text className="text-text-primary text-3xl font-bold">Dashboard</Text>
                    </View>
                    <TouchableOpacity className="w-12 h-12 bg-surface rounded-full items-center justify-center border-thin border-stroke">
                        <Ionicons name="notifications-outline" size={24} color="#E5E7EB" />
                    </TouchableOpacity>
                </View>

                {/* IA Insight Card (O "Cérebro" do App) */}
                <View className="bg-surface border-thin border-primary/30 p-5 rounded-card mb-6">
                    <View className="flex-row items-center mb-3">
                        <MaterialCommunityIcons name="robot" size={20} color="#00E0A4" />
                        <Text className="text-primary font-bold ml-2 tracking-tighter">INSIGHT DA IA</Text>
                    </View>
                    <Text className="text-text-primary leading-6">
                        Seu IMC de <Text className="font-bold text-primary">{imc}</Text> indica boa evolução.
                        Com base no seu objetivo de <Text className="italic">{profileUser?.goal}</Text>,
                        sugerimos aumentar a intensidade cardiovascular hoje.
                    </Text>
                </View>

                {/* Grid de Métricas Principais */}
                <View className="flex-row flex-wrap justify-between">
                    <View className="w-[48%] bg-surface p-4 rounded-premium border-thin border-stroke mb-4">
                        <Text className="text-text-secondary text-xs font-semibold mb-1">PESO</Text>
                        <Text className="text-text-primary text-xl font-bold">{profileUser?.weight} <Text className="text-sm font-normal text-text-secondary">kg</Text></Text>
                    </View>
                    <View className="w-[48%] bg-surface p-4 rounded-premium border-thin border-stroke mb-4">
                        <Text className="text-text-secondary text-xs font-semibold mb-1">ALTURA</Text>
                        <Text className="text-text-primary text-xl font-bold">{profileUser?.height} <Text className="text-sm font-normal text-text-secondary">cm</Text></Text>
                    </View>
                </View>

                {/* Status de Experiência e Objetivo */}
                <View className="bg-surface rounded-card p-5 border-thin border-stroke mb-6">
                    <View className="flex-row justify-between items-center border-b border-stroke pb-4 mb-4">
                        <Text className="text-text-secondary font-semibold">Nível</Text>
                        <View className="bg-background px-3 py-1 rounded-full border border-primary/50">
                            <Text className="text-primary font-bold uppercase text-xs">{profileUser?.experience}</Text>
                        </View>
                    </View>
                    <View className="flex-row justify-between items-center">
                        <Text className="text-text-secondary font-semibold">Foco Atual</Text>
                        <Text className="text-text-primary font-bold capitalize">{profileUser?.goal?.replace('_', ' ')}</Text>
                    </View>
                </View>

                {/* Botões de Ação Premium */}
                <View className="gap-y-3">
                    <TouchableOpacity 
                        onPress={() => router.push('/edit-profile')}
                        className="bg-primary p-4 rounded-premium flex-row justify-center items-center"
                    >
                        <Ionicons name="create-outline" size={20} color="#020617" />
                        <Text className="text-background font-bold ml-2">AJUSTAR PERFIL</Text>
                    </TouchableOpacity>

                    <TouchableOpacity
                        onPress={async () => {
                            await authStorage.clear();
                            router.replace('/auth/login');
                        }}
                        className="p-4 rounded-premium border-thin border-status-error/50 items-center"
                    >
                        <Text className="text-status-error font-semibold">Encerrar Sessão</Text>
                    </TouchableOpacity>
                </View>
            </View>
        </ScrollView>
    );
}