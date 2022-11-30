import { CreateCommentDto } from "../../dto/request/CreateCommentDto";
import { Comment } from "../../model/Comment";
import { Pagination, PaginationData } from "./Pagination";

export interface CommentsWithPagination {
  comments: Comment[];
  totalComments: number;
}

export enum CommentOrdering{
    DateAsc = "ASC",
    DateDsc = "DESC"
  }


export interface CommentService {
  getComments(postId: number, pagination: Pagination, ordering: CommentOrdering): Promise<CommentsWithPagination>;
  createComment(postId: number, comment: CreateCommentDto): Promise<Comment>;
  deleteComment(postId: number, commentId: number): Promise<void>;
}