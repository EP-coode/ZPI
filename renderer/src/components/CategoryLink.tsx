import Link from "next/link";
import React from "react";

type Props = {
  categoryGroupName: string;
  categoryGroupItemsCount?: number;
  categoryName?: string;
  categoryItemsCount?: number;
  anchorClassNames?: string;
};

export const CategoryLink = ({
  categoryGroupName,
  categoryName,
  anchorClassNames,
  categoryItemsCount,
  categoryGroupItemsCount,
}: Props) => {
  return (
    <Link
      href={`/posts/category/${categoryGroupName}/${categoryName ?? ""}`}
      key={categoryGroupName}
    >
      <a className={`btn btn-sm ${anchorClassNames}`}>
        {categoryName ?? categoryGroupName}
        <span className="badge ml-2">
          {categoryItemsCount ?? categoryGroupItemsCount ?? null}
        </span>
      </a>
    </Link>
  );
};
