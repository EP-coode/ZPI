import { DATA_PROVIDER_URL } from ".";
import { LikeOrDislike, LikeService } from "../interfaces/LikeService";
import { fetchWithJWT } from "../../utils/fetchWithJWT";
import { Post } from "../../model/Post";
import { Comment } from "../../model/Comment";
import { commentService } from "./CommentService";

export const likeService: LikeService = {

    LikePost: function (postId: number): Promise<LikeOrDislike> {
        try{
            const result = fetchWithJWT<LikeOrDislike>(`postRating/like/${postId}`, {method: "GET",});
            return result;
        }catch(e: any){
            console.log(e);
            throw new Error("Coś poszło nie tak");
        }
    },

    DislikePost: function (postId: number): Promise<LikeOrDislike> {
        try{
            const result = fetchWithJWT<LikeOrDislike>(`postRating/dislike/${postId}`, {method: "GET",})
            return result;
        }catch(e: any){
            console.log(e);
             throw new Error("Coś poszło nie tak");
        }
    },

    IsPostLiked: async function (postId: number): Promise<boolean | null> {
        try{
            const post = await fetchWithJWT<Post>(`posts/${postId}`, {method: "GET",});
            return post.isLiked;
        }catch(e: any){
            console.log(e);
             throw new Error("Coś poszło nie tak");
        }
    },

    LikeComment: function (commentId: number): Promise<LikeOrDislike> {
        try{
            const result = fetchWithJWT<LikeOrDislike>(`commentRating/like/${commentId}`, {method: "GET",});
            return result;
        }catch(e: any){
            console.log(e);
            throw new Error("Coś poszło nie tak");
        }
    },

    DislikeComment: function (commentId: number): Promise<LikeOrDislike> {
        try{
            const result = fetchWithJWT<LikeOrDislike>(`commentRating/dislike/${commentId}`, {method: "GET",})
            return result;
        }catch(e: any){
            console.log(e);
             throw new Error("Coś poszło nie tak");
        }
    },

    IsCommentLiked: async function (commentId: number): Promise<boolean | null> {
        try{
            const comment = await fetchWithJWT<Comment>(`posts/${0}/comments/${commentId}`, {method: "GET",})
            return comment.isLiked;
        }catch(e: any){
            console.log(e);
             throw new Error("Coś poszło nie tak");
        }
    },
};