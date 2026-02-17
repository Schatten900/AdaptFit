import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  KeyboardAvoidingView,
  Platform,
  ActivityIndicator,
  ScrollView,
  Alert,
} from 'react-native';
import { Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { MotiView } from 'moti';
import { useRouter } from 'expo-router';

import { useWorkoutDayEditor } from '../hooks/workoutDay/useWorkoutDayEditor';
import { ExercisePickerModal } from '../components/ExercisePickerModal';
import { ExerciseDTO } from '../hooks/Exercise/useWorkoutExercise';
import { WorkoutExerciseUI } from '../types/WorkoutExercise/ui';
import { WorkoutDayResponse } from '~/types/workoutDay/types';

interface CreateWorkoutScreenProps {
  workoutDayId?: number;
  initialWorkoutDayData?: WorkoutDayResponse | null;
}

export default function CreateWorkoutScreen({ workoutDayId, initialWorkoutDayData }: CreateWorkoutScreenProps) {
  const router = useRouter();

  const {
    isEditing,
    loading,
    workoutDayName,
    setWorkoutDayName,
    workoutDayDescription,
    setWorkoutDayDescription,
    exercises,
    loadWorkoutDay,
    addExercise,
    removeExercise,
    saveWorkoutDay,
  } = useWorkoutDayEditor(workoutDayId);

  const [showExercisePicker, setShowExercisePicker] = useState(false);

  useEffect(() => {
    if (initialWorkoutDayData) {
      loadWorkoutDay(initialWorkoutDayData);
    }
  }, [initialWorkoutDayData]);

  const handleRemoveExercise = (exerciseId: number) => {
    removeExercise(exerciseId);
  };

  const handleSave = async () => {
    if (!workoutDayName.trim()) {
      Alert.alert('Erro', 'Por favor, insira um nome para o treino');
      return;
    }

    if (exercises.length === 0) {
      Alert.alert('Erro', 'Por favor, adicione pelo menos um exercício');
      return;
    }

    await saveWorkoutDay();
    router.back();
  };

  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      className="flex-1 bg-background pt-12"
    >
      {/* HEADER */}
      <View className="px-6 flex-row justify-between items-center mb-6">
        <TouchableOpacity
          onPress={() => router.back()}
          className="p-2 bg-surface rounded-full border-thin border-stroke"
        >
          <Ionicons name="close" size={24} color="#E5E7EB" />
        </TouchableOpacity>

        <Text className="text-text-primary text-xl font-bold">
          {isEditing ? 'Editar Treino' : 'Novo Treino'}
        </Text>

        <TouchableOpacity
          onPress={handleSave}
          disabled={loading || !workoutDayName}
          className={`px-4 py-2 rounded-full ${
            workoutDayName
              ? 'bg-primary'
              : 'bg-surface border-thin border-stroke'
          }`}
        >
          {loading ? (
            <ActivityIndicator size="small" color="#020617" />
          ) : (
            <Text
              className={
                workoutDayName
                  ? 'text-primary font-bold'
                  : 'text-text-secondary'
              }
            >
              {isEditing ? 'Salvar' : 'Salvar'}
            </Text>
          )}
        </TouchableOpacity>
      </View>

      <ScrollView className="flex-1 px-6" showsVerticalScrollIndicator={false}>
        {/* NOME DO TREINO */}
        <View className="mb-6">
          <TextInput
            placeholder="Ex: Peito e Tríceps"
            placeholderTextColor="#94A3B8"
            value={workoutDayName}
            onChangeText={setWorkoutDayName}
            className="bg-surface text-text-primary p-5 rounded-premium border-thin border-stroke text-lg font-bold"
          />
        </View>

        {/* DESCRIÇÃO (opcional) */}
        <View className="mb-6">
          <TextInput
            placeholder="Descrição (opcional)"
            placeholderTextColor="#94A3B8"
            value={workoutDayDescription}
            onChangeText={setWorkoutDayDescription}
            multiline
            numberOfLines={3}
            className="bg-surface text-text-primary p-5 rounded-premium border-thin border-stroke text-base"
          />
        </View>

        {/* LISTA DE EXERCÍCIOS */}
        <View className="flex-1">
          <View className="flex-row justify-between items-center mb-4">
            <Text className="text-text-secondary text-xs uppercase">
              Exercícios ({exercises.length})
            </Text>

            <TouchableOpacity
              onPress={() => setShowExercisePicker(true)}
              className="flex-row items-center"
            >
              <Ionicons name="add-circle" size={20} color="#00E0A4" />
              <Text className="text-primary font-bold ml-1">
                Buscar
              </Text>
            </TouchableOpacity>
          </View>

          {exercises.length === 0 ? (
            <MotiView className="flex-1 items-center justify-center py-12">
              <MaterialCommunityIcons
                name="weight-lifter"
                size={48}
                color="#1E293B"
              />
              <Text className="text-text-secondary mt-4">
                Nenhum exercício adicionado
              </Text>
            </MotiView>
          ) : (
            <>
              {exercises.map((item, index) => (
                <View key={item.exerciseId} className="bg-surface p-4 rounded-xl mb-3 flex-row justify-between items-center">
                  <View className="flex-1">
                    <Text className="text-text-primary font-bold mb-1">
                      {index + 1}. {item.name}
                    </Text>
                    <View className="flex-row space-x-4">
                      <Text className="text-text-secondary text-xs">
                        {item.sets} x {item.reps}
                      </Text>
                      {item.weight && item.weight > 0 && (
                        <Text className="text-text-secondary text-xs">
                          {item.weight}kg
                        </Text>
                      )}
                    </View>
                  </View>

                  <TouchableOpacity
                    onPress={() => handleRemoveExercise(item.exerciseId)}
                    className="p-2"
                  >
                    <Ionicons
                      name="trash-outline"
                      size={20}
                      color="#EF4444"
                    />
                  </TouchableOpacity>
                </View>
              ))}
            </>
          )}
        </View>
      </ScrollView>

      {/* MODAL DE EXERCÍCIOS */}
      <ExercisePickerModal
        visible={showExercisePicker}
        onClose={() => setShowExercisePicker(false)}
        onSelect={(exercise: ExerciseDTO) => {
          const exerciseId = Number(exercise.id);

          if (exercises.some(e => e.exerciseId === exerciseId)) {
            Alert.alert('Atenção', 'Este exercício já foi adicionado');
            return;
          }

          addExercise({
            exerciseId,
            name: exercise.name,
            sets: 3,
            reps: 10,
            weight: 0,
            restTimeSeconds: 60,
            exerciseOrder: exercises.length,
          });

          setShowExercisePicker(false);
        }}
      />
    </KeyboardAvoidingView>
  );
}
