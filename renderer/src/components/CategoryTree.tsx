import React, { useEffect, useState } from "react";
import { categoryGroupService } from "../services";
import { CategoryGroup } from "../model/CategoryGroup";
import ContentPane from "../layout/ContentPane";
import Link from "next/link";
import { CategoryLink } from "./CategoryLink";

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
      <div className="flex flex-col">
        {categoryGroups.map(({ name, totalPosts, categories }) => (
          <div
            key={name}
            tabIndex={0}
            className="collapse collapse-arrow border border-base-300 bg-base-100 last:rounded-b-xl first:rounded-t-xl w-full max-w-md"
          >
            <div className="collapse-title">
              <CategoryLink
                key={name}
                categoryGroupItemsCount={totalPosts}
                categoryGroupName={name}
                anchorClassNames="btn-md btn-ghost"
              />
            </div>
            <div className="collapse-content flex gap-2 flex-wrap">
              {categories?.map((category) => (
                <CategoryLink
                  key={category.displayName}
                  categoryName={category.displayName}
                  categoryGroupItemsCount={category.totalPosts}
                  categoryGroupName={name}
                  anchorClassNames="btn-outline"
                />
              ))}
            </div>
          </div>
        ))}
      </div>
    </ContentPane>
  );
};

export default CategoryTree;
