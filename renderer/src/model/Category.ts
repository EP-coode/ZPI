import { CategoryGroup } from "./CategoryGroup"

export interface Category{
    displayName: string,
    postCategoryGroup: CategoryGroup
    totalPosts: number
}