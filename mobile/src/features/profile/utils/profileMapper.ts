import { ApiProfile } from '../../../types/profile/apiTypes';
import { ProfileFormData } from '../../../types/profile/types';

export function mapApiProfileToForm(
  api: ApiProfile
): ProfileFormData {
  return {
    weight: api.weight ?? null,
    height: api.height ?? null,
    age: api.age ?? null,
    goal: api.goal ?? null,          
    experience: api.experience ?? null,
    gender: api.gender ?? null,
  };
}
