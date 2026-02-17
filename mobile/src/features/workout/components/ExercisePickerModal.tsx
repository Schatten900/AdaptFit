import React from 'react';
import { View, Text, TextInput, TouchableOpacity, Modal, ActivityIndicator } from 'react-native';
import { FlashList } from '@shopify/flash-list';
import { useExercises, ExerciseDTO } from '../hooks/Exercise/useWorkoutExercise';

interface ExercisePickerModalProps {
  visible: boolean;
  onClose: () => void;
  onSelect: (exercise: ExerciseDTO) => void;
}

export function ExercisePickerModal({ visible, onClose, onSelect }: ExercisePickerModalProps) {
  const { exercisesDTO, loading, search } = useExercises();

  const renderItem = ({ item }: { item: ExerciseDTO }) => (
    <TouchableOpacity
      onPress={() => onSelect(item)}
      className="p-4 border-b border-stroke"
    >
      <Text className="text-text-primary font-bold">
        {item.name}
      </Text>
      <Text className="text-text-secondary text-xs">
        {item.muscleGroup}
      </Text>
    </TouchableOpacity>
  );

  return (
    <Modal visible={visible} animationType="slide">
      <View className="flex-1 bg-background p-6">

        <Text className="text-xl font-bold text-text-primary mb-4">
          Buscar Exercício
        </Text>

        <TextInput
          placeholder="Buscar por nome ou músculo"
          onChangeText={search}
          className="bg-surface p-4 rounded-xl mb-4 text-text-primary"
          placeholderTextColor="#94A3B8"
        />

        {loading ? (
          <ActivityIndicator size="large" />
        ) : (
          <FlashList<ExerciseDTO>
            data={exercisesDTO}
            keyExtractor={(item) => item.id}
            // @ts-ignore
            renderItem={({ item }: { item: ExerciseDTO }) => (
              <TouchableOpacity
                onPress={() => onSelect(item)}
                className="p-4 border-b border-stroke"
              >
                <Text className="text-text-primary font-bold">
                  {item.name}
                </Text>
                <Text className="text-text-secondary text-xs">
                  {item.muscleGroup}
                </Text>
              </TouchableOpacity>
            )}
            // @ts-ignore
            estimatedItemSize={60}
          />
        )}

        <TouchableOpacity onPress={onClose} className="mt-6">
          <Text className="text-center text-primary font-bold">
            Fechar
          </Text>
        </TouchableOpacity>
      </View>
    </Modal>
  );
}
