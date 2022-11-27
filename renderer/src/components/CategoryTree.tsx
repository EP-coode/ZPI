import React, { useEffect, useState } from "react";
import { categoryGroupService } from "../services";
import { CategoryGroup } from "../model/CategoryGroup";
import ContentPane from "../layout/ContentPane";
import { CategoryLink } from "./CategoryLink";
import { CategoryTreeItem } from "./CategoryTreeItem";

const CategoryTree = () => {
  const [categoryGroups, setCategoryGroups] = useState<CategoryGroup[]>([]);

  useEffect(() => {
    const fetchCategoryGroups = async () => {
      try {
        const categoryGroups = await categoryGroupService.getCategoryGroups();
        setCategoryGroups(categoryGroups);
      } catch (e) {
        console.error(e);
      }
    };

    fetchCategoryGroups();
  }, []);

  return (
    <ContentPane className="flex-col gap-5">
      <h2 className="text-2xl font-semibold">Kategorie postów</h2>
      <div className="flex flex-col w-full">
        {categoryGroups.map((categoryGroup) => (
          <CategoryTreeItem
            categoryGroup={categoryGroup}
            key={categoryGroup.displayName}
          />
        ))}
      </div>
    </ContentPane>
  );
};

export default CategoryTree;
