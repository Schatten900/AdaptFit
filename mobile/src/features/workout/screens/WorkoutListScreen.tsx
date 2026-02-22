import { useState } from 'react';
import { View, Text, TouchableOpacity, ActivityIndicator, ScrollView } from 'react-native';
import { Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import { useWorkoutsAndSplits } from '../hooks/Split/useWorkoutsAndSplits';
import { WorkoutDayResponse, SplitResponse } from '~/types';
import { useNextWorkouts } from '../hooks/Split/useNextWorkouts';
import { BackButton } from '~/commons/components/BackButton';

const dayNames = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'];

export default function WorkoutListScreen() {
  const router = useRouter();
  const { workoutDays, splits, loading } = useWorkoutsAndSplits();
  const { data: nextWorkouts, isLoading: loadingNext } = useNextWorkouts();

  const getDayName = (dayOfWeek: number) => {
    return dayNames[dayOfWeek - 1] || '';
  };

  const getNextDayOfWeek = (current: number, offset: number) => {
    return ((current - 1 + offset) % 7) + 1;
  };

  const renderTodaySection = () => {
    if (loadingNext) {
      return (
        <View className="px-6 mb-4">
          <ActivityIndicator size="small" color="#00E0A4" />
        </View>
      );
    }

    if (!nextWorkouts) {
      return (
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
      );
    }

    const { todayWorkout, todayStatus, currentDayOfWeek } = nextWorkouts;

    if (todayStatus === 'REST') {
      return (
        <View className="px-6 mb-4">
          <View className="bg-surface p-4 rounded-premium border-thin border-primary/30 flex-row items-center">
            <MaterialCommunityIcons name="beach" size={24} color="#00E0A4" />
            <View className="flex-1 ml-3">
              <Text className="text-primary font-bold text-lg">
                Dia de Descanso
              </Text>
              <Text className="text-text-secondary text-sm">
                Aproveite para recuperar!
              </Text>
            </View>
          </View>
        </View>
      );
    }

    if (todayStatus === 'COMPLETED') {
      return (
        <View className="px-6 mb-4">
          <TouchableOpacity
            onPress={() => router.push(`/workout/session?workoutDayId=${todayWorkout?.id}&completed=true`)}
            className="bg-surface p-4 rounded-premium border-thin border-primary/30"
          >
            <View className="flex-row items-center mb-2">
              <Ionicons name="checkmark-circle" size={20} color="#00E0A4" />
              <Text className="text-primary font-bold text-lg ml-2">
                Treino Concluído
              </Text>
            </View>
            <View className="border-t border-stroke pt-2 mt-2">
              <Text className="text-text-primary font-semibold">
                {todayWorkout?.name}
              </Text>
              <Text className="text-text-secondary text-sm mt-1">
                {todayWorkout?.exercises?.length || 0} exercícios
              </Text>
            </View>
            <View className="mt-3 bg-primary/10 py-2 px-3 rounded-md">
              <Text className="text-primary text-sm font-semibold text-center">
                Ver Detalhes
              </Text>
            </View>
          </TouchableOpacity>
        </View>
      );
    }

    return (
      <View className="px-6 mb-4">
        <TouchableOpacity
          onPress={() => router.push(`/workout/session?workoutDayId=${todayWorkout?.id}`)}
          className="bg-primary p-4 rounded-premium border-thin border-primary/30 flex-row items-center"
        >
          <View className="flex-1">
            <Text className="text-primary font-bold text-lg">
              Treino de Hoje
            </Text>
            <Text className="text-primary/80">
              {todayWorkout?.name} • {todayWorkout?.exercises?.length || 0} exercícios
            </Text>
          </View>
          <Ionicons name="play" size={20} color="#020617" />
        </TouchableOpacity>
      </View>
    );
  };

  const renderNextWorkouts = () => {
    if (!nextWorkouts || !nextWorkouts.nextWorkouts || nextWorkouts.nextWorkouts.length === 0) {
      return null;
    }

    return (
      <View className="px-6 mb-6">
        <View className="flex-row items-center mb-4">
          <Ionicons name="calendar-outline" size={20} color="#00E0A4" />
          <Text className="text-text-primary font-bold ml-2">Próximos Treinos</Text>
        </View>

        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          {nextWorkouts.nextWorkouts.map((workout, index) => {
            const dayOfWeek = getNextDayOfWeek(nextWorkouts.currentDayOfWeek, index + 1);
            return (
              <TouchableOpacity
                key={workout.id}
                onPress={() => router.push(`/workout/session?workoutDayId=${workout.id}`)}
                className="bg-surface p-3 rounded-premium border-thin border-stroke mr-3 w-32"
              >
                <View className="bg-primary/10 px-2 py-1 rounded-full self-start mb-2">
                  <Text className="text-primary text-xs font-bold">
                    {getDayName(dayOfWeek)}
                  </Text>
                </View>
                <Text className="text-text-primary font-semibold text-sm" numberOfLines={2}>
                  {workout.name}
                </Text>
                <Text className="text-text-secondary text-xs mt-1">
                  {workout.exercises?.length || 0} exercícios
                </Text>
              </TouchableOpacity>
            );
          })}
        </ScrollView>
      </View>
    );
  };

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

      {/* Seção: Treino de Hoje */}
      {renderTodaySection()}

      {/* Seção: Próximos Treinos */}
      {renderNextWorkouts()}

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
                <Text className="text-text-primary font-bold ml-2">Splits</Text>
              </View>

              {splits.map((split) => renderSplit(split))}
            </View>
          )}

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
