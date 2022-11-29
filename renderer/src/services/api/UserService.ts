import { DATA_PROVIDER_URL } from ".";
import { User } from "../../model/User";
import { UserService } from "../interfaces/UserService";

export const userService: UserService = {
    getUserDetails: async function (user_id: number): Promise<User> {
       try{
            const request = await fetch(`${DATA_PROVIDER_URL}/users/${user_id}`);
            return request.json();
       }    
       catch(e: any){
        console.error(e);
        throw new Error("Coś poszło nie tak")
       }
    }
}