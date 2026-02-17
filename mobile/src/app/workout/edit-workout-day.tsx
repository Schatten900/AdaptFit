import { useEffect, useState } from 'react';
import { useLocalSearchParams } from 'expo-router';
import CreateWorkoutScreen from '../../features/workout/screens/createWorkoutScreen';
import { workoutDayService } from '../../features/workout/services/WorkoutDayService';
import { WorkoutDayResponse } from '~/types/workoutDay/types';
import { ActivityIndicator, View } from 'react-native';

export default function EditWorkoutDayPage() {
  const { workoutDayId } = useLocalSearchParams<{ workoutDayId: string }>();
  const [workoutDayData, setWorkoutDayData] = useState<WorkoutDayResponse | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchWorkoutDay = async () => {
        if (workoutDayId) {
          const data = await workoutDayService.getById(Number(workoutDayId));
          setWorkoutDayData(data);
        }
      setLoading(false);
    };

    fetchWorkoutDay();
  }, [workoutDayId]);

  if (loading) {
    return (
      <View className="flex-1 bg-background justify-center items-center">
        <ActivityIndicator size="large" color="#00E0A4" />
      </View>
    );
  }

  return <CreateWorkoutScreen workoutDayId={Number(workoutDayId)} initialWorkoutDayData={workoutDayData} />;
}
