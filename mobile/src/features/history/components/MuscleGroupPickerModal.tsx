import React from 'react';
import { View, Text, TouchableOpacity, Modal, ScrollView } from 'react-native';
import { Ionicons } from '@expo/vector-icons';

interface MuscleGroup {
  id: string;
  name: string;
  icon: string;
}

const MUSCLE_GROUPS: MuscleGroup[] = [
  { id: 'peito', name: 'Peito', icon: 'fitness' },
  { id: 'costas', name: 'Costas', icon: 'body' },
  { id: 'pernas', name: 'Pernas', icon: 'walk' },
  { id: 'ombros', name: 'Ombros', icon: 'accessibility' },
  { id: 'bracos', name: 'Braços', icon: 'hand-left' },
  { id: 'abdomen', name: 'Abdômen', icon: 'remove' },
  { id: 'gluteos', name: 'Glúteos', icon: 'ellipse' },
];

interface MuscleGroupPickerModalProps {
  visible: boolean;
  onClose: () => void;
  onSelect: (muscleGroup: string | undefined) => void;
  selectedValue?: string;
}

export function MuscleGroupPickerModal({
  visible,
  onClose,
  onSelect,
  selectedValue,
}: MuscleGroupPickerModalProps) {
  const handleSelect = (muscleGroup: string) => {
    onSelect(muscleGroup);
    onClose();
  };

  const handleClear = () => {
    onSelect(undefined);
    onClose();
  };

  return (
    <Modal visible={visible} animationType="slide" transparent>
      <View className="flex-1 bg-black/50 justify-end">
        <View className="bg-background rounded-t-2xl p-6 max-h-[70%]">
          <View className="flex-row justify-between items-center mb-4">
            <Text className="text-text-primary text-xl font-bold">
              Selecionar Grupo Muscular
            </Text>
            <TouchableOpacity onPress={onClose}>
              <Ionicons name="close" size={24} color="#E5E7EB" />
            </TouchableOpacity>
          </View>

          {selectedValue && (
            <TouchableOpacity
              onPress={handleClear}
              className="bg-surface p-3 rounded-xl mb-3 border border-primary"
            >
              <Text className="text-primary text-center font-semibold">
                ✨ Todos os Grupos Musculares
              </Text>
            </TouchableOpacity>
          )}

          <ScrollView showsVerticalScrollIndicator={false}>
            {MUSCLE_GROUPS.map((group) => {
              const isSelected = selectedValue === group.id;
              return (
                <TouchableOpacity
                  key={group.id}
                  onPress={() => handleSelect(group.id)}
                  className={`p-4 rounded-xl mb-2 flex-row items-center ${
                    isSelected ? 'bg-primary/20 border border-primary' : 'bg-surface border border-stroke'
                  }`}
                >
                  <Ionicons
                    name={group.icon as any}
                    size={24}
                    color={isSelected ? '#00E0A4' : '#9CA3AF'}
                  />
                  <Text
                    className={`ml-3 font-semibold ${
                      isSelected ? 'text-primary' : 'text-text-primary'
                    }`}
                  >
                    {group.name}
                  </Text>
                  {isSelected && (
                    <Ionicons name="checkmark-circle" size={24} color="#00E0A4" className="ml-auto" />
                  )}
                </TouchableOpacity>
              );
            })}
          </ScrollView>
        </View>
      </View>
    </Modal>
  );
}

export function getMuscleGroupName(id: string): string {
  return MUSCLE_GROUPS.find(g => g.id === id)?.name || id;
}
