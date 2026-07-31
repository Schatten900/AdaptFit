import { api } from "~/config/api";
import type { ChatResponse, AgentType } from "~/types/chat/types";

export const ChatService = {
  sendMessage: async (question: string, agent: AgentType): Promise<ChatResponse> => {
    const endpoint =
      agent === "training"
        ? "/api/v1/ai/training/chat"
        : "/api/v1/ai/nutrition/chat";

    return api.post(endpoint, { question });
  },

  checkHealth: async (): Promise<boolean> => {
    return api.get("/api/v1/ai/health");
  },
};
