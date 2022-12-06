import { User } from "../../model/User";
import { UserService } from "../interfaces/UserService";

export const userService: UserService = {
    getUserDetails: function (user_id: number): Promise<User> {
        return Promise.resolve({
            id: user_id,
            email: "jan@nowak.pl",
            name: "Janusz",
            avatarUrl: "https://placeimg.com/400/400/arch",
            role: "USER",
            emailConfirmed: true,
            studentStatusConfirmed: true,
          })
    }
}