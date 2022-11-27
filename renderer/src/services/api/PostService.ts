import { DATA_PROVIDER_URL } from ".";
import { CreatePostDto } from "../../dto/request/CreatePostDto";
import { Post } from "../../model/Post";
import { Pagination } from "../interfaces/Pagination";
import {
  PostFilters,
  PostService,
  PostsWithPagination,
} from "../interfaces/PostService";

export const postsService: PostService = {
  getPost: async function (postId: number): Promise<Post> {
    try {
      const result = await fetch(`${DATA_PROVIDER_URL}/posts/${postId}`);

      if (!result.ok) {
        throw new Error("Coś poszło nie tak");
      }

      const post = await result.json();

      return post;
    } catch (e: any) {
      throw new Error("Coś poszło nie tak");
    }
  },
  getPosts: async function (
    { currentPage, postPerPage }: Pagination,
    filters: PostFilters
  ): Promise<PostsWithPagination> {
    try {
      const result = await fetch(
        `${DATA_PROVIDER_URL}/posts?page=${currentPage}&pageSize=${postPerPage}`,
        {
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
          },
          body: JSON.stringify(filters),
        }
      );

      if (!result.ok) {
        throw new Error("Coś poszło nie tak");
      }

      const postWithPagination = await result.json();

      return postWithPagination;
    } catch (e: any) {
      throw new Error("Coś poszło nie tak");
    }
  },
  createPost: function (post: CreatePostDto): Promise<void> {
    throw new Error("Function not implemented.");
  },
  deletePost: function (postId: string): Promise<void> {
    throw new Error("Function not implemented.");
  },
};
