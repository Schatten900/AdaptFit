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
  Alert
} from 'react-native';
import { Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { MotiView, AnimatePresence } from 'moti';
import { useRouter, useLocalSearchParams } from 'expo-router';

import { BackButton } from '~/commons/components/BackButton';
import { ExercisePickerModal } from '../components/ExercisePickerModal';

import { useQuery } from "@tanstack/react-query";
import { useWorkoutLoader } from '../hooks/workoutDay/useWorkoutLoader';
import { workoutDayService } from '../services/WorkoutDayService';
import { workoutSessionService } from '../services/WorkoutSessionService';
import { WorkoutSessionResponse, ExercisePerformedResponse } from '~/types';
import { WorkoutExerciseVM } from '../types/WorkoutExercise/types';
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
  onChangeKg: (value: string) => void;
  onChangeReps: (value: string) => void;
  disabled?: boolean;
}

function SetItem({ set, index, onRemove, onToggleDone, onChangeKg, onChangeReps, disabled = false }: SetItemProps) {
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
            placeholder="kg"
            editable={!disabled}
            value={set.kg}
            onChangeText={onChangeKg}
            className="flex-1 bg-background text-text-primary text-center py-2 rounded-md border border-stroke mx-1 font-bold"
          />

          <TextInput
            keyboardType="numeric"
            placeholder="reps"
            editable={!disabled}
            value={set.reps}
            onChangeText={onChangeReps}
            className="flex-1 bg-background text-text-primary text-center py-2 rounded-md border border-stroke mx-1 font-bold"
          />

          <TouchableOpacity
            onPress={onToggleDone}
            disabled={disabled}
            className={`w-10 h-10 items-center justify-center rounded-full ml-2 ${
              set.done ? 'bg-primary' : 'bg-stroke'
            } ${disabled ? 'opacity-50' : ''}`}
          >
            <Ionicons
              name="checkmark"
              size={20}
              color={set.done ? '#00E0A4' : '#94A3B8'}
            />
          </TouchableOpacity>
        </View>
      </View>

      {!disabled && (
        <TouchableOpacity
          onPress={onRemove}
          className="ml-2 p-2 bg-status-error/20 rounded-lg"
        >
          <Ionicons name="trash-outline" size={18} color="#EF4444" />
        </TouchableOpacity>
      )}
    </View>
  );
}

export default function SessionScreen() {
  const router = useRouter();
  const { workoutDayId, completed } = useLocalSearchParams<{ workoutDayId: string; completed?: string }>();
  const isReadOnly = completed === 'true';

  const {
    exercises,
    setExercises,
    loading,
  } = useWorkoutLoader(workoutDayId ? Number(workoutDayId) : undefined);

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
  const { data: workoutDayData } = useQuery({
    queryKey: ["workout-day", workoutDayId],
    queryFn: () => workoutDayService.getById(Number(workoutDayId)),
    enabled: !!workoutDayId,
  });

  const { data: sessionData } = useQuery<WorkoutSessionResponse | null>({
    queryKey: ["workout-session-latest", workoutDayId],
    queryFn: () => workoutSessionService.getLatestByWorkoutDayId(Number(workoutDayId)),
    enabled: !!workoutDayId && isReadOnly,
  });

  const [sessionLoaded, setSessionLoaded] = useState(false);

  useEffect(() => {
    if (isReadOnly && sessionData?.exercises && exercises.length > 0 && !sessionLoaded) {
      const exerciseByName = new Map<string, ExercisePerformedResponse>();
      const exerciseById = new Map<number, ExercisePerformedResponse>();
      
      sessionData.exercises.forEach((se: ExercisePerformedResponse) => {
        exerciseByName.set(se.exerciseName, se);
        exerciseById.set(se.exerciseId, se);
      });

      const updatedExercises = exercises.map((ex: WorkoutExerciseVM) => {
        const sessionExercise = exerciseByName.get(ex.name) || exerciseById.get(ex.exerciseId);
        if (sessionExercise && sessionExercise.sets) {
          return {
            ...ex,
            sets: ex.sets.map((set, idx) => {
              const sessionSet = sessionExercise.sets[idx];
              return {
                ...set,
                kg: sessionSet ? String(sessionSet.weight || '') : '',
                reps: sessionSet ? String(sessionSet.reps || '') : '',
                done: true,
              };
            }),
          };
        }
        return ex;
      });
      setExercises(updatedExercises);
      setSessionLoaded(true);
    }
  }, [sessionData, isReadOnly]);

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

  const updateSetKg = (exId: string, setId: string, kg: string) => {
    setExercises(prev =>
      prev.map(ex => {
        if (ex.id !== exId) return ex;

        return {
          ...ex,
          sets: ex.sets.map(s =>
            s.id === setId ? { ...s, kg } : s
          ),
        };
      })
    );
  };

  const updateSetReps = (exId: string, setId: string, reps: string) => {
    setExercises(prev =>
      prev.map(ex => {
        if (ex.id !== exId) return ex;

        return {
          ...ex,
          sets: ex.sets.map(s =>
            s.id === setId ? { ...s, reps } : s
          ),
        };
      })
    );
  };

  const finishWorkout = async () => {
    const durationSeconds = seconds;
    
    let totalReps = 0;
    let totalWeight = 0;

    exercises.forEach(ex => {
      ex.sets.forEach(set => {
        if (set.done) {
          const reps = parseInt(set.reps || '0', 10);
          const kg = parseFloat(set.kg || '0');
          totalReps += reps;
          totalWeight += reps * kg;
        }
      });
    });

    const payload = {
      workoutDayId: Number(workoutDayId),
      sessionDate: new Date().toISOString(),
      durationMinutes: Math.floor(durationSeconds / 60),
      totalReps,
      totalWeight,
      totalVolume: totalWeight,
      exercises: exercises.map(ex => ({
        exerciseId: Number(ex.id),
        sets: ex.sets.filter(s => s.done).map((s, index) => ({
          setNumber: index + 1,
          reps: parseInt(s.reps || '0', 10),
          weight: parseFloat(s.kg || '0'),
          done: true,
        })),
      })).filter(ex => ex.sets.length > 0),
    };

    const sessionResponse = await createSession(payload);

    const completedSets = exercises.reduce((acc, ex) => {
      return acc + ex.sets.filter(s => s.done).length;
    }, 0);

    const totalSets = exercises.reduce((acc, ex) => acc + ex.sets.length, 0);

    const sessionData = {
      id: sessionResponse.id.toString(),
      workoutDayId: Number(workoutDayId),
      workoutDayName: workoutDayData?.name || 'Treino',
      duration: durationSeconds,
      totalSets,
      completedSets,
      totalReps,
      totalWeight,
      exercises: exercises.map(ex => {
        const exCompletedSets = ex.sets.filter(s => s.done);
        const exReps = exCompletedSets.reduce((sum, s) => sum + parseInt(s.reps || '0', 10), 0);
        const exWeight = exCompletedSets.reduce((sum, s) => sum + (parseFloat(s.kg || '0') * parseInt(s.reps || '0', 10)), 0);
        
        return {
          name: ex.name,
          sets: ex.sets.length,
          completedSets: exCompletedSets.length,
          reps: exReps,
          weight: exWeight,
        };
      }),
      finishedAt: new Date().toISOString(),
    };

    await workoutSessionStorage.saveSession(sessionData);

    router.push('/endWorkout');
  };

  const handleFinishWorkout = () => {
    const hasIncompleteSets = exercises.some(ex => 
      ex.sets.some(s => !s.done && (s.reps || s.kg))
    );

    if (hasIncompleteSets) {
      Alert.alert(
        "Séries Incompletas",
        "Você tem séries que não foram marcadas como concluídas. Deseja finalizar mesmo assim?",
        [
          { text: "Cancelar", style: "cancel" },
          { text: "Finalizar", onPress: () => finishWorkout() }
        ]
      );
    } else {
      finishWorkout();
    }
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
          {isReadOnly ? (
            <>
              <Text className="text-text-secondary text-xs uppercase">
                Treino Concluído
              </Text>
              <View className="flex-row items-center mt-1">
                <Ionicons name="checkmark-circle" size={16} color="#00E0A4" />
                <Text className="text-status-success text-sm font-bold ml-1">
                  Hoje
                </Text>
              </View>
            </>
          ) : (
            <>
              <Text className="text-text-secondary text-xs uppercase">
                Tempo de Treino
              </Text>
              <Text className="text-primary text-2xl font-bold">
                {formatTime(seconds)}
              </Text>
            </>
          )}
        </View>

        {isReadOnly ? (
          <View className="w-20" />
        ) : (
          <TouchableOpacity
            onPress={handleFinishWorkout}
            className="bg-status-success px-6 py-2 rounded-full"
          >
            <Text className="text-primary font-bold">FINALIZAR</Text>
          </TouchableOpacity>
        )}
      </View>

      {/* Mensagem modo somente leitura */}
      {isReadOnly && (
        <View className="bg-primary/10 px-6 py-3 border-b border-primary/20">
          <Text className="text-primary text-sm text-center">
            Este treino já foi realizado hoje. Visualização apenas para consulta.
          </Text>
        </View>
      )}

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

              {!isReadOnly && (
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
              )}
            </View>

            {ex.sets.map((set, i) => (
              <SetItem
                key={set.id}
                set={set}
                index={i}
                onRemove={() => {}}
                onToggleDone={() => isReadOnly ? {} : toggleDone(ex.id, set.id)}
                onChangeKg={(value) => isReadOnly ? {} : updateSetKg(ex.id, set.id, value)}
                onChangeReps={(value) => isReadOnly ? {} : updateSetReps(ex.id, set.id, value)}
                disabled={isReadOnly}
              />
            ))}
          </MotiView>
        ))}

        {!isReadOnly && (
          <TouchableOpacity
            onPress={() => setShowExercisePicker(true)}
            className="py-8 items-center"
          >
            <Ionicons name="add-circle-outline" size={24} color="#00E0A4" />
            <Text className="text-primary font-bold mt-2">
              Adicionar Exercício
            </Text>
          </TouchableOpacity>
        )}
      </ScrollView>

      <ExercisePickerModal
        visible={showExercisePicker && !isReadOnly}
        onClose={() => setShowExercisePicker(false)}
        onSelect={addExercise}
      />

      {/* MODAL DE OPÇÕES */}
      <Modal visible={showExerciseOptions && !isReadOnly} transparent animationType="fade">
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
        {isResting && !isReadOnly && (
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
                color="#00E0A4"
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
