import { CreatePostDto } from "../../dto/request/CreatePostDto";
import { Post } from "../../model/Post";
import { Pagination, PaginationData } from "./Pagination";

export interface PostsWithPagination {
  posts: Post[];
  postCount: PaginationData;
}

export enum PostOrdering{
  DateAsc = "date_asc",
  DateDsc = "date_dsc",
  LikesAsc = "likes_dsc",
  LikesDsc = "likes_dsc",
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
  getPosts(pagination: Pagination, filters?: PostFilters): Promise<PostsWithPagination>;
  createPost(post: CreatePostDto): Promise<void>;
  deletePost(postId: string): Promise<void>;
}
