import "~/commons/styles/global.css";
import { Slot } from "expo-router";
import * as SplashScreen from "expo-splash-screen";
import { useFonts, Inter_400Regular, Inter_600SemiBold, Inter_700Bold } from "@expo-google-fonts/inter";
import { useEffect } from "react";
import { View } from "react-native";

// Impede que a Splash Screen feche automaticamente
SplashScreen.preventAutoHideAsync();

export default function Layout() {
  const [loaded, error] = useFonts({
    "Inter-Regular": Inter_400Regular,
    "Inter-SemiBold": Inter_600SemiBold,
    "Inter-Bold": Inter_700Bold,
  });

  useEffect(() => {
    if (loaded || error) {
      SplashScreen.hideAsync();
    }
  }, [loaded, error]);

  if (!loaded && !error) {
    return null;
  }

  // Usamos uma View ocupando a tela toda com o fundo do seu tema
  // para evitar o "flash branco" entre transições de rota.
  return (
    <View className="flex-1 bg-background">
      <Slot />
    </View>
  );
}