import { NextPage } from "next";
import { useRouter } from "next/router";
import React from "react";
import { BreadCrumbs } from "../../../../src/components/BreadCrumbs";
import CategoryTree from "../../../../src/components/CategoryTree";
import PostFilterBox from "../../../../src/components/PostFilterBox";
import { PostFilterContextProvider } from "../../../../src/context/PostFilterContext";
import { CollumnWrapper } from "../../../../src/layout/CollumnWrapper";
import { ContentWrapper } from "../../../../src/layout/ContentWrapper";
import { LeftCollumn } from "../../../../src/layout/LeftCollumn";
import RightCollumn from "../../../../src/layout/RightCollumn";

const PostCategoryGroupPage: NextPage = () => {
  const router = useRouter();
  const { category_group, category } = router.query;

  const crumbs = [{ title: "Główna", href: "/" }];

  if (typeof category_group == "string") {
    crumbs.push({
      title: category_group,
      href: `/posts/${category_group}`,
    });
  }

  if (typeof category == "string") {
    crumbs.push({
      title: category,
      href: `/posts/category/${category}`,
    });
  }

  return (
    <ContentWrapper>
      <PostFilterContextProvider>
        <BreadCrumbs crumbs={crumbs} />
        <CollumnWrapper>
          <LeftCollumn>WYNIKI WYSZUKIWANIA</LeftCollumn>
          <RightCollumn>
            <PostFilterBox />
            <CategoryTree />
          </RightCollumn>
        </CollumnWrapper>
      </PostFilterContextProvider>
    </ContentWrapper>
  );
};

export default PostCategoryGroupPage;
