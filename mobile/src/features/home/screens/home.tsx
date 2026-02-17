import {
  View,
  Text,
  ActivityIndicator,
  ScrollView,
  RefreshControl,
  TouchableOpacity,
} from "react-native";
import { useCallback } from "react";
import { useMyProfile } from "../../profile/hooks/useMyProfile";
import { useRouter } from "expo-router";
import { Ionicons, MaterialCommunityIcons } from "@expo/vector-icons";

import {
  mapApiProfileToForm,
  formatGoal,
  formatExperience,
  calculateBMI,
} from "../utils/profileMapper";

export default function HomeScreen() {
  const { data: apiProfile, isLoading, refetch } = useMyProfile();
  const router = useRouter();

  const onRefresh = useCallback(() => {
    refetch();
  }, [refetch]);

  const profile = apiProfile ? mapApiProfileToForm(apiProfile) : null;
  const imc = calculateBMI(profile?.weight ?? null, profile?.height ?? null);

  // ⏳ Loading inicial
  if (isLoading && !profile) {
    return (
      <View className="flex-1 bg-background items-center justify-center">
        <ActivityIndicator size="large" color="#00E0A4" />
        <Text className="mt-4 text-text-secondary font-sans italic">
          Sincronizando dados biofísicos...
        </Text>
      </View>
    );
  }

  return (
    <ScrollView
      className="flex-1 bg-background"
      refreshControl={
        <RefreshControl
          refreshing={isLoading}
          onRefresh={onRefresh}
          tintColor="#00E0A4"
        />
      }
    >
      <View className="p-6 pt-12">
        {/* HEADER */}
        <View className="flex-row justify-between items-center mb-8">
          <View>
            <Text className="text-text-secondary text-sm font-sans uppercase tracking-widest">
              Performance
            </Text>
            <Text className="text-text-primary text-3xl font-bold">
              Dashboard
            </Text>
          </View>

          <TouchableOpacity className="w-12 h-12 bg-surface rounded-full items-center justify-center border-thin border-stroke">
            <Ionicons
              name="notifications-outline"
              size={24}
              color="#E5E7EB"
            />
          </TouchableOpacity>
        </View>

        {/* INSIGHT IA */}
        <View className="bg-surface border-thin border-primary/30 p-5 rounded-card mb-6">
          <View className="flex-row items-center mb-3">
            <MaterialCommunityIcons name="robot" size={20} color="#00E0A4" />
            <Text className="text-primary font-bold ml-2 tracking-tighter">
              INSIGHT DA IA
            </Text>
          </View>

          <Text className="text-text-primary leading-6">
            Seu IMC de{" "}
            <Text className="font-bold text-primary">
              {imc ?? "—"}
            </Text>{" "}
            indica boa evolução. Com base no seu objetivo de{" "}
            <Text className="italic text-primary">
              {formatGoal(profile?.goal)}
            </Text>
            , sugerimos aumentar a intensidade cardiovascular hoje.
          </Text>
        </View>

        {/* MÉTRICAS */}
        <View className="flex-row flex-wrap justify-between">
          <View className="w-[48%] bg-surface p-4 rounded-premium border-thin border-stroke mb-4">
            <Text className="text-text-secondary text-xs font-semibold mb-1">
              PESO
            </Text>
            <Text className="text-text-primary text-xl font-bold">
              {profile?.weight ?? "—"}{" "}
              <Text className="text-sm font-normal text-text-secondary">
                kg
              </Text>
            </Text>
          </View>

          <View className="w-[48%] bg-surface p-4 rounded-premium border-thin border-stroke mb-4">
            <Text className="text-text-secondary text-xs font-semibold mb-1">
              ALTURA
            </Text>
            <Text className="text-text-primary text-xl font-bold">
              {profile?.height ?? "—"}{" "}
              <Text className="text-sm font-normal text-text-secondary">
                cm
              </Text>
            </Text>
          </View>
        </View>

        {/* STATUS */}
        <View className="bg-surface rounded-card p-5 border-thin border-stroke mb-6">
          <View className="flex-row justify-between items-center border-b border-stroke pb-4 mb-4">
            <Text className="text-text-secondary font-semibold">
              Nível
            </Text>
            <View className="bg-background px-3 py-1 rounded-full border border-primary/50">
              <Text className="text-primary font-bold uppercase text-xs">
                {formatExperience(profile?.experience)}
              </Text>
            </View>
          </View>

          <View className="flex-row justify-between items-center">
            <Text className="text-text-secondary font-semibold">
              Foco Atual
            </Text>
            <Text className="text-text-primary font-bold capitalize">
              {formatGoal(profile?.goal)}
            </Text>
          </View>
        </View>

        {/* AÇÕES */}
        <View className="gap-y-3">
          <TouchableOpacity
            onPress={() => router.push("/workout/create")}
            className="bg-primary p-4 rounded-premium flex-row justify-center items-center"
          >
            <Ionicons name="add-circle-outline" size={20} color="#020617" />
            <Text className="text-primary font-bold ml-2">
              CRIAR TREINO
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            onPress={() => router.push("/workout/create-split")}
            className="bg-primary p-4 rounded-premium flex-row justify-center items-center"
          >
            <Ionicons name="calendar-outline" size={20} color="#020617" />
            <Text className="text-primary font-bold ml-2">
              CRIAR SPLIT
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            onPress={() => router.push("/workout/list")}
            className="bg-primary p-4 rounded-premium flex-row justify-center items-center"
          >
            <Ionicons name="play-circle-outline" size={20} color="#020617" />
            <Text className="text-primary font-bold ml-2">
              COMEÇAR TREINO
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            onPress={() => router.push("/feedback/history")}
            className="p-4 rounded-premium border-thin border-primary items-center"
          >
            <Text className="text-primary font-semibold">
              VER FEEDBACKS
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            onPress={() => router.push("/edit-profile")}
            className="p-4 rounded-premium border-thin border-primary items-center"
          >
            <Text className="text-primary font-semibold">
              AJUSTAR PERFIL
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            onPress={async () => {
              const { authStorage } = await import(
                "~/commons/utils/authStorage"
              );
              await authStorage.clear();
              router.replace("/auth/login");
            }}
            className="p-4 rounded-premium border-thin border-status-error/50 items-center"
          >
            <Text className="text-status-error font-semibold">
              Encerrar Sessão
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    </ScrollView>
  );
}
