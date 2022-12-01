import { User } from "./User";

export interface Comment {
  commentId: number;
  postId: number;
  creator: User;
  totalLikes: number;
  totalDislikes: number;
  content: string;
  creationTime: string;
  isLiked: boolean | null;
}
