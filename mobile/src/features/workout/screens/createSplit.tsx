import React, { useState, useEffect } from 'react';
import { View, Text, ScrollView, TouchableOpacity, TextInput, ActivityIndicator, Alert, Modal } from 'react-native';
import { Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { MotiView } from 'moti';
import { useRouter } from 'expo-router';

import { useWorkoutDays } from '../hooks/workoutDay/useWorkoutDays';
import { useSplitEditor } from '../hooks/Split/useSplitEditor';
import { SplitResponse } from '~/types/workoutSplit/types';

interface CreateSplitScreenProps {
  splitId?: number;
  initialSplitData?: SplitResponse | null;
}

export default function CreateSplitScreen({ splitId, initialSplitData }: CreateSplitScreenProps) {
  const router = useRouter();

  const {
  workoutDays,
  isLoading: loadingWorkoutDays,
  isFetching,
  refetch,
} = useWorkoutDays();


  const {
    isEditing,
    loading,
    splitName,
    setSplitName,
    splitDescription,
    setSplitDescription,
    splitWorkoutDays,
    initializeWeekSplit,
    loadSplit,
    updateWorkoutDayForDay,
    saveSplit,
    DAYS_OF_WEEK,
  } = useSplitEditor(splitId);

  const [showWorkoutPicker, setShowWorkoutPicker] = useState<number | null>(null);
  const [selectedDayKey, setSelectedDayKey] = useState<string | null>(null);

  useEffect(() => {
    if (initialSplitData) {
      loadSplit(initialSplitData);
    } else {
      initializeWeekSplit();
    }
  }, [initialSplitData]);

  const getWorkoutDayName = (workoutDayId: number) => {
    const workoutDay = workoutDays.find(wd => wd.id === workoutDayId);
    return workoutDay ? workoutDay.name : null;
  };

  const handleDayPress = (dayOfWeek: number, dayKey: string) => {
    console.log('Dia pressionado:', dayKey, 'dia da semana:', dayOfWeek);
    setSelectedDayKey(dayKey);
    setShowWorkoutPicker(dayOfWeek);
  };

  const handleWorkoutSelect = (workoutDayId: number) => {
    console.log('WorkoutDay selecionado ID:', workoutDayId);
    if (showWorkoutPicker !== null) {
      updateWorkoutDayForDay(showWorkoutPicker, workoutDayId);
      setShowWorkoutPicker(null);
      setSelectedDayKey(null);
      Alert.alert('Sucesso', 'Treino selecionado para ' + DAYS_OF_WEEK.find(d => d.value === showWorkoutPicker)?.key);
    }
  };

  const handleRestDay = () => {
    console.log('Dia de descanso selecionado');
    if (showWorkoutPicker !== null) {
      updateWorkoutDayForDay(showWorkoutPicker, 0);
      setShowWorkoutPicker(null);
      setSelectedDayKey(null);
      Alert.alert('Info', 'Dia de descanso configurado para ' + DAYS_OF_WEEK.find(d => d.value === showWorkoutPicker)?.key);
    }
  };

  const handleSave = async () => {
  if (!splitName.trim()) {
    Alert.alert('Erro', 'Por favor, insira um nome para o split');
    return;
  }

  const validDays = splitWorkoutDays.filter(day => day.workoutDayId > 0);
  if (validDays.length === 0) {
    Alert.alert('Erro', 'Selecione pelo menos um dia de treino');
    return;
  }

  await saveSplit(); // erro já tratado globalmente
  router.back();
};


  return (
    <View className="flex-1 bg-background pt-14">
      {/* Header */}
      <View className="px-6 mb-8 flex-row justify-between items-center">
        <TouchableOpacity onPress={() => router.back()}>
          <Ionicons name="close" size={28} color="#E5E7EB" />
        </TouchableOpacity>
        <View>
          <Text className="text-text-secondary text-xs font-bold uppercase tracking-widest">Organizador</Text>
          <Text className="text-text-primary text-3xl font-bold">{isEditing ? 'Editar Split' : 'Split Semanal'}</Text>
        </View>
        <TouchableOpacity className="w-12 h-12 bg-surface rounded-full items-center justify-center border-thin border-primary/20">
          <MaterialCommunityIcons name="auto-fix" size={24} color="#00E0A4" />
        </TouchableOpacity>
      </View>

      <ScrollView className="flex-1 px-6" showsVerticalScrollIndicator={false}>
        {/* Nome do Split */}
        <View className="mb-6">
          <TextInput
            placeholder="Nome do Split (ex: Push/Pull/Legs)"
            placeholderTextColor="#94A3B8"
            value={splitName}
            onChangeText={setSplitName}
            className="bg-surface text-text-primary p-4 rounded-premium border-thin border-stroke text-lg font-bold"
          />
        </View>

        {/* Descrição (opcional) */}
        <View className="mb-6">
          <TextInput
            placeholder="Descrição (opcional)"
            placeholderTextColor="#94A3B8"
            value={splitDescription}
            onChangeText={setSplitDescription}
            multiline
            numberOfLines={2}
            className="bg-surface text-text-primary p-4 rounded-premium border-thin border-stroke text-base"
          />
        </View>

        {/* IA Smart Suggestion Banner */}
        <MotiView
          from={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          className="bg-primary/10 border-thin border-primary/30 p-4 rounded-card mb-8 flex-row items-center"
        >
          <MaterialCommunityIcons name="star" size={24} color="#00E0A4" />
          <View className="ml-3 flex-1">
            <Text className="text-primary font-bold text-sm">Otimização IA Disponível</Text>
            <Text className="text-text-secondary text-xs">A IA pode reorganizar seu split baseado na sua recuperação muscular.</Text>
          </View>
          <TouchableOpacity className="bg-primary px-3 py-2 rounded-lg">
            <Text className="text-background font-bold text-xs">GERAR</Text>
          </TouchableOpacity>
        </MotiView>

          {/* Timeline Semanal */}
          <View className="mb-10">
            {DAYS_OF_WEEK.map((day, index) => {
              const workoutDayId = splitWorkoutDays.find(swd => swd.dayOfWeek === day.value)?.workoutDayId || 0;
              const workoutDayName = workoutDayId > 0 ? getWorkoutDayName(workoutDayId) : null;
              const isRest = workoutDayId === 0;
              const isSelected = selectedDayKey === day.key;

              return (
                <MotiView
                  key={day.key}
                  from={{ opacity: 0, translateX: -20 }}
                  animate={{ opacity: 1, translateX: 0 }}
                  transition={{ delay: index * 50 }}
                  className="flex-row items-center mb-4"
                >
                  <View className="w-14 items-center">
                    <Text className="text-text-secondary font-bold text-xs uppercase">{day.key}</Text>
                    <View className="w-[2px] h-10 bg-stroke my-1" />
                  </View>

                  <TouchableOpacity
                    onPress={() => handleDayPress(day.value, day.key)}
                    className={`flex-1 p-4 rounded-premium border-thin ${
                      isRest
                        ? 'bg-background border-dashed border-stroke'
                        : isSelected
                          ? 'bg-primary border-primary'
                          : 'bg-surface border-stroke'
                    }`}
                  >
                    <View className="flex-row justify-between items-center">
                      <View>
                        <Text className={`font-bold text-base ${
                          isRest ? 'text-text-secondary' : isSelected ? 'text-background' : 'text-text-primary'
                        }`}>
                          {isRest ? 'Dia de Descanso' : workoutDayName || 'Selecione um treino'}
                        </Text>
                        {!isRest && (
                          <Text className={`${isSelected ? 'text-primary/80' : 'text-primary'} text-xs mt-1`}>
                            {isSelected ? '✓ ' : ''}Clique para alterar
                          </Text>
                        )}
                      </View>
                      <Ionicons
                        name={isRest ? "moon-outline" : "barbell-outline"}
                        size={20}
                        color={isRest ? "#334155" : (isSelected ? "#020617" : "#00E0A4")}
                      />
                    </View>
                  </TouchableOpacity>
                </MotiView>
              );
            })}
          </View>

        {/* Seção de Workouts Disponíveis */}
        <Text className="text-text-secondary text-xs font-bold uppercase tracking-widest mb-4">Meus Treinos Criados</Text>
        {loadingWorkoutDays ? (
          <View className="items-center py-8">
            <ActivityIndicator size="large" color="#00E0A4" />
          </View>
        ) : workoutDays.length === 0 ? (
          <View className="items-center py-8">
            <MaterialCommunityIcons name="dumbbell" size={48} color="#334155" />
            <Text className="text-text-secondary mt-4 text-center px-4">
              Nenhum treino criado ainda
            </Text>
            <TouchableOpacity
              onPress={() => router.push('/workout/create')}
              className="bg-primary px-6 py-3 rounded-lg mt-4"
            >
              <Text className="text-background font-bold">Criar Primeiro Treino</Text>
            </TouchableOpacity>
          </View>
        ) : (
          <ScrollView horizontal showsHorizontalScrollIndicator={false} className="mb-12">
            {workoutDays.map((w) => (
              <TouchableOpacity
                key={w.id}
                className="bg-surface border-thin border-stroke p-4 rounded-premium mr-3 w-40"
              >
                <View className="w-8 h-1 rounded-full mb-3 bg-primary" />
                <Text className="text-text-primary font-bold mb-1">{w.name}</Text>
                <Text className="text-text-secondary text-[10px] uppercase">{w.exercises?.length || 0} exercícios</Text>
              </TouchableOpacity>
            ))}
            <TouchableOpacity
              onPress={() => router.push('/workout/create')}
              className="border-thin border-dashed border-stroke p-4 rounded-premium mr-3 w-40 items-center justify-center"
            >
              <Ionicons name="add" size={24} color="#94A3B8" />
              <Text className="text-text-secondary text-xs font-bold mt-2">Criar Novo</Text>
            </TouchableOpacity>
          </ScrollView>
        )}

      </ScrollView>

      {/* Botão de Finalizar Split */}
      <View className="px-6 pb-10">
        <TouchableOpacity
          onPress={handleSave}
          disabled={loading}
          className="bg-primary p-5 rounded-premium items-center shadow-lg shadow-primary/20"
        >
          {loading ? (
            <ActivityIndicator size="small" color="#020617" />
          ) : (
            <Text className="text-primary font-bold text-lg">{isEditing ? 'SALVAR ALTERAÇÕES' : 'CONFIRMAR PLANEJAMENTO'}</Text>
          )}
        </TouchableOpacity>
      </View>

      {/* Modal para Seleção de WorkoutDay */}
      <Modal
        visible={showWorkoutPicker !== null}
        transparent
        animationType="slide"
        onRequestClose={() => {
          setShowWorkoutPicker(null);
          setSelectedDayKey(null);
        }}
      >
        <View className="flex-1 bg-black/60 justify-end">
          <View className="bg-background rounded-t-3xl p-6 max-h-[80%]">
            <View className="flex-row justify-between items-center mb-4">
              <Text className="text-text-primary text-xl font-bold">
                Selecionar Treino para {showWorkoutPicker ? DAYS_OF_WEEK.find(d => d.value === showWorkoutPicker)?.key : 'hoje'}
              </Text>
              <TouchableOpacity onPress={() => {
                setShowWorkoutPicker(null);
                setSelectedDayKey(null);
              }}>
                <Ionicons name="close" size={28} color="#E5E7EB" />
              </TouchableOpacity>
            </View>

            {loadingWorkoutDays ? (
              <View className="items-center py-8">
                <ActivityIndicator size="large" color="#00E0A4" />
              </View>
            ) : workoutDays.length === 0 ? (
              <View className="items-center py-8">
                <MaterialCommunityIcons name="dumbbell" size={48} color="#334155" />
                <Text className="text-text-secondary mt-4 text-center px-4">
                  Nenhum treino criado ainda.
                </Text>
                <TouchableOpacity
                  onPress={() => router.push('/workout/create')}
                  className="bg-primary px-6 py-3 rounded-lg mt-4"
                >
                  <Text className="text-background font-bold">
                    Criar Primeiro Treino
                  </Text>
                </TouchableOpacity>
              </View>
            ) : (
              <ScrollView showsVerticalScrollIndicator={false}>
                {workoutDays.map((workoutDay) => (
                  <TouchableOpacity
                    key={workoutDay.id}
                    onPress={() => handleWorkoutSelect(workoutDay.id)}
                    className="bg-surface p-4 rounded-xl mb-3 border-thin border-stroke active:bg-primary/5"
                  >
                    <Text className="text-text-primary font-bold text-base">{workoutDay.name}</Text>
                    {workoutDay.description && (
                      <Text className="text-text-secondary text-sm mt-1">{workoutDay.description}</Text>
                    )}
                    <Text className="text-primary text-xs mt-2">{workoutDay.exercises?.length || 0} exercícios</Text>
                  </TouchableOpacity>
                ))}

                {/* Opção de Dia de Descanso */}
                <TouchableOpacity
                  onPress={handleRestDay}
                  className="bg-surface/50 p-4 rounded-xl mb-3 border-thin border-dashed border-stroke"
                >
                  <View className="flex-row items-center">
                    <Ionicons name="moon-outline" size={24} color="#334155" />
                    <Text className="text-text-secondary font-bold ml-3">
                      Dia de Descanso
                    </Text>
                  </View>
                </TouchableOpacity>
              </ScrollView>
            )}

            {/* Botão para criar novo treino */}
            {!loadingWorkoutDays && (
              <TouchableOpacity
                onPress={() => {
                  setShowWorkoutPicker(null);
                  setSelectedDayKey(null);
                  router.push('/workout/create');
                }}
                className="bg-primary p-4 rounded-xl items-center border-thin border-primary/20"
              >
                <Ionicons name="add" size={20} color="#020617" />
                <Text className="text-background font-bold ml-2">
                  Criar Novo Treino
                </Text>
              </TouchableOpacity>
            )}
          </View>
        </View>
      </Modal>
    </View>
  );
}