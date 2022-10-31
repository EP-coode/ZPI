import { CreatePostDto } from "../../dto/request/CreatePostDto";
import { Post } from "../../model/Post";
import { Pagination, PaginationData } from "./Pagination";

export interface PostsWithPagination {
  posts: Post[];
  postCount: PaginationData;
}

export interface PostFilters {
  tagNames: string[] | null;
  categoryGroup: string | null;
  category: string | null;
  creatorId: number | null
}

export interface PostService {
  getPost(postId: number): Promise<Post>;
  getPosts(pagination: Pagination, filters?: PostFilters): Promise<PostsWithPagination>;
  createPost(post: CreatePostDto): Promise<void>;
  deletePost(postId: string): Promise<void>;
}
