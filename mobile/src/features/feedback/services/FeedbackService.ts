import { api } from "~/config/api";
import { ApiResponse } from "~/types/api/apiResponse";
import { CreateFeedbackRequest,Feedback } from "~/types/feedback/types";


export const FeedbackService = {
  createFeedback: async (request: CreateFeedbackRequest): Promise<Feedback> => {
    return api.post("/feedbacks", request);
  },

  getFeedbacks: async (): Promise<Feedback[]> => {
    return api.get("/feedbacks");
  },

  getFeedbackById: async (id: number): Promise<Feedback> => {
    return api.get(`/feedbacks/${id}`);
  },

  getFeedbacksBySession: async (workoutSessionId: number): Promise<Feedback[]> => {
    return api.get(`/feedbacks/session/${workoutSessionId}`);
  },

  deleteFeedback: async (id: number): Promise<void> => {
    return api.delete(`/feedbacks/${id}`);
  },
};
