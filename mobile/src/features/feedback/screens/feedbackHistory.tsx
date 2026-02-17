import React from "react";
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  ActivityIndicator,
} from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useRouter } from "expo-router";
import { BackButton } from "~/commons/components/BackButton";
import { useFeedbacks } from "~/features/feedback/hooks/useFeedback";

export default function FeedbackHistoryScreen() {
  const router = useRouter();
  const { data: feedbacks, isLoading, error } = useFeedbacks();

  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString("pt-BR", {
      day: "2-digit",
      month: "short",
      year: "numeric",
    });
  };

  const getFatigueLabel = (level: number) => {
    if (level <= 3) return "Baixa";
    if (level <= 6) return "Moderada";
    return "Alta";
  };

  const getSorenessLabel = (level: number) => {
    if (level <= 3) return "Leve";
    if (level <= 6) return "Moderada";
    return "Intensa";
  };

  const getFatigueColor = (level: number) => {
    if (level <= 3) return "text-status-success";
    if (level <= 6) return "text-primary";
    return "text-status-error";
  };

  const getSorenessColor = (level: number) => {
    if (level <= 3) return "text-status-success";
    if (level <= 6) return "text-primary";
    return "text-status-error";
  };

  if (isLoading) {
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
      >
        <View className="px-6">
          <Text className="text-text-primary text-2xl font-bold mb-2">
            Histórico de Feedback
          </Text>
          <Text className="text-text-secondary mb-6">
            Acompanhe como seu corpo tem respondido aos treinos.
          </Text>

          {!feedbacks || feedbacks.length === 0 ? (
            <View className="bg-surface border-thin border-stroke rounded-premium p-8 items-center">
              <Ionicons name="document-text-outline" size={48} color="#64748B" />
              <Text className="text-text-secondary text-center mt-4">
                Nenhum feedback registrado ainda.
              </Text>
              <Text className="text-text-secondary text-center text-sm mt-2">
                Após cada treino, você pode avaliar como seu corpo está se sentindo.
              </Text>
            </View>
          ) : (
            feedbacks.map((feedback) => (
              <View
                key={feedback.id}
                className="bg-surface border-thin border-stroke rounded-premium p-4 mb-4"
              >
                <View className="flex-row justify-between items-center mb-3">
                  <Text className="text-text-secondary text-sm">
                    {formatDate(feedback.createdAt)}
                  </Text>
                  <View className="bg-primary/10 px-2 py-1 rounded-full">
                    <Text className="text-primary text-xs font-bold">
                      Treino #{feedback.workoutSessionId}
                    </Text>
                  </View>
                </View>

                <View className="flex-row justify-between mb-3">
                  <View className="flex-1">
                    <Text className="text-text-secondary text-xs uppercase mb-1">
                      Fadiga
                    </Text>
                    <View className="flex-row items-center">
                      <Text className={`text-xl font-bold ${getFatigueColor(feedback.fatigueLevel)}`}>
                        {feedback.fatigueLevel}
                      </Text>
                      <Text className={`text-sm ml-2 ${getFatigueColor(feedback.fatigueLevel)}`}>
                        • {getFatigueLabel(feedback.fatigueLevel)}
                      </Text>
                    </View>
                  </View>

                  <View className="flex-1">
                    <Text className="text-text-secondary text-xs uppercase mb-1">
                      Dor Muscular
                    </Text>
                    <View className="flex-row items-center">
                      <Text className={`text-xl font-bold ${getSorenessColor(feedback.muscleSoreness)}`}>
                        {feedback.muscleSoreness}
                      </Text>
                      <Text className={`text-sm ml-2 ${getSorenessColor(feedback.muscleSoreness)}`}>
                        • {getSorenessLabel(feedback.muscleSoreness)}
                      </Text>
                    </View>
                  </View>
                </View>

                {feedback.notes && (
                  <View className="border-t border-stroke pt-3 mt-1">
                    <Text className="text-text-secondary text-xs uppercase mb-1">
                      Observações
                    </Text>
                    <Text className="text-text-primary text-sm">
                      {feedback.notes}
                    </Text>
                  </View>
                )}
              </View>
            ))
          )}
        </View>
      </ScrollView>
    </View>
  );
}
