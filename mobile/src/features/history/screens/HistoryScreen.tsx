import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  ActivityIndicator,
} from 'react-native';
import { useState, useCallback } from 'react';
import { useRouter } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { useHistory, useDashboard } from '../hooks/useHistory';
import { HistoricoResponse, DashboardResponse } from '~/types/history/types';
import { MuscleGroupPickerModal, getMuscleGroupName } from '../components/MuscleGroupPickerModal';
import { ExercisePickerModal } from '../components/ExercisePickerModal';
import { WorkoutPickerModal } from '../components/WorkoutPickerModal';

type PeriodFilter = 'week' | 'month' | 'year';
type MetricType = 'duration' | 'volume' | 'weight' | 'reps';

const METRICS = [
  { key: 'duration', label: 'Tempo (min)', icon: 'time' },
  { key: 'volume', label: 'Volume (kg)', icon: 'fitness' },
  { key: 'weight', label: 'Peso (kg)', icon: 'barbell' },
  { key: 'reps', label: 'Reps', icon: 'repeat' },
];

export default function HistoryScreen() {
  const router = useRouter();
  const [period, setPeriod] = useState<PeriodFilter>('month');
  const [muscleGroup, setMuscleGroup] = useState<string | undefined>(undefined);
  const [exerciseId, setExerciseId] = useState<number | undefined>(undefined);
  const [workoutId, setWorkoutId] = useState<number | undefined>(undefined);
  const [metric, setMetric] = useState<MetricType>('volume');

  const [showMuscleGroupModal, setShowMuscleGroupModal] = useState(false);
  const [showExerciseModal, setShowExerciseModal] = useState(false);
  const [showWorkoutModal, setShowWorkoutModal] = useState(false);

  const { data: history, isLoading: isLoadingHistory } = useHistory(period, muscleGroup, exerciseId, workoutId);
  const { data: dashboard, isLoading: isLoadingDashboard } = useDashboard(period, muscleGroup, exerciseId, workoutId);

  const handlePeriodChange = useCallback((newPeriod: PeriodFilter) => {
    setPeriod(newPeriod);
  }, []);

  const handleMuscleGroupSelect = useCallback((value: string | undefined) => {
    setMuscleGroup(value);
    setExerciseId(undefined);
  }, []);

  const handleExerciseSelect = useCallback((value: number | undefined) => {
    setExerciseId(value);
  }, []);

  const handleWorkoutSelect = useCallback((value: number | undefined) => {
    setWorkoutId(value);
  }, []);

  const clearAllFilters = useCallback(() => {
    setMuscleGroup(undefined);
    setExerciseId(undefined);
    setWorkoutId(undefined);
  }, []);

  const hasActiveFilters = muscleGroup || exerciseId || workoutId;

  const renderFilterButton = (filter: PeriodFilter, label: string) => {
    const isActive = period === filter;
    return (
      <TouchableOpacity
        key={filter}
        onPress={() => handlePeriodChange(filter)}
        className={`px-4 py-2 rounded-full mr-2 ${
          isActive ? 'bg-primary' : 'bg-surface border border-stroke'
        }`}
      >
        <Text
          className={`text-sm font-semibold ${
            isActive ? 'text-primary' : 'text-text-secondary'
          }`}
        >
          {label}
        </Text>
      </TouchableOpacity>
    );
  };

  const renderFilterSelector = (
    label: string,
    value: string | undefined,
    onPress: () => void,
    icon: string
  ) => (
    <TouchableOpacity
      onPress={onPress}
      className="bg-surface border border-stroke px-4 py-2.5 rounded-xl flex-row items-center flex-1 mr-2"
    >
      <Ionicons name={icon as any} size={16} color={value ? '#00E0A4' : '#9CA3AF'} />
      <Text
        className={`ml-2 text-sm font-medium flex-1 ${
          value ? 'text-primary' : 'text-text-secondary'
        }`}
        numberOfLines={1}
      >
        {value || label}
      </Text>
      <Ionicons name="chevron-down" size={16} color="#9CA3AF" />
    </TouchableOpacity>
  );

  const renderDashboard = (data: DashboardResponse) => {
    const pieData = Object.entries(data.sessionsByMuscleGroup || {}).map(
      ([name, value], index) => ({
        name,
        value,
        color: getChartColor(index),
        legendFontColor: '#9CA3AF',
        legendFontSize: 12,
      })
    );

    const lineData = (data.evolutionData || []).map((point) => ({
      period: point.period,
      sessions: point.sessionsCount,
      duration: point.totalDuration,
      volume: point.totalVolume || 0,
      weight: point.totalWeight || 0,
      reps: point.totalReps || 0,
    }));

    const distributionData = Object.entries(data.workoutDistribution || {}).map(
      ([name, value], index) => ({
        name,
        value,
        color: getChartColor(index),
        legendFontColor: '#9CA3AF',
        legendFontSize: 12,
      })
    );

    const getMetricValue = (item: typeof lineData[0]) => {
      switch (metric) {
        case 'volume': return item.volume;
        case 'weight': return item.weight;
        case 'reps': return item.reps;
        default: return item.duration;
      }
    };

    const getMetricLabel = () => {
      return METRICS.find(m => m.key === metric)?.label || 'Tempo (min)';
    };

    return (
      <View className="mb-6">
        <Text className="text-text-primary text-lg font-bold mb-4">
          Dashboard
        </Text>

        <View className="flex-row flex-wrap justify-between mb-4">
          <View className="w-[48%] bg-surface p-4 rounded-premium border border-stroke mb-3">
            <Text className="text-text-secondary text-xs font-semibold">
              Total de Treinos
            </Text>
            <Text className="text-text-primary text-2xl font-bold">
              {data.totalSessions || 0}
            </Text>
          </View>
          <View className="w-[48%] bg-surface p-4 rounded-premium border border-stroke mb-3">
            <Text className="text-text-secondary text-xs font-semibold">
              Tempo Total
            </Text>
            <Text className="text-text-primary text-2xl font-bold">
              {data.totalDurationMinutes || 0}
              <Text className="text-sm font-normal text-text-secondary"> min</Text>
            </Text>
          </View>
          <View className="w-[48%] bg-surface p-4 rounded-premium border border-stroke mb-3">
            <Text className="text-text-secondary text-xs font-semibold">
              Volume Total
            </Text>
            <Text className="text-text-primary text-2xl font-bold">
              {Math.round(data.totalVolume || 0)}
              <Text className="text-sm font-normal text-text-secondary"> kg</Text>
            </Text>
          </View>
          <View className="w-[48%] bg-surface p-4 rounded-premium border border-stroke">
            <Text className="text-text-secondary text-xs font-semibold">
              Reps Totais
            </Text>
            <Text className="text-text-primary text-2xl font-bold">
              {data.totalReps || 0}
            </Text>
          </View>
        </View>

        <View className="flex-row mb-3">
          {METRICS.map((m) => (
            <TouchableOpacity
              key={m.key}
              onPress={() => setMetric(m.key as MetricType)}
              className={`mr-2 px-3 py-1.5 rounded-full ${
                metric === m.key ? 'bg-primary' : 'bg-surface border border-stroke'
              }`}
            >
              <Text
                className={`text-xs font-semibold ${
                  metric === m.key ? 'text-primary' : 'text-text-secondary'
                }`}
              >
                {m.label}
              </Text>
            </TouchableOpacity>
          ))}
        </View>

        {pieData.length > 0 && (
          <View className="bg-surface p-4 rounded-premium border border-stroke mb-4">
            <Text className="text-text-secondary text-xs font-semibold mb-3">
              Por Grupo Muscular
            </Text>
            {pieData.map((item, index) => (
              <View key={index} className="flex-row items-center mb-2">
                <View
                  style={{ backgroundColor: item.color }}
                  className="w-3 h-3 rounded-full mr-2"
                />
                <Text className="text-text-primary flex-1">{item.name}</Text>
                <Text className="text-text-secondary">{item.value}</Text>
              </View>
            ))}
          </View>
        )}

        {lineData.length > 0 && (
          <View className="bg-surface p-4 rounded-premium border border-stroke mb-4">
            <Text className="text-text-secondary text-xs font-semibold mb-3">
              Evolução - {getMetricLabel()}
            </Text>
            <View className="flex-row items-end h-32">
              {lineData.map((item, index) => {
                const maxValue = Math.max(...lineData.map((d) => getMetricValue(d)), 1);
                const value = getMetricValue(item);
                const height = (value / maxValue) * 100;
                return (
                  <View key={index} className="flex-1 items-center">
                    <View
                      style={{ height: `${Math.max(height, 5)}%` }}
                      className="w-6 bg-primary rounded-t"
                    />
                    <Text className="text-text-secondary text-xs mt-1">
                      {item.period}
                    </Text>
                  </View>
                );
              })}
            </View>
          </View>
        )}

        {distributionData.length > 0 && (
          <View className="bg-surface p-4 rounded-premium border border-stroke">
            <Text className="text-text-secondary text-xs font-semibold mb-3">
              Distribuição de Treinos (Pizza)
            </Text>
            {distributionData.map((item, index) => (
              <View key={index} className="flex-row items-center mb-2">
                <View
                  style={{ backgroundColor: item.color }}
                  className="w-3 h-3 rounded-full mr-2"
                />
                <Text className="text-text-primary flex-1">{item.name}</Text>
                <Text className="text-text-secondary">{item.value}x</Text>
              </View>
            ))}
          </View>
        )}
      </View>
    );
  };

  const renderHistoryItem = (item: HistoricoResponse) => {
    const date = new Date(item.sessionDate);
    const formattedDate = date.toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
    });

    return (
      <View
        key={item.sessionId}
        className="bg-surface p-4 rounded-premium border border-stroke mb-3"
      >
        <View className="flex-row justify-between items-start mb-2">
          <Text className="text-text-primary font-bold text-base flex-1">
            {item.workoutName || 'Treino'}
          </Text>
          <Text className="text-text-secondary text-sm">{formattedDate}</Text>
        </View>
        
        <View className="flex-row items-center mb-2">
          <Ionicons name="time-outline" size={14} color="#9CA3AF" />
          <Text className="text-text-secondary text-sm ml-1">
            {item.durationMinutes || 0} min
          </Text>
          {item.exercises && item.exercises.length > 0 && (
            <>
              <Ionicons name="fitness-outline" size={14} color="#9CA3AF" className="ml-3" />
              <Text className="text-text-secondary text-sm ml-1">
                {item.exercises.length} exercícios
              </Text>
            </>
          )}
        </View>

        {item.notes && (
          <Text className="text-text-secondary text-sm italic">
            {item.notes}
          </Text>
        )}

        {item.exercises && item.exercises.length > 0 && (
          <View className="mt-2 pt-2 border-t border-stroke">
            <Text className="text-text-secondary text-xs font-semibold mb-1">
              Exercícios:
            </Text>
            {item.exercises.slice(0, 3).map((ex, idx) => (
              <Text key={idx} className="text-text-secondary text-xs">
                • {ex.exerciseName} ({ex.sets}x{ex.reps})
              </Text>
            ))}
            {item.exercises.length > 3 && (
              <Text className="text-text-secondary text-xs italic">
                +{item.exercises.length - 3} mais...
              </Text>
            )}
          </View>
        )}
      </View>
    );
  };

  const isLoading = isLoadingHistory || isLoadingDashboard;

  return (
    <View className="flex-1 bg-background">
      <ScrollView className="flex-1" showsVerticalScrollIndicator={false}>
        <View className="p-6 pt-12">
          <View className="flex-row items-center justify-between mb-6">
            <View className="flex-row items-center">
              <TouchableOpacity
                onPress={() => router.back()}
                className="mr-3 p-2 -ml-2"
              >
                <Ionicons name="arrow-back" size={24} color="#E5E7EB" />
              </TouchableOpacity>
              <Text className="text-text-primary text-2xl font-bold">
                Histórico
              </Text>
            </View>
            {hasActiveFilters && (
              <TouchableOpacity
                onPress={clearAllFilters}
                className="bg-surface px-3 py-1.5 rounded-full border border-primary"
              >
                <Text className="text-primary text-xs font-semibold">
                  Limpar filtros
                </Text>
              </TouchableOpacity>
            )}
          </View>

          <View className="flex-row mb-4">
            {renderFilterButton('week', 'Semana')}
            {renderFilterButton('month', 'Mês')}
            {renderFilterButton('year', 'Ano')}
          </View>

          <View className="flex-row flex-wrap mb-4">
            {renderFilterSelector(
              'Grupo Muscular',
              muscleGroup ? getMuscleGroupName(muscleGroup) : undefined,
              () => setShowMuscleGroupModal(true),
              'body'
            )}
            {renderFilterSelector(
              'Exercício',
              undefined,
              () => setShowExerciseModal(true),
              'barbell'
            )}
            {renderFilterSelector(
              'Treino',
              undefined,
              () => setShowWorkoutModal(true),
              'clipboard'
            )}
          </View>

          {isLoading ? (
            <View className="flex-1 items-center justify-center py-20">
              <ActivityIndicator size="large" color="#00E0A4" />
              <Text className="text-text-secondary mt-4">Carregando...</Text>
            </View>
          ) : (
            <>
              {dashboard && renderDashboard(dashboard)}

              <Text className="text-text-primary text-lg font-bold mb-4">
                Lista de Treinos
              </Text>

              {history && history.length > 0 ? (
                history.map(renderHistoryItem)
              ) : (
                <View className="bg-surface p-6 rounded-premium border border-stroke items-center">
                  <Ionicons name="calendar-outline" size={48} color="#6B7280" />
                  <Text className="text-text-secondary mt-3 text-center">
                    Nenhum treino encontrado{hasActiveFilters ? ' com esses filtros.' : ' neste período.'}
                  </Text>
                  {!hasActiveFilters && (
                    <Text className="text-text-secondary text-sm mt-1">
                      Complete um treino para vê-lo aqui!
                    </Text>
                  )}
                </View>
              )}
            </>
          )}
        </View>
      </ScrollView>

      <MuscleGroupPickerModal
        visible={showMuscleGroupModal}
        onClose={() => setShowMuscleGroupModal(false)}
        onSelect={handleMuscleGroupSelect}
        selectedValue={muscleGroup}
      />

      <ExercisePickerModal
        visible={showExerciseModal}
        onClose={() => setShowExerciseModal(false)}
        onSelect={handleExerciseSelect}
        selectedValue={exerciseId}
      />

      <WorkoutPickerModal
        visible={showWorkoutModal}
        onClose={() => setShowWorkoutModal(false)}
        onSelect={handleWorkoutSelect}
        selectedValue={workoutId}
      />
    </View>
  );
}

function getChartColor(index: number): string {
  const colors = [
    '#00E0A4',
    '#3B82F6',
    '#F59E0B',
    '#EF4444',
    '#8B5CF6',
    '#EC4899',
    '#14B8A6',
    '#F97316',
  ];
  return colors[index % colors.length];
}
