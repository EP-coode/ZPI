import { DATA_PROVIDER_URL } from ".";
import { CreatePostDto } from "../../dto/request/CreatePostDto";
import { Post } from "../../model/Post";
import { fetchWithJWT } from "../../utils/fetchWithJWT";
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
  createPost: async function (post: CreatePostDto): Promise<Post> {
    const formData = new FormData();

    formData.append("title", post.title);
    formData.append("categoryName", post.categoryName);
    formData.append("markdownContent", post.markdownContent);
    if (post.photo) formData.append("photo", post.photo);
    post.tagNames.forEach((tagName) => {
      formData.append(`tagNames`, tagName);
    });

    return await fetchWithJWT<Post>("posts/create", {
      method: "POST",
      body: formData,
    });
  },
  deletePost: function (postId: string): Promise<void> {
    throw new Error("Function not implemented.");
  },
};
