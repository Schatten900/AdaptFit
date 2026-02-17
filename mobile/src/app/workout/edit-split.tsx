import { useEffect, useState } from 'react';
import { useLocalSearchParams } from 'expo-router';
import CreateSplitScreen from '../../features/workout/screens/createSplit';
import { splitService } from '../../features/workout/services/WorkoutSplitService';
import { SplitResponse } from '~/types/workoutSplit/types';
import { ActivityIndicator, View } from 'react-native';

export default function EditSplitPage() {
  const { splitId } = useLocalSearchParams<{ splitId: string }>();
  const [splitData, setSplitData] = useState<SplitResponse | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSplit = async () => {
        if (splitId) {
          const data = await splitService.getById(Number(splitId));
          setSplitData(data);
        }
      setLoading(false);
    };

    fetchSplit();
  }, [splitId]);

  if (loading) {
    return (
      <View className="flex-1 bg-background justify-center items-center">
        <ActivityIndicator size="large" color="#00E0A4" />
      </View>
    );
  }

  return <CreateSplitScreen splitId={Number(splitId)} initialSplitData={splitData} />;
}
