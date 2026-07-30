
export enum EquipmentType {
    GYM = "GYM",
    HOME = "HOME",
    CROSSFIT = "CROSSFIT"
}

export enum InjuryType {
    KNEE_PAIN = "KNEE_PAIN",
    LOWER_BACK_PAIN = "LOWER_BACK_PAIN",
    SHOULDER_PAIN = "SHOULDER_PAIN",
    ELBOW_PAIN = "ELBOW_PAIN",
    WRIST_PAIN = "WRIST_PAIN",
    NECK_PAIN = "NECK_PAIN",
    DISC_HERNIA = "DISC_HERNIA",
    TENDONITIS = "TENDONITIS"
}

export interface ApiPreferences {
  userId: number;
  injuries?: string[];
  availableEquipment?: string[];
  exerciseBlacklist?: string[];
}
