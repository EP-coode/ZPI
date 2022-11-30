import { CategoryGroup } from "../../model/CategoryGroup";

export interface CategoryGroupService{
    getCategoryGroups(): Promise<Required<CategoryGroup>[]>
}