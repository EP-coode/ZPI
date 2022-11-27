import classNames from "classnames";
import React, { useEffect, useState } from "react";
import { CategoryGroup } from "../model/CategoryGroup";
import { CategoryLink } from "./CategoryLink";

type Props = {
  categoryGroup: CategoryGroup;
};

export const CategoryTreeItem = ({
  categoryGroup: { displayName, postCategories, totalPosts },
}: Props) => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div
      tabIndex={0}
      className={classNames(
        "collapse collapse-arrow border border-base-300 bg-base-100 last:rounded-b-xl first:rounded-t-xl w-full cursor-pointer",
        {
          "collapse-open": isOpen,
          "collapse-close": !isOpen,
        }
      )}
    >
      <div
        className="collapse-title"
        onClick={() => setIsOpen((prev) => !prev)}
      >
        <CategoryLink
          key={displayName}
          categoryGroupItemsCount={totalPosts}
          categoryGroupName={displayName}
          anchorClassNames="btn-md btn-ghost"
        />
      </div>
      <div className="collapse-content flex gap-2 flex-wrap">
        {postCategories?.map((category) => (
          <CategoryLink
            key={category.displayName}
            categoryName={category.displayName}
            categoryGroupItemsCount={category.totalPosts}
            categoryGroupName={displayName}
            anchorClassNames="btn-outline"
          />
        ))}
      </div>
    </div>
  );
};
