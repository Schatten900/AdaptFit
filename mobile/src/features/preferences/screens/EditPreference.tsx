import React, { useState, useRef, useEffect } from 'react';
import { View, Text, TouchableOpacity, ScrollView, Dimensions, TextInput, Alert } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import { MotiView } from 'moti';
import { useMyPreference } from '../hooks/useMyPreferences';
import { useSetPreference } from '../hooks/useSetPreference';

const { width } = Dimensions.get('window');

const EQUIPMENT_OPTIONS = [
  { value: 'GYM', label: 'Academia' },
  { value: 'HOME', label: 'Casa' },
  { value: 'CROSSFIT', label: 'Crossfit' },
];

const INJURY_OPTIONS = [
  { value: 'KNEE_PAIN', label: 'Dor no Joelho' },
  { value: 'LOWER_BACK_PAIN', label: 'Dor Lombar' },
  { value: 'SHOULDER_PAIN', label: 'Dor no Ombro' },
  { value: 'ELBOW_PAIN', label: 'Dor no Cotovelo' },
  { value: 'WRIST_PAIN', label: 'Dor no Punho' },
  { value: 'NECK_PAIN', label: 'Dor no Pescoço' },
  { value: 'DISC_HERNIA', label: 'Hérnia de Disco' },
  { value: 'TENDONITIS', label: 'Tendinite' },
];

export default function EditPreference() {
  const router = useRouter();
  const { data: preferences, isLoading } = useMyPreference(true);
  const { setPreference, loading } = useSetPreference();
  const scrollRef = useRef<ScrollView>(null);

  const [selectedEquipment, setSelectedEquipment] = useState<string[]>([]);
  const [selectedInjuries, setSelectedInjuries] = useState<string[]>([]);
  const [customInjuries, setCustomInjuries] = useState<string[]>([]);
  const [customInput, setCustomInput] = useState('');
  const [currentIndex, setCurrentIndex] = useState(0);
  const [hasLoaded, setHasLoaded] = useState(false);

  useEffect(() => {
    if (preferences && !hasLoaded) {
      setHasLoaded(true);
      if (preferences.availableEquipment) {
        setSelectedEquipment(preferences.availableEquipment);
      }
      if (preferences.injuries) {
        const predefined = preferences.injuries.filter(
          (i) => INJURY_OPTIONS.some((o) => o.value === i)
        );
        const custom = preferences.injuries.filter(
          (i) => !INJURY_OPTIONS.some((o) => o.value === i)
        );
        setSelectedInjuries(predefined);
        setCustomInjuries(custom);
      }
    }
  }, [preferences, hasLoaded]);

  const toggleEquipment = (value: string) => {
    setSelectedEquipment((prev) =>
      prev.includes(value)
        ? prev.filter((v) => v !== value)
        : [...prev, value]
    );
  };

  const toggleInjury = (value: string) => {
    setSelectedInjuries((prev) =>
      prev.includes(value)
        ? prev.filter((v) => v !== value)
        : [...prev, value]
    );
  };

  const addCustomInjury = () => {
    const text = customInput.trim().toUpperCase();
    if (!text) return;
    if (!customInjuries.includes(text)) {
      setCustomInjuries((prev) => [...prev, text]);
    }
    setCustomInput('');
  };

  const removeCustomInjury = (value: string) => {
    setCustomInjuries((prev) => prev.filter((v) => v !== value));
  };

  const steps = [
    {
      id: 'equipment',
      title: 'Equipamentos Disponíveis',
      subtitle: 'Selecione onde você treina',
    },
    {
      id: 'injuries',
      title: 'Lesões e Restrições',
      subtitle: 'Selecione e adicione suas lesões',
    },
    {
      id: 'summary',
      title: 'Resumo',
      subtitle: 'Confirme suas preferências',
    },
  ];

  const nextStep = async () => {
    if (currentIndex < steps.length - 1) {
      scrollRef.current?.scrollTo({ x: (currentIndex + 1) * width, animated: true });
      setCurrentIndex(currentIndex + 1);
    } else {
      const allInjuries = [...selectedInjuries, ...customInjuries];

      await setPreference({
        availableEquipment: selectedEquipment,
        injuries: allInjuries,
      });

      router.replace('/home');
    }
  };

  const prevStep = () => {
    if (currentIndex > 0) {
      scrollRef.current?.scrollTo({ x: (currentIndex - 1) * width, animated: true });
      setCurrentIndex(currentIndex - 1);
    } else {
      router.back();
    }
  };

  const renderStep = (item: typeof steps[0]) => {
    if (item.id === 'equipment') {
      return (
        <View className="w-full gap-y-3">
          {EQUIPMENT_OPTIONS.map((opt) => {
            const isSelected = selectedEquipment.includes(opt.value);
            return (
              <TouchableOpacity
                key={opt.value}
                onPress={() => toggleEquipment(opt.value)}
                className={`w-full p-4 rounded-premium border flex-row items-center ${
                  isSelected
                    ? 'border-primary bg-primary/10'
                    : 'border-stroke bg-background'
                }`}
              >
                <View
                  className={`w-6 h-6 rounded-md border-2 items-center justify-center mr-3 ${
                    isSelected ? 'border-primary bg-primary' : 'border-stroke'
                  }`}
                >
                  {isSelected && <Ionicons name="checkmark" size={16} color="#020617" />}
                </View>
                <Text
                  className={`font-bold ${
                    isSelected ? 'text-primary' : 'text-text-secondary'
                  }`}
                >
                  {opt.label}
                </Text>
              </TouchableOpacity>
            );
          })}
        </View>
      );
    }

    if (item.id === 'injuries') {
      return (
        <View className="w-full">
          <Text className="text-text-secondary text-sm mb-3 font-semibold">
            Lesões predefinidas:
          </Text>
          <View className="flex-row flex-wrap gap-2 mb-4">
            {INJURY_OPTIONS.map((opt) => {
              const isSelected = selectedInjuries.includes(opt.value);
              return (
                <TouchableOpacity
                  key={opt.value}
                  onPress={() => toggleInjury(opt.value)}
                  className={`px-3 py-2 rounded-full border ${
                    isSelected
                      ? 'border-status-error bg-status-error/10'
                      : 'border-stroke bg-background'
                  }`}
                >
                  <Text
                    className={`text-xs font-semibold ${
                      isSelected ? 'text-status-error' : 'text-text-secondary'
                    }`}
                  >
                    {opt.label}
                  </Text>
                </TouchableOpacity>
              );
            })}
          </View>

          <Text className="text-text-secondary text-sm mb-2 font-semibold">
            Adicionar outra lesão:
          </Text>
          <View className="flex-row items-center gap-2 mb-3">
            <TextInput
              className="flex-1 bg-background border border-stroke rounded-premium p-3 text-text-primary"
              placeholder="Ex: Fratura no braço"
              placeholderTextColor="#94A3B8"
              value={customInput}
              onChangeText={setCustomInput}
              onSubmitEditing={addCustomInjury}
            />
            <TouchableOpacity
              onPress={addCustomInjury}
              className="bg-[#3B82F6] w-12 h-12 rounded-full items-center justify-center active:opacity-70"
            >
              <Ionicons name="add" size={24} color="#FFFFFF" />
            </TouchableOpacity>
          </View>

          {customInjuries.length > 0 && (
            <View className="flex-row flex-wrap gap-2">
              {customInjuries.map((inj) => (
                <TouchableOpacity
                  key={inj}
                  onPress={() => removeCustomInjury(inj)}
                  className="px-3 py-2 rounded-full border border-status-error/50 bg-status-error/10 flex-row items-center gap-1"
                >
                  <Text className="text-status-error text-xs font-semibold">{inj}</Text>
                  <Ionicons name="close-circle" size={14} color="#EF4444" />
                </TouchableOpacity>
              ))}
            </View>
          )}
        </View>
      );
    }

    return (
      <View className="w-full">
        <View className="bg-surface p-5 rounded-card border-thin border-stroke mb-4">
          <Text className="text-text-secondary text-sm uppercase tracking-widest mb-3 font-semibold">
            Equipamentos
          </Text>
          {selectedEquipment.length > 0 ? (
            <View className="flex-row flex-wrap gap-2">
              {selectedEquipment.map((eq) => (
                <View key={eq} className="bg-background px-3 py-1.5 rounded-full border border-primary/50">
                  <Text className="text-primary text-xs font-semibold">
                    {EQUIPMENT_OPTIONS.find((o) => o.value === eq)?.label || eq}
                  </Text>
                </View>
              ))}
            </View>
          ) : (
            <Text className="text-text-secondary italic text-sm">Nenhum selecionado</Text>
          )}
        </View>

        <View className="bg-surface p-5 rounded-card border-thin border-stroke mb-4">
          <Text className="text-text-secondary text-sm uppercase tracking-widest mb-3 font-semibold">
            Lesões
          </Text>
          {selectedInjuries.length > 0 || customInjuries.length > 0 ? (
            <View className="flex-row flex-wrap gap-2">
              {selectedInjuries.map((inj) => (
                <View key={inj} className="bg-background px-3 py-1.5 rounded-full border border-status-error/50">
                  <Text className="text-status-error text-xs font-semibold">
                    {INJURY_OPTIONS.find((o) => o.value === inj)?.label || inj}
                  </Text>
                </View>
              ))}
              {customInjuries.map((inj) => (
                <View key={inj} className="bg-background px-3 py-1.5 rounded-full border border-status-error/50">
                  <Text className="text-status-error text-xs font-semibold">{inj}</Text>
                </View>
              ))}
            </View>
          ) : (
            <Text className="text-text-secondary italic text-sm">Nenhuma registrada</Text>
          )}
        </View>

        <TouchableOpacity
          onPress={nextStep}
          disabled={loading}
          className="bg-[#3B82F6] w-full p-4 rounded-premium items-center active:opacity-70"
        >
          <Text className="text-white font-bold text-lg">
            {loading ? 'SALVANDO...' : 'SALVAR PREFERÊNCIAS'}
          </Text>
        </TouchableOpacity>
      </View>
    );
  };

  if (isLoading) {
    return (
      <View className="flex-1 items-center justify-center bg-background">
        <Text className="text-text-secondary italic">Carregando...</Text>
      </View>
    );
  }

  return (
    <View className="flex-1 bg-background pt-12">
      <View className="px-6 flex-row items-center justify-between h-14 mb-4">
        <TouchableOpacity
          onPress={prevStep}
          className="w-10 h-10 items-center justify-center rounded-full bg-surface border-thin border-stroke"
        >
          <Ionicons name="arrow-back" size={20} color="#00E0A4" />
        </TouchableOpacity>

        <View className="flex-row gap-x-1">
          {steps.map((_, i) => (
            <MotiView
              key={i}
              animate={{
                width: i === currentIndex ? 24 : 8,
                backgroundColor: i === currentIndex ? '#3B82F6' : '#1E293B',
              }}
              className="h-1.5 rounded-full"
            />
          ))}
        </View>
        <View className="w-10" />
      </View>

      <View className="px-6 mt-4 mb-4">
        <Text className="text-text-primary text-3xl font-bold">{steps[currentIndex].title}</Text>
        <Text className="text-text-secondary">{steps[currentIndex].subtitle}</Text>
      </View>

      <ScrollView
        ref={scrollRef}
        horizontal
        pagingEnabled
        scrollEnabled={false}
        showsHorizontalScrollIndicator={false}
        style={{ flex: 1 }}
      >
        {steps.map((item) => (
          <View key={item.id} style={{ width }} className="px-6">
            <MotiView
              from={{ opacity: 0, scale: 0.9, translateY: 20 }}
              animate={{ opacity: 1, scale: 1, translateY: 0 }}
              transition={{ type: 'timing', duration: 500 }}
              className="bg-surface p-8 rounded-card border-thin border-stroke"
            >
              {renderStep(item)}

              {item.id !== 'summary' && (
                <TouchableOpacity
                  onPress={nextStep}
                  className="bg-[#3B82F6] mt-8 w-full p-4 rounded-premium items-center active:opacity-70"
                >
                  <Text className="text-white font-bold text-lg">CONTINUAR</Text>
                </TouchableOpacity>
              )}
            </MotiView>
          </View>
        ))}
      </ScrollView>

      <View className="mb-12" />
    </View>
  );
}
