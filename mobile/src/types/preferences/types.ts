
import {EquipmentType, InjuryType} from "./apiTypes"

export interface PreferencesFormData {
  availableEquipment: EquipmentType[];
  injuries: InjuryType[];
  customInjuries: string[];
}

export interface CreatePreferenceRequest {
  injuries?: string[];
  availableEquipment?: string[];
  exerciseBlacklist?: string[];
}

export interface UpdatePreferenceRequest extends Partial<CreatePreferenceRequest> {}
