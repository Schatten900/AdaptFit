import { TouchableOpacity } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';

interface BackButtonProps {
  onPress?: () => void;
}

export function BackButton({ onPress }: BackButtonProps) {
  const router = useRouter();

  const handlePress = () => {
    if (onPress) {
      onPress();
    } else {
      router.back();
    }
  };

  return (
    <TouchableOpacity
      onPress={handlePress}
      className="w-10 h-10 items-center justify-center rounded-full bg-surface border-thin border-stroke"
    >
      <Ionicons name="arrow-back" size={20} color="#00E0A4" />
    </TouchableOpacity>
  );
}
