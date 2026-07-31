export interface ChatMessage {
  id: string;
  text: string;
  sender: "user" | "agent";
  timestamp: Date;
  isError?: boolean;
}

export interface ChatResponse {
  content: string;
  success: boolean;
  errorMessage: string | null;
}

export type AgentType = "training" | "nutrition";
