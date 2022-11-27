import { DATA_PROVIDER_URL } from ".";
import { CategoryGroup } from "../../model/CategoryGroup";
import { CategoryGroupService } from "../interfaces/CategoryGroupService";

export const categoryGroupService: CategoryGroupService = {
  getCategoryGroups: async function (): Promise<Required<CategoryGroup>[]> {
    try {
      const request = await fetch(`${DATA_PROVIDER_URL}/postCategoryGroups`);

      if (!request.ok) {
        throw new Error("Coś poszło nie tak");
      }

      const categoryGroups: Required<CategoryGroup>[] = await request.json();

      return categoryGroups;
    } catch (e: any) {
      throw new Error("Coś poszło nie tak");
    }
  },
};
