import { Category } from "./Category"
import { Tag } from "./Tag"
import { User } from "./User"

export interface Post{
    postId: number
    title: string,
    markdownContent: string,
    imageUrl?: string,
    isLiked?: boolean,
    totalLikes: number,
    totalDislikes: number,
    creationTime: string
    category: Category
    author: User,
    tags: Tag[],
}