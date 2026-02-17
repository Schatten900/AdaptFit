import AsyncStorage from '@react-native-async-storage/async-storage';

const WORKOUT_SESSION_KEY = '@workout_session';
const WORKOUT_HISTORY_KEY = '@workout_history';

export interface WorkoutSessionData {
  id?: string;
  workoutDayId: number;
  workoutDayName: string;
  duration: number;
  totalSets: number;
  completedSets: number;
  totalReps: number;
  totalWeight: number;
  exercises: {
    name: string;
    sets: number;
    completedSets: number;
    reps: number;
    weight: number;
  }[];
  finishedAt: string;
}

export const workoutSessionStorage = {
  async saveSession(data: WorkoutSessionData) {
    try {
      await AsyncStorage.setItem(WORKOUT_SESSION_KEY, JSON.stringify(data));
    } catch (error) {
      console.error('Erro ao salvar sessão:', error);
    }
  },

  async getSession(): Promise<WorkoutSessionData | null> {
    try {
      const data = await AsyncStorage.getItem(WORKOUT_SESSION_KEY);
      return data ? JSON.parse(data) : null;
    } catch (error) {
      console.error('Erro ao buscar sessão:', error);
      return null;
    }
  },

  async clearSession() {
    try {
      await AsyncStorage.removeItem(WORKOUT_SESSION_KEY);
    } catch (error) {
      console.error('Erro ao limpar sessão:', error);
    }
  },

  async addToHistory(data: WorkoutSessionData) {
    try {
      const history = await this.getHistory();
      const newEntry = {
        ...data,
        id: Date.now().toString(),
      };
      history.unshift(newEntry);
      await AsyncStorage.setItem(WORKOUT_HISTORY_KEY, JSON.stringify(history));
      return newEntry;
    } catch (error) {
      console.error('Erro ao adicionar ao histórico:', error);
      throw error;
    }
  },

  async getHistory(): Promise<WorkoutSessionData[]> {
    try {
      const data = await AsyncStorage.getItem(WORKOUT_HISTORY_KEY);
      return data ? JSON.parse(data) : [];
    } catch (error) {
      console.error('Erro ao buscar histórico:', error);
      return [];
    }
  },

  async getHistoryByWorkoutDay(workoutDayId: number): Promise<WorkoutSessionData[]> {
    try {
      const history = await this.getHistory();
      return history.filter(h => h.workoutDayId === workoutDayId);
    } catch (error) {
      console.error('Erro ao buscar histórico por workoutDay:', error);
      return [];
    }
  },

  async getLastWorkoutDayHistory(workoutDayId: number): Promise<WorkoutSessionData | null> {
    try {
      const history = await this.getHistoryByWorkoutDay(workoutDayId);
      return history.length > 1 ? history[1] : null;
    } catch (error) {
      console.error('Erro ao buscar último treino:', error);
      return null;
    }
  },

  async clearHistory() {
    try {
      await AsyncStorage.removeItem(WORKOUT_HISTORY_KEY);
    } catch (error) {
      console.error('Erro ao limpar histórico:', error);
    }
  },
};
