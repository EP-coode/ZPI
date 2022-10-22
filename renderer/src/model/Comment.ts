import { User } from "./User";

export interface Comment {
  content: string;
  totalLikes: number;
  totalDislikes: number;
  creationTime: string;
  creator: User;
}
