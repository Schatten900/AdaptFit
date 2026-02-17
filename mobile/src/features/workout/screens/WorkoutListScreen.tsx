import {useState} from 'react';
import { View, Text, TouchableOpacity, ActivityIndicator, ScrollView, Alert } from 'react-native';
import { Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import { useWorkoutsAndSplits } from '../hooks/Split/useWorkoutsAndSplits';
import { WorkoutDayResponse, SplitResponse } from '~/types';
import { useWorkoutToday } from '../hooks/Split/useWorkoutToday';
import { BackButton } from '~/commons/components/BackButton';

export default function WorkoutListScreen() {
  const router = useRouter();
  const { workoutDays, splits, loading, refetch } = useWorkoutsAndSplits();
  const [activeSplit, setActiveSplit] = useState<SplitResponse | null>(null);
  const { todayWorkout, loading: loadingToday } = useWorkoutToday();

  const renderWorkoutDay = (item: WorkoutDayResponse) => (
    <TouchableOpacity
      onPress={() => router.push(`/workout/session?workoutDayId=${item.id}`)}
      activeOpacity={0.85}
      className="bg-surface p-4 rounded-premium border-thin border-stroke mb-3"
    >
      <View className="flex-row justify-between items-start">
        <View className="flex-1">
          <Text className="text-text-primary font-bold text-lg">
            {item.name}
          </Text>

          {item.description && (
            <Text className="text-text-secondary text-sm mt-1">
              {item.description}
            </Text>
          )}

          <Text className="text-text-secondary text-xs mt-2">
            {item.exercises?.length || 0} exercícios
          </Text>
        </View>

        <TouchableOpacity
          onPress={() => router.push(`/workout/edit-workout-day?workoutDayId=${item.id}`)}
          className="ml-2 p-2"
        >
          <Ionicons name="create-outline" size={20} color="#94A3B8" />
        </TouchableOpacity>
      </View>
    </TouchableOpacity>
  );

  const renderSplit = (item: SplitResponse) => (
    <TouchableOpacity
      onPress={() => router.push(`/workout/edit-split?splitId=${item.id}`)}
      activeOpacity={0.85}
      className="bg-surface p-4 rounded-premium border-thin border-stroke mb-3"
    >
      <View className="flex-row justify-between items-start">
        <View className="flex-1">
          <Text className="text-text-primary font-bold text-lg">
            {item.name}
          </Text>

          {item.description && (
            <Text className="text-secondary text-sm mt-1">
              {item.description}
            </Text>
          )}

          <Text className="text-text-secondary text-xs mt-2">
            {item.splitWorkoutDays?.length || 0} dias configurados
          </Text>
        </View>

        <View className="flex-row items-center">
          {item.active && (
            <View className="bg-primary/10 px-2 py-1 rounded-full mr-2">
              <Text className="text-primary text-xs font-bold">ATIVO</Text>
            </View>
          )}
          <TouchableOpacity
            onPress={() => router.push(`/workout/edit-split?splitId=${item.id}`)}
            className="ml-2 p-2"
          >
            <Ionicons name="create-outline" size={20} color="#94A3B8" />
          </TouchableOpacity>
        </View>
      </View>
    </TouchableOpacity>
  );

  return (
    <View className="flex-1 bg-background pt-12">
      {/* Header */}
      <View className="px-6 flex-row justify-between items-center mb-4">
        <BackButton onPress={() => router.replace('/home')}/>
        <Text className="text-text-primary text-xl font-bold">Meus Treinos</Text>
        <TouchableOpacity
          onPress={() => router.push('/workout/create')}
          className="bg-primary p-3 rounded-full"
        >
          <Ionicons name="add" size={24} color="#020617" />
        </TouchableOpacity>
      </View>

      {/* Treino de Hoje */}
      {loadingToday ? (
        <View className="px-6 mb-4">
          <ActivityIndicator size="small" color="#00E0A4" />
        </View>
      ) : todayWorkout ? (
        <View className="px-6 mb-4">
          <TouchableOpacity
            onPress={() => router.push(`/workout/session?workoutDayId=${todayWorkout.id}`)}
            className="bg-primary p-4 rounded-premium border-thin border-primary/30 flex-row items-center"
          >
            <View className="flex-1">
              <Text className="text-primary font-bold text-lg">
                Treino de Hoje
              </Text>
              <Text className="text-primary/80 ml-2">
                {todayWorkout.name} • {todayWorkout.exercises?.length || 0} exercícios
              </Text>
            </View>
            <Ionicons name="play" size={20} color="#020617" />
          </TouchableOpacity>
        </View>
      ) : (
        <View className="px-6 mb-4">
          <TouchableOpacity
            onPress={() => router.push('/workout/create-split')}
            className="bg-surface/50 p-4 rounded-premium border-thin border-dashed border-stroke flex-row items-center"
          >
            <MaterialCommunityIcons name="calendar-plus" size={20} color="#00E0A4" />
            <Text className="text-text-secondary font-bold ml-3">
              Configure seu Split para ver treino de hoje
            </Text>
          </TouchableOpacity>
        </View>
      )}

      {/* Loading */}
      {loading && (
        <View className="flex-1 justify-center items-center">
          <ActivityIndicator size="large" color="#00E0A4" />
        </View>
      )}

      {/* Content */}
      {!loading && (
        <ScrollView className="flex-1" showsVerticalScrollIndicator={false}>
          {/* Splits Section */}
          {splits.length > 0 && (
            <View className="px-6 mb-6">
              <View className="flex-row items-center mb-4">
                <MaterialCommunityIcons name="calendar-week" size={20} color="#00E0A4" />
                <Text className="text-text-primary font-bold ml-2">Splits Semanais</Text>
              </View>

              {splits.map((split) => renderSplit(split))}
            </View>
          )}

          {/* Workout Days Section */}
          <View className="px-6 mb-6">
            <View className="flex-row items-center mb-4">
              <MaterialCommunityIcons name="dumbbell" size={20} color="#00E0A4" />
              <Text className="text-text-primary font-bold ml-2">Dias de Treino</Text>
            </View>

            {workoutDays.length > 0 ? (
              workoutDays.map((day) => renderWorkoutDay(day))
            ) : (
              <View className="items-center py-8">
                <Text className="text-text-secondary text-center">
                  Nenhum dia de treino criado ainda.
                </Text>
                <TouchableOpacity
                  onPress={() => router.push('/workout/create')}
                  className="mt-4 bg-primary px-6 py-3 rounded-premium"
                >
                  <Text className="text-background font-bold">
                    Criar Primeiro Treino
                  </Text>
                </TouchableOpacity>
              </View>
            )}
          </View>

          {/* Create Split Button */}
          <View className="px-6 pb-8">
            <TouchableOpacity
              onPress={() => router.push('/workout/create-split')}
              className="bg-primary p-4 rounded-premium items-center border-thin border-primary/20"
            >
              <MaterialCommunityIcons name="calendar-plus" size={20} color="#020617" />
              <Text className="text-background font-bold ml-2">
                Criar Novo Split
              </Text>
            </TouchableOpacity>
          </View>
        </ScrollView>
      )}
    </View>
  );
}