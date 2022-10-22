import { CreatePostDto } from "../../dto/request/CreatePostDto";
import { Post } from "../../model/Post";
import { Pagination } from "../interfaces/Pagination";
import {
  PostFilters,
  PostService,
  PostsWithPagination,
} from "../interfaces/PostService";

export const postsService: PostService = {
  getPost: function (postId: number): Promise<Post> {
    return Promise.resolve({
      postId: postId,
      title: "Legitymacje Studenckie",
      markdownContent: 
`# Jakiś bardzo interesujący content
**Lorem Ipsum** is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
## pod tytół
I tyle w temacie.`,
      category: { displayName: "W8", totalPosts: 103 },
      creationTime: new Date().toISOString(),
      tags: [
        { name: "dziekanat", totalPosts: 10 },
        { name: "legitymacje", totalPosts: 13 },
      ],
      author: {
        email: "jan@nowak.pl",
        avatarUrl: undefined,
        role: { roleName: "ADMIN"},
        emailConfirmed: true,
        studentStatusConfirmed: true
      },
      totalDislikes: 10,
      totalLikes: 100,
      imageUrl: "https://placeimg.com/400/400/arch",
      isLiked: undefined,
    });
  },
  getPosts: function (pagination: Pagination): Promise<PostsWithPagination> {
    throw new Error("Function not implemented.");
  },
  getPostsByCategory: function (
    categoryName: string,
    pagination: Pagination,
    postFilters?: PostFilters | undefined
  ): Promise<PostsWithPagination> {
    throw new Error("Function not implemented.");
  },
  getPostsByCategoryGroup: function (
    categoryGroupName: string,
    pagination: Pagination,
    postFilters?: PostFilters | undefined
  ): Promise<PostsWithPagination> {
    throw new Error("Function not implemented.");
  },
  createPost: function (post: CreatePostDto): Promise<void> {
    throw new Error("Function not implemented.");
  },
  deletePost: function (postId: string): Promise<void> {
    throw new Error("Function not implemented.");
  },
};
