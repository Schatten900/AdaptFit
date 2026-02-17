import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  TextInput,
  KeyboardAvoidingView,
  Platform,
  Modal,
} from 'react-native';
import { Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { MotiView, AnimatePresence } from 'moti';
import { useRouter, useLocalSearchParams } from 'expo-router';

import { BackButton } from '~/commons/components/BackButton';
import { ExercisePickerModal } from '../components/ExercisePickerModal';

import { useWorkoutLoader } from '../hooks/workoutDay/useWorkoutLoader';
import { useWorkoutSessionEditor } from '../hooks/Session/useWorkoutSessionEditor';
import { useWorkoutSession } from '../hooks/Session/useWorkoutSession';

import { workoutSessionStorage } from '~/commons/utils/workoutSessionStorage';

interface SetItemProps {
  set: {
    id: string;
    kg: string;
    reps: string;
    done: boolean;
  };
  index: number;
  onRemove: () => void;
  onToggleDone: () => void;
}

function SetItem({ set, index, onRemove, onToggleDone }: SetItemProps) {
  return (
    <View className="flex-row items-center mb-2">
      <View className="flex-1">
        <View
          className={`flex-row items-center p-2 rounded-lg ${
            set.done ? 'bg-primary/5' : 'bg-surface'
          }`}
        >
          <Text className="flex-1 text-text-primary font-bold">
            {index + 1}
          </Text>

          <TextInput
            keyboardType="numeric"
            placeholder="0"
            className="flex-1 bg-background text-text-primary text-center py-2 rounded-md border border-stroke mx-1 font-bold"
          />

          <TextInput
            keyboardType="numeric"
            placeholder="0"
            className="flex-1 bg-background text-text-primary text-center py-2 rounded-md border border-stroke mx-1 font-bold"
          />

          <TouchableOpacity
            onPress={onToggleDone}
            className={`w-10 h-10 items-center justify-center rounded-full ml-2 ${
              set.done ? 'bg-primary' : 'bg-stroke'
            }`}
          >
            <Ionicons
              name="checkmark"
              size={20}
              color={set.done ? '#020617' : '#94A3B8'}
            />
          </TouchableOpacity>
        </View>
      </View>

      <TouchableOpacity
        onPress={onRemove}
        className="ml-2 p-2 bg-status-error/20 rounded-lg"
      >
        <Ionicons name="trash-outline" size={18} color="#EF4444" />
      </TouchableOpacity>
    </View>
  );
}

export default function SessionScreen() {
  const router = useRouter();
  const { workoutDayId } = useLocalSearchParams<{ workoutDayId: string }>();

  const {
    exercises,
    setExercises,
    loading,
  } = useWorkoutLoader(workoutDayId ? Number(workoutDayId) : undefined);

  // ⚠️ NÃO usamos removeExercise local
  const { addExercise } = useWorkoutSessionEditor(
    workoutDayId ? Number(workoutDayId) : undefined,
    exercises,
    setExercises
  );

  const { removeExercise } = useWorkoutSessionEditor(
    workoutDayId ? Number(workoutDayId) : undefined,
    exercises,
    setExercises
  );

  const { createSession } = useWorkoutSession();

  const [seconds, setSeconds] = useState(0);
  const [isResting, setIsResting] = useState(false);
  const [restTimer, setRestTimer] = useState(60);
  const [showExercisePicker, setShowExercisePicker] = useState(false);
  const [showExerciseOptions, setShowExerciseOptions] = useState(false);
  const [selectedExerciseId, setSelectedExerciseId] = useState<string | null>(
    null
  );

  useEffect(() => {
    const interval = setInterval(() => setSeconds(s => s + 1), 1000);
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    let interval: any;

    if (isResting && restTimer > 0) {
      interval = setInterval(() => setRestTimer(t => t - 1), 1000);
    }

    if (restTimer === 0) {
      setIsResting(false);
      setRestTimer(60);
    }

    return () => clearInterval(interval);
  }, [isResting, restTimer]);

  const formatTime = (sec: number) => {
    const m = Math.floor(sec / 60);
    const s = sec % 60;
    return `${m}:${String(s).padStart(2, '0')}`;
  };

  const toggleDone = (exId: string, setId: string) => {
    setExercises(prev =>
      prev.map(ex => {
        if (ex.id !== exId) return ex;

        return {
          ...ex,
          sets: ex.sets.map(s =>
            s.id === setId ? { ...s, done: !s.done } : s
          ),
        };
      })
    );

    setIsResting(true);
    setRestTimer(60);
  };

  const finishWorkout = async () => {
    const durationMinutes = Math.floor(seconds / 60);
    
    const payload = {
      workoutId: Number(workoutDayId),
      sessionDate: new Date().toISOString(),
      durationMinutes: durationMinutes,
    };

    await createSession(payload);
    await workoutSessionStorage.clearSession();

    router.push('/endWorkout');
  };

  if (loading) {
    return (
      <View className="flex-1 bg-background justify-center items-center">
        <Text className="text-text-secondary">Carregando treino...</Text>
      </View>
    );
  }

  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      className="flex-1 bg-background"
    >
      {/* HEADER */}
      <View className="pt-14 pb-4 px-6 bg-surface border-b border-stroke flex-row justify-between items-center">
        <BackButton onPress={() => router.replace('/workout/list')} />

        <View className="items-center">
          <Text className="text-text-secondary text-xs uppercase">
            Tempo de Treino
          </Text>
          <Text className="text-primary text-2xl font-bold">
            {formatTime(seconds)}
          </Text>
        </View>

        <TouchableOpacity
          onPress={finishWorkout}
          className="bg-status-success px-6 py-2 rounded-full"
        >
          <Text className="text-primary font-bold">FINALIZAR</Text>
        </TouchableOpacity>
      </View>

      {/* LISTA */}
      <ScrollView className="flex-1 px-4 pt-4">
        {exercises.map((ex, index) => (
          <MotiView
            key={ex.id}
            from={{ opacity: 0, translateY: 15 }}
            animate={{ opacity: 1, translateY: 0 }}
            transition={{ delay: index * 100 }}
            className="bg-surface rounded-card border-thin border-stroke p-4 mb-4"
          >
            <View className="flex-row justify-between items-center mb-3">
              <Text className="text-text-primary text-lg font-bold">
                {ex.name}
              </Text>

              <TouchableOpacity
                onPress={() => {
                  setSelectedExerciseId(ex.id);
                  setShowExerciseOptions(true);
                }}
              >
                <Ionicons
                  name="ellipsis-vertical"
                  size={22}
                  color="#E5E7EB"
                />
              </TouchableOpacity>
            </View>

            {ex.sets.map((set, i) => (
              <SetItem
                key={set.id}
                set={set}
                index={i}
                onRemove={() => {}}
                onToggleDone={() => toggleDone(ex.id, set.id)}
              />
            ))}
          </MotiView>
        ))}

        <TouchableOpacity
          onPress={() => setShowExercisePicker(true)}
          className="py-8 items-center"
        >
          <Ionicons name="add-circle-outline" size={24} color="#00E0A4" />
          <Text className="text-primary font-bold mt-2">
            Adicionar Exercício
          </Text>
        </TouchableOpacity>
      </ScrollView>

      <ExercisePickerModal
        visible={showExercisePicker}
        onClose={() => setShowExercisePicker(false)}
        onSelect={addExercise}
      />

      {/* MODAL DE OPÇÕES */}
      <Modal visible={showExerciseOptions} transparent animationType="fade">
        <View className="flex-1 bg-black/60 justify-end">
          <View className="bg-surface p-6 rounded-t-3xl">
            <TouchableOpacity
              onPress={async () => {
                if (!selectedExerciseId) return;

                await removeExercise(selectedExerciseId);

                setSelectedExerciseId(null);
                setShowExerciseOptions(false);
              }}
            >
              <Text className="text-status-error font-bold">
                Remover Exercício
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>

      <AnimatePresence>
        {isResting && (
          <MotiView
            from={{ translateY: 100, opacity: 0 }}
            animate={{ translateY: 0, opacity: 1 }}
            exit={{ translateY: 100, opacity: 0 }}
            className="absolute bottom-10 left-6 right-6 bg-primary p-4 rounded-card flex-row justify-between items-center"
          >
            <View className="flex-row items-center">
              <MaterialCommunityIcons
                name="timer-sand"
                size={24}
                color="#020617"
              />
              <Text className="ml-3 text-primary font-bold text-xl">
                {formatTime(restTimer)}
              </Text>
            </View>

            <TouchableOpacity
              onPress={() => {
                setIsResting(false);
                setRestTimer(60);
              }}
            >
              <Text className="text-primary font-bold">PULAR</Text>
            </TouchableOpacity>
          </MotiView>
        )}
      </AnimatePresence>
    </KeyboardAvoidingView>
  );
}
