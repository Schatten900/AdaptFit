import { GoalType, ExperienceLevel, Gender } from './apiTypes';

export interface ProfileFormData {
  weight: number | null;
  height: number | null;
  age: number | null;
  goal: GoalType | null;
  experience: ExperienceLevel | null;
  gender: Gender | null;
}

export interface ProfileFormDataInput {
  weight: string;
  height: string;
  age: string;
  goal: string;
  experience: string;
  gender: string;
}

export interface ProfileCardProps {
  title: string;
  value: string | number | undefined;
  icon?: string;
  onPress?: () => void;
}

export interface ProfileStep {
  key: string;
  title: string;
  description: string;
  options: { label: string; value: string }[];
}

