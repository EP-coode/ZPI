import { DATA_PROVIDER_URL } from ".";
import { CommentService, CommentOrdering, CommentsWithPagination } from "../interfaces/CommentService";
import { CreateCommentDto } from "../../dto/request/CreateCommentDto";
import { Pagination, PaginationData } from "../interfaces/Pagination";
import { fetchWithJWT } from "../../utils/fetchWithJWT";
import { Comment } from "../../model/Comment";

export const commentService: CommentService = {
    getComments: async function (postId: number, pagination: Pagination, ordering: CommentOrdering): Promise<CommentsWithPagination>{
        try{
            const result = await fetch(`${DATA_PROVIDER_URL}/posts/${postId}/comments?page=${pagination.currentPage}&sort=${ordering}`);

            if (!result.ok) {
                throw new Error("Coś poszło nie tak");
            }
        
              const commentsWithPagination = await result.json();
        
              return commentsWithPagination;

        }catch(e: any){
            console.log(e);
            throw new Error("Coś poszło nie tak");
        }
    },

    createComment: async function(postId: number, comment: CreateCommentDto): Promise<Comment>{
        try{
            const result = fetchWithJWT<Comment>(`posts/${postId}/comments`,         
            {
                method: "POST",
                headers: {
                  Accept: "application/json",
                  "Content-Type": "application/json",
                },
                body: JSON.stringify(comment),
            });
            return result;
        }catch(e: any){
            console.log(e);
            throw new Error("Coś poszło nie tak");
        }
    },

    deleteComment: async function(postId: number, commentId: number): Promise<any>{
        try{
            const result = fetchWithJWT<any>(`posts/${postId}/comments/${commentId}`, {method: "DELETE",});
            return result;
        }catch(e: any){
            console.log(e);
            throw new Error("Coś poszło nie tak");
        }
    }
}