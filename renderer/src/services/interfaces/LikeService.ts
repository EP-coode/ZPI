export interface LikeOrDislike {
  totalLikes: number;
  message: string;
  isLiked: boolean;
}

export interface LikeService {
    LikePost(postId: string): Promise<LikeOrDislike>;
    DislikePost(postId: string): Promise<LikeOrDislike>;
  }