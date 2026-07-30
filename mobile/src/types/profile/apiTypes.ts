export type GoalType = 'FAT_LOSS' | 'MUSCLE_GAIN' | 'ENDURANCE' | 'STRENGTH';
export type ExperienceLevel = 'BEGINNER' | 'INTERMEDIATE' | 'ADVANCED';
export type Gender = 'MASCULINE' | 'FEMININE' | 'OTHER';

export interface ApiProfile {
  id: number;
  userId: number;
  weight?: number;
  height?: number;
  age?: number;
  goal: GoalType;
  experience: ExperienceLevel;
  gender?: Gender;
  daysPerWeek?: number;
  sessionDuration?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateProfileRequest {
  weight: number;
  height: number;
  age: number;
  goal: GoalType;
  experience: ExperienceLevel;
  gender: Gender;
  daysPerWeek?: number;
  sessionDuration?: number;
}

export interface UpdateProfileRequest extends Partial<CreateProfileRequest> {}
