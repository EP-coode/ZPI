import { Category } from "./Category";

export interface CategoryGroup{
    name: string,
    categories?: Partial<Category>[],
    totalPosts: number,
}