export interface ApiResponse<T> {
  message?: string;
  type?: 'SUCCESS' | 'ERROR' | 'WARNING';
  data: T;
}

