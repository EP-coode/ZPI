import Link from "next/link";
import React from "react";
import { CategoryGroup } from "../model/CategoryGroup";
import { Post } from "../model/Post";
import { CategoryLink } from "./CategoryLink";
import SmallPostCard from "./SmallPostCard";

type Props = {
  categoryGroup: CategoryGroup;
  categoryGroupPosts: Post[];
};

const CategoryGroupSlider = ({ categoryGroupPosts, categoryGroup }: Props) => {
  return (
    <div>
      <div className="px-3 xl:px-0">
        <h2 className="font-bold text-2xl mb-3">{categoryGroup.name}</h2>
        <div className="flex flex-row flex-wrap mb-3 gap-3 items-center">
          <h3>Kategorie:</h3>
          {categoryGroup.categories?.map((category) => (
            <CategoryLink
              key={category.displayName}
              categoryName={category.displayName}
              categoryGroupItemsCount={category.totalPosts}
              categoryGroupName={categoryGroup.name}
              anchorClassNames="btn-outline"
            />
          ))}
        </div>
      </div>
      <div className="h-120 flex flex-row gap-4 w-full overflow-x-auto min-w-full p-3">
        {categoryGroupPosts.map((post) => (
          <SmallPostCard post={post} key={post.postId} />
        ))}
      </div>
    </div>
  );
};

export default CategoryGroupSlider;
