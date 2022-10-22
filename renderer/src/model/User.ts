import { Role } from "../model/Role";

export interface User {
  email: string;
  emailConfirmed: boolean;
  studentStatusConfirmed: boolean;
  avatarUrl?: string;
  role: Role;
}
