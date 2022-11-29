export interface LikeOrDislike {
  totalLikes: number;
  message: string;
  isLiked: boolean;
}

export interface LikeService {
    LikePost(postId: number): Promise<LikeOrDislike>;
    DislikePost(postId: number): Promise<LikeOrDislike>;
  }