import { useMutation, useQuery } from "@tanstack/react-query";
import {
  FeedbackService,
} from "../services/FeedbackService";
import { CreateFeedbackRequest,Feedback } from "~/types/feedback/types";

export const useFeedbacks = () => {
  return useQuery({
    queryKey: ["feedbacks"],
    queryFn: () => FeedbackService.getFeedbacks(),
  });
};

export const useFeedbackById = (id: number) => {
  return useQuery({
    queryKey: ["feedback", id],
    queryFn: () => FeedbackService.getFeedbackById(id),
    enabled: !!id,
  });
};

export const useFeedbacksBySession = (workoutSessionId: number) => {
  return useQuery({
    queryKey: ["feedbacks", "session", workoutSessionId],
    queryFn: () => FeedbackService.getFeedbacksBySession(workoutSessionId),
    enabled: !!workoutSessionId,
  });
};

export const useCreateFeedback = () => {
  return useMutation({
    mutationFn: (request: CreateFeedbackRequest) =>
      FeedbackService.createFeedback(request),
  });
};

export const useDeleteFeedback = () => {
  return useMutation({
    mutationFn: (id: number) => FeedbackService.deleteFeedback(id),
  });
};
