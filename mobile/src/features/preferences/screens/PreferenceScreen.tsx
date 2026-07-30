import React from 'react';
import { View, Text, TouchableOpacity, ScrollView, ActivityIndicator } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import { MotiView } from 'moti';
import { useMyPreference } from '../hooks/useMyPreferences';
import { BackButton } from '~/commons/components/BackButton';
import { formatEquipment, formatInjury } from '../utils/preferencesMapper';

export default function PreferenceScreen() {
  const router = useRouter();
  const { data: preferences, isLoading } = useMyPreference(true);

  if (isLoading) {
    return (
      <View className="flex-1 items-center justify-center bg-background">
        <ActivityIndicator size="large" color="#00E0A4" />
      </View>
    );
  }

  return (
    <View className="flex-1 bg-background pt-12">
      <View className="px-6 flex-row items-center h-14 mb-4">
        <BackButton />
        <Text className="text-text-primary text-xl font-bold ml-4">Preferências</Text>
      </View>

      <ScrollView className="flex-1 px-6">
        <MotiView
          from={{ opacity: 0, translateY: 20 }}
          animate={{ opacity: 1, translateY: 0 }}
          transition={{ type: 'timing', duration: 500 }}
          className="bg-surface p-5 rounded-card border-thin border-stroke mb-6"
        >
          <Text className="text-text-secondary text-sm uppercase tracking-widest mb-4 font-semibold">
            Equipamentos Disponíveis
          </Text>
          {preferences?.availableEquipment?.length ? (
            <View className="flex-row flex-wrap gap-2">
              {preferences.availableEquipment.map((eq) => (
                <View key={eq} className="bg-background px-3 py-1.5 rounded-full border border-primary/50">
                  <Text className="text-primary text-xs font-semibold">{formatEquipment(eq)}</Text>
                </View>
              ))}
            </View>
          ) : (
            <Text className="text-text-secondary italic"> Nenhum equipamento registrado</Text>
          )}
        </MotiView>

        <MotiView
          from={{ opacity: 0, translateY: 20 }}
          animate={{ opacity: 1, translateY: 0 }}
          transition={{ type: 'timing', duration: 500, delay: 100 }}
          className="bg-surface p-5 rounded-card border-thin border-stroke mb-6"
        >
          <Text className="text-text-secondary text-sm uppercase tracking-widest mb-4 font-semibold">
            Lesões / Restrições
          </Text>
          {preferences?.injuries?.length ? (
            <View className="flex-row flex-wrap gap-2">
              {preferences.injuries.map((inj) => (
                <View key={inj} className="bg-background px-3 py-1.5 rounded-full border border-status-error/50">
                  <Text className="text-status-error text-xs font-semibold">{formatInjury(inj)}</Text>
                </View>
              ))}
            </View>
          ) : (
            <Text className="text-text-secondary italic">Nenhuma lesão registrada</Text>
          )}
        </MotiView>

        <MotiView
          from={{ opacity: 0, translateY: 20 }}
          animate={{ opacity: 1, translateY: 0 }}
          transition={{ type: 'timing', duration: 500, delay: 200 }}
          className="bg-surface p-5 rounded-card border-thin border-stroke mb-6"
        >
          <Text className="text-text-secondary text-sm uppercase tracking-widest mb-4 font-semibold">
            Exercícios Bloqueados
          </Text>
          {preferences?.exerciseBlacklist?.length ? (
            <View className="flex-row flex-wrap gap-2">
              {preferences.exerciseBlacklist.map((ex) => (
                <View key={ex} className="bg-background px-3 py-1.5 rounded-full border border-stroke">
                  <Text className="text-text-secondary text-xs font-semibold">{ex}</Text>
                </View>
              ))}
            </View>
          ) : (
            <Text className="text-text-secondary italic">Nenhum exercício bloqueado</Text>
          )}
        </MotiView>

        <TouchableOpacity
          onPress={() => router.push('/edit-preferences')}
          className="bg-primary w-full p-4 rounded-premium items-center mb-12 active:opacity-70"
        >
          <Text className="text-primary font-bold text-lg">EDITAR PREFERÊNCIAS</Text>
        </TouchableOpacity>
      </ScrollView>
    </View>
  );
}
