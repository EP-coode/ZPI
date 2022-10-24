import { CreatePostDto } from "../dto/request/CreatePostDto";
import { Post } from "../model/Post";
import { Pagination } from "./interfaces/Pagination";
import { PostFilters, PostService, PostsWithPagination } from "./interfaces/PostService";

export const postsService: PostService = {
    getPost: function (postId: number): Promise<Post> {
        throw new Error("Function not implemented.");
    },
    getPosts: function (pagination: Pagination): Promise<PostsWithPagination> {
        throw new Error("Function not implemented.");
    },
    getPostsByCategory: function (categoryName: string, pagination: Pagination, postFilters?: PostFilters | undefined): Promise<PostsWithPagination> {
        throw new Error("Function not implemented.");
    },
    getPostsByCategoryGroup: function (categoryGroupName: string, pagination: Pagination, postFilters?: PostFilters | undefined): Promise<PostsWithPagination> {
        throw new Error("Function not implemented.");
    },
    createPost: function (post: CreatePostDto): Promise<void> {
        throw new Error("Function not implemented.");
    },
    deletePost: function (postId: string): Promise<void> {
        throw new Error("Function not implemented.");
    }
};