import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity, Modal, ActivityIndicator, ScrollView } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import api from '~/config/api';

interface ExerciseOption {
  id: number;
  name: string;
  muscleGroup: string;
}

interface ExercisePickerModalProps {
  visible: boolean;
  onClose: () => void;
  onSelect: (exerciseId: number | undefined) => void;
  selectedValue?: number;
}

export function ExercisePickerModal({
  visible,
  onClose,
  onSelect,
  selectedValue,
}: ExercisePickerModalProps) {
  const [search, setSearch] = useState('');
  const [exercises, setExercises] = useState<ExerciseOption[]>([]);
  const [loading, setLoading] = useState(false);
  const [filteredExercises, setFilteredExercises] = useState<ExerciseOption[]>([]);

  useEffect(() => {
    if (visible) {
      fetchExercises();
    }
  }, [visible]);

  useEffect(() => {
    if (search.trim()) {
      const filtered = exercises.filter(
        (ex) =>
          ex.name.toLowerCase().includes(search.toLowerCase()) ||
          ex.muscleGroup.toLowerCase().includes(search.toLowerCase())
      );
      setFilteredExercises(filtered);
    } else {
      setFilteredExercises(exercises);
    }
  }, [search, exercises]);

  const fetchExercises = async () => {
    try {
      setLoading(true);
      const response = await api.get('/exercises');
      const data = response.map((ex: any) => ({
        id: ex.id,
        name: ex.name,
        muscleGroup: ex.muscleGroup,
      }));
      setExercises(data);
      setFilteredExercises(data);
    } catch (error) {
      console.error('Erro ao buscar exercícios:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSelect = (exerciseId: number) => {
    onSelect(exerciseId);
    onClose();
  };

  const handleClear = () => {
    onSelect(undefined);
    onClose();
  };

  const renderItem = ({ item }: { item: ExerciseOption }) => {
    const isSelected = selectedValue === item.id;
    return (
      <TouchableOpacity
        key={item.id}
        onPress={() => handleSelect(item.id)}
        className={`p-4 rounded-xl mb-2 flex-row items-center ${
          isSelected ? 'bg-primary/20 border border-primary' : 'bg-surface border border-stroke'
        }`}
      >
        <View className="flex-1">
          <Text className={`font-semibold ${isSelected ? 'text-primary' : 'text-text-primary'}`}>
            {item.name}
          </Text>
          <Text className="text-text-secondary text-xs">{item.muscleGroup}</Text>
        </View>
        {isSelected && (
          <Ionicons name="checkmark-circle" size={24} color="#00E0A4" />
        )}
      </TouchableOpacity>
    );
  };

  return (
    <Modal visible={visible} animationType="slide" transparent>
      <View className="flex-1 bg-black/50 justify-end">
        <View className="bg-background rounded-t-2xl p-6 max-h-[85%]">
          <View className="flex-row justify-between items-center mb-4">
            <Text className="text-text-primary text-xl font-bold">
              Selecionar Exercício
            </Text>
            <TouchableOpacity onPress={onClose}>
              <Ionicons name="close" size={24} color="#E5E7EB" />
            </TouchableOpacity>
          </View>

          <TextInput
            placeholder="Buscar por nome ou músculo"
            value={search}
            onChangeText={setSearch}
            className="bg-surface p-4 rounded-xl mb-4 text-text-primary"
            placeholderTextColor="#94A3B8"
          />

          {selectedValue && (
            <TouchableOpacity
              onPress={handleClear}
              className="bg-surface p-3 rounded-xl mb-3 border border-primary"
            >
              <Text className="text-primary text-center font-semibold">
                ✨ Todos os Exercícios
              </Text>
            </TouchableOpacity>
          )}

          {loading ? (
            <ActivityIndicator size="large" color="#00E0A4" className="py-8" />
          ) : (
            <ScrollView showsVerticalScrollIndicator={false} className="flex-1">
              {filteredExercises.length === 0 ? (
                <Text className="text-text-secondary text-center py-8">
                  Nenhum exercício encontrado
                </Text>
              ) : (
                filteredExercises.map((item) => renderItem({ item }))
              )}
            </ScrollView>
          )}
        </View>
      </View>
    </Modal>
  );
}
