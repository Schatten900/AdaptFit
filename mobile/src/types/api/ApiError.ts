export class ApiError extends Error {
  status: number;
  type?: string;

  constructor(message: string, status: number, type?: string) {
    super(message);
    this.status = status;
    this.type = type;
  }
}
