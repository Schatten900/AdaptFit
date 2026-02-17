import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  ActivityIndicator,
  TextInput,
  Alert,
} from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useRouter } from "expo-router";
import { BackButton } from "~/commons/components/BackButton";
import {
  workoutSessionStorage,
  WorkoutSessionData,
} from "~/commons/utils/workoutSessionStorage";
import { useCreateFeedback } from "~/features/feedback/hooks/useFeedback";

const FATIGUE_LEVELS = [
  { value: 1, label: "Muito Baixo", emoji: "🔋" },
  { value: 2, label: "Baixo", emoji: "🔋" },
  { value: 3, label: "Moderado", emoji: "⚡" },
  { value: 4, label: "Moderado+", emoji: "⚡" },
  { value: 5, label: "Alto", emoji: "🔥" },
  { value: 6, label: "Alto+", emoji: "🔥" },
  { value: 7, label: "Muito Alto", emoji: "💪" },
  { value: 8, label: "Extremo", emoji: "💪" },
  { value: 9, label: "Exausto", emoji: "🤯" },
  { value: 10, label: "Total", emoji: "😵" },
];

const SORENESS_LEVELS = [
  { value: 1, label: "Nenhuma", emoji: "😌" },
  { value: 2, label: "Mínima", emoji: "😌" },
  { value: 3, label: "Leve", emoji: "🙂" },
  { value: 4, label: "Leve+", emoji: "🙂" },
  { value: 5, label: "Moderada", emoji: "😐" },
  { value: 6, label: "Moderada+", emoji: "😐" },
  { value: 7, label: "Forte", emoji: "😣" },
  { value: 8, label: "Forte+", emoji: "😣" },
  { value: 9, label: "Intensa", emoji: "😖" },
  { value: 10, label: "Máxima", emoji: "😫" },
];

export default function FeedbackScreen() {
  const router = useRouter();
  const [sessionData, setSessionData] = useState<WorkoutSessionData | null>(null);
  const [loading, setLoading] = useState(true);
  const [fatigueLevel, setFatigueLevel] = useState<number>(5);
  const [muscleSoreness, setMuscleSoreness] = useState<number>(5);
  const [notes, setNotes] = useState<string>("");

  const createFeedback = useCreateFeedback();

  useEffect(() => {
    const loadSession = async () => {
      const data = await workoutSessionStorage.getSession();
      setSessionData(data);
      setLoading(false);
    };
    loadSession();
  }, []);

  const handleSubmit = async () => {
    if (!sessionData?.id) {
      Alert.alert("Erro", "Sessão de treino não encontrada");
      return;
    }

    try {
      await createFeedback.mutateAsync({
        workoutSessionId: parseInt(sessionData.id, 10),
        fatigueLevel,
        muscleSoreness,
        notes: notes.trim() || undefined,
      });

      await workoutSessionStorage.clearSession();
      router.replace("/home");
    } catch (error) {
      Alert.alert("Erro", "Falha ao enviar feedback. Tente novamente.");
    }
  };

  const handleSkip = async () => {
    await workoutSessionStorage.clearSession();
    router.replace("/home");
  };

  if (loading) {
    return (
      <View className="flex-1 bg-background justify-center items-center">
        <ActivityIndicator size="large" color="#00E0A4" />
      </View>
    );
  }

  return (
    <View className="flex-1 bg-background pt-12">
      <View className="px-6 mb-4">
        <BackButton />
      </View>

      <ScrollView
        className="flex-1"
        showsVerticalScrollIndicator={false}
        keyboardShouldPersistTaps="handled"
      >
        <View className="px-6">
          <Text className="text-text-primary text-2xl font-bold mb-2">
            Como você se sente?
          </Text>
          <Text className="text-text-secondary mb-6">
            Seu feedback ajuda a IA a entender melhor seu corpo e adaptar os
            treinos.
          </Text>

          <View className="mb-8">
            <Text className="text-text-primary font-bold text-lg mb-4">
              Nível de Fadiga
            </Text>
            <View className="flex-row flex-wrap justify-between">
              {FATIGUE_LEVELS.map((level) => (
                <TouchableOpacity
                  key={level.value}
                  onPress={() => setFatigueLevel(level.value)}
                  className={`w-[18%] aspect-square rounded-lg items-center justify-center mb-2 ${
                    fatigueLevel === level.value
                      ? "bg-primary"
                      : "bg-surface border-thin border-stroke"
                  }`}
                >
                  <Text className="text-xl">{level.emoji}</Text>
                  <Text
                    className={`text-[8px] mt-1 ${
                      fatigueLevel === level.value
                        ? "text-primary font-bold"
                        : "text-text-secondary"
                    }`}
                    numberOfLines={1}
                  >
                    {level.value}
                  </Text>
                </TouchableOpacity>
              ))}
            </View>
            <Text className="text-text-secondary text-center mt-2 text-sm">
              {FATIGUE_LEVELS.find((l) => l.value === fatigueLevel)?.label}
            </Text>
          </View>

          <View className="mb-8">
            <Text className="text-text-primary font-bold text-lg mb-4">
              Dor Muscular
            </Text>
            <View className="flex-row flex-wrap justify-between">
              {SORENESS_LEVELS.map((level) => (
                <TouchableOpacity
                  key={level.value}
                  onPress={() => setMuscleSoreness(level.value)}
                  className={`w-[18%] aspect-square rounded-lg items-center justify-center mb-2 ${
                    muscleSoreness === level.value
                      ? "bg-primary"
                      : "bg-surface border-thin border-stroke"
                  }`}
                >
                  <Text className="text-xl">{level.emoji}</Text>
                  <Text
                    className={`text-[8px] mt-1 ${
                      muscleSoreness === level.value
                        ? "text-primary font-bold"
                        : "text-text-secondary"
                    }`}
                    numberOfLines={1}
                  >
                    {level.value}
                  </Text>
                </TouchableOpacity>
              ))}
            </View>
            <Text className="text-text-secondary text-center mt-2 text-sm">
              {SORENESS_LEVELS.find((l) => l.value === muscleSoreness)?.label}
            </Text>
          </View>

          <View className="mb-8">
            <Text className="text-text-primary font-bold text-lg mb-4">
              Observações (opcional)
            </Text>
            <View className="bg-surface border-thin border-stroke rounded-premium p-4">
              <TextInput
                className="text-text-primary text-base min-h-[100px] text-start"
                placeholder="Como foi o treino? Alguma dor específica? Como você está se sentindo?"
                placeholderTextColor="#64748B"
                multiline
                value={notes}
                onChangeText={setNotes}
                textAlignVertical="top"
              />
            </View>
          </View>

          <View className="mb-12 gap-y-3">
            <TouchableOpacity
              onPress={handleSubmit}
              disabled={createFeedback.isPending}
              className="bg-primary p-5 rounded-premium items-center justify-center flex-row shadow-lg shadow-primary/20"
            >
              {createFeedback.isPending ? (
                <ActivityIndicator size="small" color="#020617" />
              ) : (
                <>
                  <Text className="text-primary font-bold text-lg mr-2">
                    ENVIAR FEEDBACK
                  </Text>
                  <Ionicons name="send" size={20} color="#020617" />
                </>
              )}
            </TouchableOpacity>

            <TouchableOpacity
              onPress={handleSkip}
              className="p-4 rounded-premium border-thin border-stroke items-center"
            >
              <Text className="text-text-secondary font-semibold">
                Pular por agora
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </ScrollView>
    </View>
  );
}
