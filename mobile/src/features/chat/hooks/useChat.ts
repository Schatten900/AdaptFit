import { useMutation, useQuery } from "@tanstack/react-query";
import { ChatService } from "../services/ChatService";
import type { AgentType } from "~/types/chat/types";

export const useSendMessage = () => {
  return useMutation({
    mutationFn: ({
      question,
      agent,
    }: {
      question: string;
      agent: AgentType;
    }) => ChatService.sendMessage(question, agent),
  });
};

export const useAiHealth = () => {
  return useQuery({
    queryKey: ["ai-health"],
    queryFn: () => ChatService.checkHealth(),
    retry: 0,
    staleTime: 1000 * 60 * 2,
  });
};
