import { Role } from "../model/Role";

export interface User {
  id: number;
  email: string;
  emailConfirmed: boolean;
  studentStatusConfirmed: boolean;
  avatarUrl: string | null;
  role: Role;
}
