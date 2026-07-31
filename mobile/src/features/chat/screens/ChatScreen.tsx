import { useState, useRef, useCallback } from "react";
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  ScrollView,
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
} from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useRouter } from "expo-router";
import { BackButton } from "~/commons/components/BackButton";
import { useSendMessage } from "../hooks/useChat";
import type { ChatMessage, AgentType } from "~/types/chat/types";

const AGENTS: { key: AgentType; label: string; icon: string }[] = [
  { key: "training", label: "Treino", icon: "barbell" },
  { key: "nutrition", label: "Nutrição", icon: "nutrition" },
];

const WELCOME_MESSAGES: Record<AgentType, ChatMessage> = {
  training: {
    id: "welcome-training",
    text: "Olá! Sou seu assistente de treino.\n\nPergunte sobre exercícios, progressão, divisão de treino ou peça substituições.",
    sender: "agent",
    timestamp: new Date(),
  },
  nutrition: {
    id: "welcome-nutrition",
    text: "Olá! Sou seu assistente de nutrição.\n\nPergunte sobre refeições, substituições alimentares, distribuição de macros ou sugestões.",
    sender: "agent",
    timestamp: new Date(),
  },
};

let msgId = 0;
const nextId = () => `msg-${++msgId}`;

export default function ChatScreen() {
  const router = useRouter();
  const [agent, setAgent] = useState<AgentType>("training");
  const [messages, setMessages] = useState<ChatMessage[]>([
    WELCOME_MESSAGES.training,
  ]);
  const [input, setInput] = useState("");
  const scrollRef = useRef<ScrollView>(null);
  const { mutateAsync: sendMessage, isPending } = useSendMessage();

  const switchAgent = useCallback(
    (newAgent: AgentType) => {
      if (newAgent === agent) return;
      setAgent(newAgent);
      setMessages([WELCOME_MESSAGES[newAgent]]);
    },
    [agent]
  );

  const handleSend = useCallback(async () => {
    const text = input.trim();
    if (!text || isPending) return;
    setInput("");

    const userMsg: ChatMessage = {
      id: nextId(),
      text,
      sender: "user",
      timestamp: new Date(),
    };
    setMessages((prev) => [...prev, userMsg]);

    try {
      const response = await sendMessage({ question: text, agent });

      const agentMsg: ChatMessage = {
        id: nextId(),
        text:
          response.content ||
          "Desculpe, não consegui processar sua pergunta.",
        sender: "agent",
        timestamp: new Date(),
      };
      setMessages((prev) => [...prev, agentMsg]);
    } catch {
      const errorMsg: ChatMessage = {
        id: nextId(),
        text: "Erro ao comunicar com o assistente. Verifique se o servidor Ollama está rodando e tente novamente.",
        sender: "agent",
        timestamp: new Date(),
        isError: true,
      };
      setMessages((prev) => [...prev, errorMsg]);
    }
  }, [input, isPending, sendMessage, agent]);

  return (
    <KeyboardAvoidingView
      className="flex-1 bg-background"
      behavior={Platform.OS === "ios" ? "padding" : undefined}
      keyboardVerticalOffset={Platform.OS === "ios" ? 90 : 0}
    >
      {/* HEADER */}
      <View className="pt-14 px-6 pb-4">
        <View className="flex-row items-center justify-between mb-4">
          <BackButton />
          <Text className="text-text-primary text-lg font-bold">
            Assistente IA
          </Text>
          <View className="w-10" />
        </View>

        {/* AGENT SELECTOR */}
        <View className="flex-row bg-surface rounded-premium p-1 border-thin border-stroke">
          {AGENTS.map(({ key, label, icon }) => (
            <TouchableOpacity
              key={key}
              onPress={() => switchAgent(key)}
              className={`flex-1 py-2.5 rounded-premium flex-row items-center justify-center ${
                agent === key ? "bg-primary" : ""
              }`}
            >
              <Ionicons
                name={icon as any}
                size={16}
                color={agent === key ? "#020617" : "#94A3B8"}
              />
              <Text
                className={`ml-1.5 text-sm font-semibold ${
                  agent === key ? "text-background" : "text-text-secondary"
                }`}
              >
                {label}
              </Text>
            </TouchableOpacity>
          ))}
        </View>
      </View>

      {/* MESSAGES */}
      <ScrollView
        ref={scrollRef}
        className="flex-1 px-6"
        onContentSizeChange={() =>
          scrollRef.current?.scrollToEnd({ animated: true })
        }
        showsVerticalScrollIndicator={false}
        keyboardShouldPersistTaps="handled"
      >
        {messages.map((msg) => {
          const isUser = msg.sender === "user";

          return (
            <View
              key={msg.id}
              className={`mb-4 ${isUser ? "items-end" : "items-start"}`}
            >
              <View
                className={`flex-row items-end ${
                  isUser ? "flex-row-reverse" : ""
                }`}
              >
                {!isUser && (
                  <View className="w-8 h-8 rounded-full bg-primary items-center justify-center mr-2">
                    <Ionicons
                      name="chatbubble-ellipses"
                      size={16}
                      color="#020617"
                    />
                  </View>
                )}

                <View
                  className={`max-w-[80%] p-4 rounded-premium ${
                    isUser
                      ? "bg-primary"
                      : msg.isError
                      ? "bg-status-error/10 border-thin border-status-error/30"
                      : "bg-surface border-thin border-stroke"
                  }`}
                >
                  <Text
                    className={`text-sm leading-5 ${
                      isUser ? "text-background font-semibold" : "text-text-primary"
                    }`}
                  >
                    {msg.text}
                  </Text>
                </View>
              </View>
            </View>
          );
        })}

        {isPending && (
          <View className="flex-row items-center mb-4">
            <View className="w-8 h-8 rounded-full bg-primary items-center justify-center mr-2">
              <ActivityIndicator size="small" color="#020617" />
            </View>
            <Text className="text-text-secondary text-sm italic">
              Assistente está pensando...
            </Text>
          </View>
        )}
      </ScrollView>

      {/* INPUT */}
      <View className="px-6 pb-8 pt-2">
        <View className="flex-row items-center bg-surface rounded-premium border-thin border-stroke">
          <TextInput
            className="flex-1 p-4 text-text-primary text-sm font-sans"
            placeholder="Digite sua pergunta..."
            placeholderTextColor="#94A3B8"
            value={input}
            onChangeText={setInput}
            multiline
            maxLength={500}
            returnKeyType="send"
            onSubmitEditing={handleSend}
            blurOnSubmit
          />
          <TouchableOpacity
            onPress={handleSend}
            disabled={!input.trim() || isPending}
            className="w-12 h-12 mr-2 items-center justify-center"
          >
            <Ionicons
              name="send"
              size={22}
              color={input.trim() && !isPending ? "#00E0A4" : "#1E293B"}
            />
          </TouchableOpacity>
        </View>
      </View>
    </KeyboardAvoidingView>
  );
}
