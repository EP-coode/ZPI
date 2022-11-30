export interface LikeOrDislike {
  totalLikes: number;
  message: string;
  isLiked: boolean;
}

export interface LikeService {
    LikePost(postId: number): Promise<LikeOrDislike>;
    DislikePost(postId: number): Promise<LikeOrDislike>;
    IsPostLiked(postId: number): Promise<boolean | null>;
    LikeComment(commentId: number): Promise<LikeOrDislike>;
    DislikeComment(commentId: number): Promise<LikeOrDislike>;
    IsCommentLiked(commentId: number): Promise<boolean | null>;
  }