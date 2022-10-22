import { Category } from "./Category";

export interface CategoryGroup{
    name: string,
    categories?: Category[],
    totalPosts: number,
}