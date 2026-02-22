import React, { useState, useEffect } from 'react';
import { View, Text, TextInput, TouchableOpacity, Modal, ActivityIndicator, ScrollView } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { workoutDayService } from '../../workout/services/WorkoutDayService';

interface WorkoutOption {
  id: number;
  name: string;
}

interface WorkoutPickerModalProps {
  visible: boolean;
  onClose: () => void;
  onSelect: (workoutId: number | undefined) => void;
  selectedValue?: number;
}

export function WorkoutPickerModal({
  visible,
  onClose,
  onSelect,
  selectedValue,
}: WorkoutPickerModalProps) {
  const [search, setSearch] = useState('');
  const [workouts, setWorkouts] = useState<WorkoutOption[]>([]);
  const [loading, setLoading] = useState(false);
  const [filteredWorkouts, setFilteredWorkouts] = useState<WorkoutOption[]>([]);

  useEffect(() => {
    if (visible) {
      fetchWorkouts();
    }
  }, [visible]);

  useEffect(() => {
    if (search.trim()) {
      const filtered = workouts.filter((w) =>
        w.name.toLowerCase().includes(search.toLowerCase())
      );
      setFilteredWorkouts(filtered);
    } else {
      setFilteredWorkouts(workouts);
    }
  }, [search, workouts]);

  const fetchWorkouts = async () => {
    setLoading(true);
    setWorkouts([]);
    
    try {
      const response = await workoutDayService.getAll();
      
      const workoutOptions: WorkoutOption[] = response.map((wd: any) => ({
        id: wd.id,
        name: wd.name,
      }));
      
      setWorkouts(workoutOptions);
      setFilteredWorkouts(workoutOptions);
    } catch (error) {
      console.error('Erro ao buscar workouts:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSelect = (workoutId: number) => {
    onSelect(workoutId);
    onClose();
  };

  const handleClear = () => {
    onSelect(undefined);
    onClose();
  };

  const renderItem = (item: WorkoutOption) => {
    const isSelected = selectedValue === item.id;
    return (
      <TouchableOpacity
        key={item.id}
        onPress={() => handleSelect(item.id)}
        className={`p-4 rounded-xl mb-2 flex-row items-center ${
          isSelected ? 'bg-primary/20 border border-primary' : 'bg-surface border border-stroke'
        }`}
      >
        <Ionicons
          name="barbell"
          size={20}
          color={isSelected ? '#00E0A4' : '#9CA3AF'}
        />
        <Text
          className={`ml-3 font-semibold flex-1 ${
            isSelected ? 'text-primary' : 'text-text-primary'
          }`}
        >
          {item.name}
        </Text>
        {isSelected && (
          <Ionicons name="checkmark-circle" size={24} color="#00E0A4" />
        )}
      </TouchableOpacity>
    );
  };

  return (
    <Modal visible={visible} animationType="slide" transparent>
      <View className="flex-1 bg-black/50 justify-end">
        <View className="bg-background rounded-t-2xl p-6 max-h-[70%]">
          <View className="flex-row justify-between items-center mb-4">
            <Text className="text-text-primary text-xl font-bold">
              Selecionar Treino
            </Text>
            <TouchableOpacity onPress={onClose}>
              <Ionicons name="close" size={24} color="#E5E7EB" />
            </TouchableOpacity>
          </View>

          <TextInput
            placeholder="Buscar por nome do treino"
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
                ✨ Todos os Treinos
              </Text>
            </TouchableOpacity>
          )}

          {loading ? (
            <ActivityIndicator size="large" color="#00E0A4" className="py-8" />
          ) : (
            <ScrollView showsVerticalScrollIndicator={false} className="flex-1">
              {filteredWorkouts.length === 0 ? (
                <Text className="text-text-secondary text-center py-8">
                  Nenhum treino encontrado{'\n'}Complete um treino primeiro!
                </Text>
              ) : (
                filteredWorkouts.map(renderItem)
              )}
            </ScrollView>
          )}
        </View>
      </View>
    </Modal>
  );
}
