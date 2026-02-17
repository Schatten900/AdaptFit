import React, { useState, useEffect } from 'react';
import { View, Text, ScrollView, TouchableOpacity, Dimensions, ActivityIndicator } from 'react-native';
import { Ionicons, MaterialCommunityIcons, FontAwesome5 } from '@expo/vector-icons';
import { MotiView } from 'moti';
import { useRouter } from 'expo-router';
import { BackButton } from '~/commons/components/BackButton';
import { workoutSessionStorage, WorkoutSessionData } from '~/commons/utils/workoutSessionStorage';

const { width } = Dimensions.get('window');

export default function EndWorkoutScreen() {
  const router = useRouter();
  const [sessionData, setSessionData] = useState<WorkoutSessionData | null>(null);
  const [previousSession, setPreviousSession] = useState<WorkoutSessionData | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadSession = async () => {
      const data = await workoutSessionStorage.getSession();
      setSessionData(data);
      
      if (data) {
        const previous = await workoutSessionStorage.getLastWorkoutDayHistory(data.workoutDayId);
        setPreviousSession(previous);
      }
      
      setLoading(false);
    };
    loadSession();
  }, []);

  const formatDuration = (sec: number) => {
    const m = Math.floor(sec / 60);
    const s = sec % 60;
    return `${m}:${s < 10 ? '0' : ''}${s}`;
  };

  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('pt-BR', { day: '2-digit', month: 'short' });
  };

  const today = new Date();

  const motivationMessages = [
    "Parabéns! Suas fibras musculares estão enviando um 'obrigado' (e um 'socorro').",
    "Treino finalizado. Você está 1% mais perto de virar uma máquina.",
    "Missão cumprida. O espelho já está começando a notar a diferença!",
    "IA AdaptFit: Performance detectada acima da média. Continue assim, humano.",
    "Mais um treino concluído! Seu corpo te agradeçe.",
    "Você está no caminho certo! Keep going!"
  ];

  const randomMessage = motivationMessages[Math.floor(Math.random() * motivationMessages.length)];

  if (loading) {
    return (
      <View className="flex-1 bg-background justify-center items-center">
        <ActivityIndicator size="large" color="#00E0A4" />
      </View>
    );
  }

  const getComparison = (current: number, previous: number) => {
    if (!previous) return null;
    const diff = current - previous;
    const percent = Math.round((diff / previous) * 100);
    if (diff > 0) return { text: `+${percent}%`, positive: true };
    if (diff < 0) return { text: `${percent}%`, positive: false };
    return { text: '0%', positive: true };
  };

  const stats = sessionData ? {
    duration: formatDuration(sessionData.duration),
    volume: sessionData.totalWeight.toLocaleString('pt-BR'),
    exercises: sessionData.exercises.length,
    sets: `${sessionData.completedSets}/${sessionData.totalSets}`,
  } : {
    duration: "0:00",
    volume: "0",
    exercises: 0,
    sets: "0/0",
  };

  return (
    <View className="flex-1 bg-background pt-12">
      {/* Header com botão voltar */}
      <View className="px-6 mb-4">
        <BackButton />
      </View>
      
      <ScrollView className="flex-1" showsVerticalScrollIndicator={false}>
        {/* Mensagem de Celebração */}
      <View className="px-6 items-center mb-8">
        <MotiView
          from={{ scale: 0, rotate: '0deg' }}
          animate={{ scale: 1, rotate: '360deg' }}
          transition={{ type: 'spring', damping: 10 }}
          className="w-20 h-20 bg-primary rounded-full items-center justify-center mb-4 shadow-lg shadow-primary/40"
        >
          <Ionicons name="checkmark-done" size={40} color="#020617" />
        </MotiView>
        
        <MotiView
          from={{ opacity: 0, translateY: 10 }}
          animate={{ opacity: 1, translateY: 0 }}
          transition={{ delay: 300 }}
        >
          <Text className="text-text-primary text-3xl font-bold text-center">Treino Concluído!</Text>
          <Text className="text-text-secondary text-center mt-2 px-4 italic">
            "{randomMessage}"
          </Text>
        </MotiView>
      </View>

      {/* Calendário Minimalista (Data do Dia) */}
      <View className="px-6 mb-6">
        <View className="bg-surface border-thin border-stroke rounded-card p-4 flex-row items-center justify-between">
          <View className="flex-row items-center">
            <Ionicons name="calendar-outline" size={20} color="#00E0A4" />
            <Text className="text-text-primary font-bold ml-3 text-lg">
              {today.toLocaleDateString('pt-BR', { day: '2-digit', month: 'long', year: 'numeric' })}
            </Text>
          </View>
          <View className="bg-primary/10 px-3 py-1 rounded-full">
            <Text className="text-primary font-bold text-xs uppercase">Hoje</Text>
          </View>
        </View>
      </View>

      {/* Cards de Resumo Rápido */}
      <View className="px-6 flex-row flex-wrap justify-between mb-6">
        {[
          { label: 'Tempo', value: stats.duration, icon: 'time-outline', key: 'duration' },
          { label: 'Volume', value: `${stats.volume} kg`, icon: 'barbell-outline', key: 'volume' },
          { label: 'Séries', value: stats.sets, icon: 'repeat-outline', key: 'sets' },
          { label: 'Exercícios', value: stats.exercises, icon: 'fitness-outline', key: 'exercises' },
        ].map((item, index) => {
          const comparison = sessionData && previousSession 
            ? item.key === 'volume' 
              ? getComparison(sessionData.totalWeight, previousSession.totalWeight)
              : item.key === 'sets'
                ? getComparison(sessionData.completedSets, previousSession.completedSets)
                : null
            : null;
          
          return (
            <MotiView
              key={item.label}
              from={{ opacity: 0, scale: 0.8 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: 400 + (index * 100) }}
              className="w-[48%] bg-surface p-4 rounded-premium border-thin border-stroke mb-4"
            >
              <Ionicons name={item.icon as any} size={18} color="#94A3B8" />
              <Text className="text-text-secondary text-xs font-semibold uppercase mt-2">{item.label}</Text>
              <View className="flex-row items-center">
                <Text className="text-text-primary text-xl font-bold">{item.value}</Text>
                {comparison && (
                  <View className={`ml-2 px-2 py-0.5 rounded-full ${comparison.positive ? 'bg-status-success/20' : 'bg-status-error/20'}`}>
                    <Text className={`text-xs font-bold ${comparison.positive ? 'text-status-success' : 'text-status-error'}`}>
                      {comparison.text}
                    </Text>
                  </View>
                )}
              </View>
            </MotiView>
          );
        })}
      </View>

      {/* Comparação com Treino Anterior */}
      {previousSession && sessionData && (
        <View className="px-6 mb-6">
          <View className="bg-surface border-thin border-stroke rounded-card p-4">
            <View className="flex-row items-center mb-3">
              <Ionicons name="trending-up" size={20} color="#00E0A4" />
              <Text className="text-text-primary font-bold ml-2">Comparação com último treino</Text>
            </View>
            <Text className="text-text-secondary text-xs">
              {formatDate(previousSession.finishedAt)} • {previousSession.workoutDayName}
            </Text>
            <View className="flex-row mt-3 justify-between">
              <View className="items-center">
                <Text className="text-text-secondary text-xs">Volume</Text>
                <Text className="text-text-primary font-bold">
                  {previousSession.totalWeight} → {sessionData.totalWeight} kg
                </Text>
              </View>
              <View className="items-center">
                <Text className="text-text-secondary text-xs">Séries</Text>
                <Text className="text-text-primary font-bold">
                  {previousSession.completedSets} → {sessionData.completedSets}
                </Text>
              </View>
              <View className="items-center">
                <Text className="text-text-secondary text-xs">Tempo</Text>
                <Text className="text-text-primary font-bold">
                  {formatDuration(previousSession.duration)} → {formatDuration(sessionData.duration)}
                </Text>
              </View>
            </View>
          </View>
        </View>
      )}
      
      {/* Detalhamento dos Exercícios */}
      {sessionData && sessionData.exercises.length > 0 && (
        <View className="px-6 mb-8">
          <Text className="text-text-secondary text-xs font-semibold uppercase tracking-widest mb-4">Resumo por Exercício</Text>
          {sessionData.exercises.map((ex, index) => (
            <View key={index} className="bg-surface p-4 rounded-xl mb-2 border-thin border-stroke">
              <View className="flex-row justify-between items-center">
                <Text className="text-text-primary font-bold flex-1">{ex.name}</Text>
                <Text className="text-primary text-sm">
                  {ex.completedSets}/{ex.sets} séries
                </Text>
              </View>
              <Text className="text-text-secondary text-xs mt-1">
                {ex.reps} reps • {ex.weight} kg
              </Text>
            </View>
          ))}
        </View>
      )}

      {/* CTA Final */}
      <View className="px-6 mb-12">
        <TouchableOpacity 
          onPress={async () => {
            await workoutSessionStorage.addToHistory(sessionData!);
            router.push('/feedback');
          }}
          className="bg-primary p-5 rounded-premium items-center justify-center flex-row shadow-lg shadow-primary/20"
        >
          <Text className="text-primary font-bold text-lg mr-2">DAR FEEDBACK</Text>
          <Ionicons name="arrow-forward" size={20} color="#020617" />
        </TouchableOpacity>
        
        <TouchableOpacity 
          onPress={async () => {
            await workoutSessionStorage.clearSession();
            router.replace('/home');
          }}
          className="mt-3 p-4 rounded-premium border-thin border-stroke items-center"
        >
          <Text className="text-text-secondary font-semibold">Pular</Text>
        </TouchableOpacity>
      </View>
      </ScrollView>
    </View>
  );
}