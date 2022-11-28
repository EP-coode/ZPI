import { DATA_PROVIDER_URL } from ".";
import { LikeOrDislike, LikeService } from "../interfaces/LikeService";
import { fetchWithJWT } from "../../utils/fetchWithJWT";

export const likeService: LikeService = {

    LikePost: function (postId: string): Promise<LikeOrDislike> {
        try{
            const result = fetchWithJWT<LikeOrDislike>(`postRating/like/${postId}`, {method: "GET",});
            return result;
        }catch(e: any){
            console.log(e);
            throw new Error("Coś poszło nie tak");
        }
    },

    DislikePost: function (postId: string): Promise<LikeOrDislike> {
        try{
            const result = fetchWithJWT<LikeOrDislike>(`postRating/dislike/${postId}`, {method: "GET",})
            return result;
        }catch(e: any){
            console.log(e);
             throw new Error("Coś poszło nie tak");
        }
    },
};