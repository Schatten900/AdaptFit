import { ApiProfile, ProfileFormData } from '~/types';

export const transformApiToForm = (apiProfile: ApiProfile): ProfileFormData => ({
    weight: apiProfile.weight ?? null,
    height: apiProfile.height ?? null,
    age: apiProfile.age ?? null,
    goal: apiProfile.goal ?? null,
    experience: apiProfile.experience ?? null,
    gender: apiProfile.gender ?? null,
});

export const transformFormToApi = (formData: ProfileFormData): Partial<ApiProfile> => ({
    weight: formData.weight ?? undefined,
    height: formData.height ?? undefined,
    age: formData.age ?? undefined,
    goal: formData.goal ?? undefined,
    experience: formData.experience ?? undefined,
    gender: formData.gender ?? undefined,
});

export const formatGoal = (goal: string | null | undefined): string => {
    if (!goal) return 'Não definido';
    const goalMap: Record<string, string> = {
        'FAT_LOSS': 'Perda de Gordura',
        'MUSCLE_GAIN': 'Ganho de Massa',
        'ENDURANCE': 'Resistência',
    };
    return goalMap[goal] || goal;
};

export const formatExperience = (experience: string | null | undefined): string => {
    if (!experience) return 'Não definido';
    const expMap: Record<string, string> = {
        'BEGINNER': 'Iniciante',
        'INTERMEDIATE': 'Intermediário',
        'ADVANCED': 'Avançado',
    };
    return expMap[experience] || experience;
};

export const calculateBMI = (weight: number | null | undefined, height: number | null | undefined): number | null => {
    if (!weight || !height || height <= 0) return null;
    return Number((weight / Math.pow(height / 100, 2)).toFixed(1));
};


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
