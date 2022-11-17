import { NextPage } from "next";
import { useRouter } from "next/router";
import React, { useContext, useMemo, useState } from "react";
import { BreadCrumbs } from "../../../../../src/components/BreadCrumbs";
import CategoryTree from "../../../../../src/components/CategoryTree";
import PostFilterBox from "../../../../../src/components/PostFilterBox";
import PostList from "../../../../../src/components/PostList";
import {
  PostFilterContext,
  PostFilterContextProvider,
} from "../../../../../src/context/PostFilterContext";
import { CollumnWrapper } from "../../../../../src/layout/CollumnWrapper";
import { ContentWrapper } from "../../../../../src/layout/ContentWrapper";
import { LeftCollumn } from "../../../../../src/layout/LeftCollumn";
import RightCollumn from "../../../../../src/layout/RightCollumn";
import {
  PostFilters,
  PostOrdering,
} from "../../../../../src/services/interfaces/PostService";

const POST_PER_PAGE = 7;

const PostCategoryPage: NextPage = () => {
  const [currnetPage, setCurrentPage] = useState<number>(0);
  const postFilterContext = useContext(PostFilterContext);
  const router = useRouter();
  let { category_group, category } = router.query;

  // only to match types
  if (typeof category != "string") category = "";

  const crumbs = [{ title: "Główna", href: "/" }];

  if (typeof category_group == "string" && category_group.length > 0) {
    crumbs.push({
      title: category_group,
      href: `/posts/category/${category_group}`,
    });

    if (typeof category == "string" && category.length > 0) {
      crumbs.push({
        title: category,
        href: `/posts/category/${category_group}/${category}`,
      });
    }
  }

  return (
    <ContentWrapper>
      <BreadCrumbs crumbs={crumbs} />
      <CollumnWrapper>
        <LeftCollumn>
          <PostList
            categoryGroupId={null}
            categoryId={category}
            tagNames={postFilterContext?.activeTagFilters ?? []}
            postPerPage={7}
            creatorId={null}
            maxPostDaysAge={postFilterContext?.maxPostAgeInDays ?? 30}
            orderBy={postFilterContext?.postOrdering ?? PostOrdering.LikesDsc}
          />
        </LeftCollumn>
        <RightCollumn className="m-2 md:m-0">
          <PostFilterBox />
          <CategoryTree />
        </RightCollumn>
      </CollumnWrapper>
    </ContentWrapper>
  );
};

export default PostCategoryPage;
