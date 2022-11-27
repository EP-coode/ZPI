import { Category } from "./Category";

export interface CategoryGroup{
    displayName: string,
    postCategories?: Partial<Category>[],
    totalPosts: number,
}