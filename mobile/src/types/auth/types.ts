export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  userId: number;
  token: string;
  tokenType: string;
  expiresIn: number;
  success: boolean
}

export interface RegisterRequest {
  email: string;
  username: string;
  password: string;
  confirmPassword: string;
}

export interface RegisterResponse {
  userId: number;
  message: string;
}

export interface AuthError {
  code: string;
  message: string;
  details?: Record<string, string[]>;
}

export interface ValidationErrors {
  email?: string[];
  password?: string[];
  username?: string[];
  confirmPassword?: string[];
}
