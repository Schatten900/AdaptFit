import {
  View,
  Text,
  ActivityIndicator,
  ScrollView,
  RefreshControl,
  TouchableOpacity,
  Dimensions,
} from "react-native";
import { useCallback, useState } from "react";
import Animated, {
  useSharedValue,
  useAnimatedStyle,
  withSpring,
} from "react-native-reanimated";
import { useMyProfile } from "../../profile/hooks/useMyProfile";
import { useRouter } from "expo-router";
import { Ionicons, MaterialCommunityIcons } from "@expo/vector-icons";

import {
  mapApiProfileToForm,
  formatGoal,
  formatExperience,
  calculateBMI,
} from "../utils/profileMapper";

const { width: SCREEN_WIDTH } = Dimensions.get("window");
const SIDEBAR_WIDTH = SCREEN_WIDTH * 0.75;

type MenuItem = {
  label: string;
  icon: keyof typeof Ionicons.glyphMap;
  route?: string;
  color: string;
  bgColor?: string;
  border?: boolean;
  action?: () => void;
};

export default function HomeScreen() {
  const { data: apiProfile, isLoading, refetch } = useMyProfile();
  const router = useRouter();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const translateX = useSharedValue(-SIDEBAR_WIDTH);
  const overlayOpacity = useSharedValue(0);

  const openSidebar = useCallback(() => {
    setSidebarOpen(true);
    translateX.value = withSpring(0, { damping: 30, stiffness: 300 });
    overlayOpacity.value = withSpring(1, { damping: 30, stiffness: 300 });
  }, [translateX, overlayOpacity]);

  const closeSidebar = useCallback(() => {
    setSidebarOpen(false);
    translateX.value = withSpring(-SIDEBAR_WIDTH, { damping: 30, stiffness: 300 });
    overlayOpacity.value = withSpring(0, { damping: 30, stiffness: 300 });
  }, [translateX, overlayOpacity]);

  const toggleSidebar = useCallback(() => {
    if (sidebarOpen) {
      closeSidebar();
    } else {
      openSidebar();
    }
  }, [sidebarOpen, openSidebar, closeSidebar]);

  const sidebarAnimatedStyle = useAnimatedStyle(() => ({
    transform: [{ translateX: translateX.value }],
  }));

  const overlayAnimatedStyle = useAnimatedStyle(() => ({
    opacity: overlayOpacity.value,
    pointerEvents: overlayOpacity.value > 0 ? "auto" : "none",
  }));

  const handleNavigation = (route?: string, action?: () => void) => {
    closeSidebar();
    if (route) {
      router.push(route as any);
    } else if (action) {
      action();
    }
  };

  const menuItems: MenuItem[] = [
    {
      label: "Criar Treino",
      icon: "add-circle-outline",
      route: "/workout/create",
      color: "#00E0A4",
      bgColor: "bg-primary",
    },
    {
      label: "Criar Split",
      icon: "barbell-outline",
      route: "/workout/create-split",
      color: "#00E0A4",
      bgColor: "bg-primary",
    },
    {
      label: "Começar Treino",
      icon: "play-circle-outline",
      route: "/workout/list",
      color: "#00E0A4",
      bgColor: "bg-primary",
    },
    {
      label: "Chat",
      icon: "chatbubble-ellipses-outline",
      route: "/chat",
      color: "#00E0A4",
      bgColor: "bg-primary",
    },
    {
      label: "Ver Feedbacks",
      icon: "analytics-outline",
      route: "/feedback/history",
      color: "#00E0A4",
      border: true,
    },
    {
      label: "Ver Histórico",
      icon: "calendar-outline",
      route: "/history",
      color: "#00E0A4",
      border: true,
    },
    {
      label: "Ajustar Perfil",
      icon: "person-outline",
      route: "/edit-profile",
      color: "#00E0A4",
      border: true,
    },
    {
      label: "Encerrar Sessão",
      icon: "exit-outline",
      color: "#00E0A4",
      border: true,
      action: async () => {
        const { authStorage } = await import("~/commons/utils/authStorage");
        await authStorage.clear();
        router.replace("/auth/login");
      },
    },
  ];

  const renderMenuItem = (item: MenuItem, index: number) => (
    <TouchableOpacity
      key={index}
      onPress={() => handleNavigation(item.route, item.action)}
      className={`p-4 rounded-premium flex-row items-center mb-3 ${
        item.bgColor ? item.bgColor : "border-thin"
      } ${item.border ? "border-primary" : ""}`}
      style={item.border ? { borderWidth: 1 } : {}}
    >
      <Ionicons name={item.icon} size={20} color={item.color} />
      <Text
        className={`ml-2 font-semibold ${
          item.bgColor ? "text-primary font-bold" : "text-primary"
        }`}
      >
        {item.label.toUpperCase()}
      </Text>
    </TouchableOpacity>
  );

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
    <View className="flex-1 bg-background">
      {/* OVERLAY */}
      <Animated.View
        style={[
          {
            position: "absolute",
            left: 0,
            right: 0,
            top: 0,
            bottom: 0,
            backgroundColor: "rgba(0,0,0,0.5)",
            zIndex: 50,
          },
          overlayAnimatedStyle,
        ]}
      >
        <TouchableOpacity
          activeOpacity={1}
          onPress={closeSidebar}
          style={{ flex: 1 }}
        />
      </Animated.View>

      {/* SIDEBAR */}
      <Animated.View
        style={[
          {
            position: "absolute",
            left: 0,
            top: 0,
            bottom: 0,
            width: SIDEBAR_WIDTH,
            zIndex: 100,
          },
          sidebarAnimatedStyle,
        ]}
      >
        <View className="flex-1 bg-surface pt-16 px-4">
          <Text className="text-primary font-bold text-xl mb-8 tracking-tighter">
            MENU
          </Text>
          {menuItems.map((item, index) => renderMenuItem(item, index))}
        </View>
      </Animated.View>

      {/* MAIN CONTENT */}
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

            <View className="flex-row items-center gap-3">
              <TouchableOpacity
                onPress={toggleSidebar}
                className="w-12 h-12 bg-surface rounded-full items-center justify-center border-thin border-stroke"
              >
                <Ionicons name="menu" size={24} color="#00E0A4" />
              </TouchableOpacity>

              <TouchableOpacity className="w-12 h-12 bg-surface rounded-full items-center justify-center border-thin border-stroke">
                <Ionicons
                  name="notifications-outline"
                  size={24}
                  color="#00E0A4"
                />
              </TouchableOpacity>
            </View>
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

        </View>
      </ScrollView>
    </View>
  );
}
