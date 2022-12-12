import { CreatePostDto } from "../../dto/request/CreatePostDto";
import { Post } from "../../model/Post";
import { Pagination, PaginationData } from "./Pagination";

export interface PostsWithPagination {
  posts: Post[];
  totalPosts: number;
}

export enum PostOrdering{
  DateAsc = "DATE_ASC",
  DateDsc = "DATE_DSC",
  LikesAsc = "LIKES_ASC",
  LikesDsc = "LIKES_DSC",
}

export interface PostFilters {
  tagNames: string[] | null;
  categoryGroupId: string | null;
  categoryId: string | null;
  creatorId: number | null;
  maxPostDaysAge: number;
  orderBy: PostOrdering;
}

export interface PostService {
  getPost(postId: number): Promise<Post>;
  getPosts(pagination: Pagination, filters: PostFilters): Promise<PostsWithPagination>;
  createPost(post: CreatePostDto): Promise<Post>;
  deletePost(postId: number): Promise<void>;
}
