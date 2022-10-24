import { CreatePostDto } from "../../dto/request/CreatePostDto";
import { Post } from "../../model/Post";
import { Pagination, PaginationData } from "./Pagination";

export interface PostsWithPagination {
  posts: Post[];
  postCount: PaginationData;
}

export interface PostFilters {
  tagNames: string[];
}

export interface PostService {
  getPost(postId: number): Promise<Post>;
  getPosts(pagination: Pagination): Promise<PostsWithPagination>;
  getPostsByCategory(
    categoryName: string,
    pagination: Pagination,
    postFilters?: PostFilters
  ): Promise<PostsWithPagination>;
  getPostsByCategoryGroup(
    categoryGroupName: string,
    pagination: Pagination,
    postFilters?: PostFilters
  ): Promise<PostsWithPagination>;
  createPost(post: CreatePostDto): Promise<void>;
  deletePost(postId: string): Promise<void>;
}
