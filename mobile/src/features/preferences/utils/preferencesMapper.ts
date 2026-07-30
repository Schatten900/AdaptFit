import { EquipmentType, InjuryType } from '~/types/preferences/apiTypes';

export const EQUIPMENT_LABELS: Record<string, string> = {
  [EquipmentType.GYM]: 'Academia',
  [EquipmentType.HOME]: 'Casa',
  [EquipmentType.CROSSFIT]: 'Crossfit',
};

export const INJURY_LABELS: Record<string, string> = {
  [InjuryType.KNEE_PAIN]: 'Dor no Joelho',
  [InjuryType.LOWER_BACK_PAIN]: 'Dor Lombar',
  [InjuryType.SHOULDER_PAIN]: 'Dor no Ombro',
  [InjuryType.ELBOW_PAIN]: 'Dor no Cotovelo',
  [InjuryType.WRIST_PAIN]: 'Dor no Punho',
  [InjuryType.NECK_PAIN]: 'Dor no Pescoço',
  [InjuryType.DISC_HERNIA]: 'Hérnia de Disco',
  [InjuryType.TENDONITIS]: 'Tendinite',
};

export const formatEquipment = (value: string): string =>
  EQUIPMENT_LABELS[value] || value;

export const formatInjury = (value: string): string =>
  INJURY_LABELS[value] || value;
