import { User } from "../../model/User";

export interface UserService {
  getUserDetails(user_id: number): Promise<User>;
}
