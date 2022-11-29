import { Role } from "../model/Role";

export interface User {
  id: number;
  email: string | null;
  name: string;
  emailConfirmed: boolean;
  studentStatusConfirmed: boolean;
  avatarUrl: string | null;
  role: Role;
}
