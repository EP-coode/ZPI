import { NextPage } from "next";
import { useRouter } from "next/router";
import React from "react";
import CategoryTree from "../../../../../src/components/CategoryTree";
import PostFilterBox from "../../../../../src/components/PostFilterBox";
import { PostFilterContextProvider } from "../../../../../src/context/PostFilterContext";
import { CollumnWrapper } from "../../../../../src/layout/CollumnWrapper";
import { ContentWrapper } from "../../../../../src/layout/ContentWrapper";
import { LeftCollumn } from "../../../../../src/layout/LeftCollumn";
import RightCollumn from "../../../../../src/layout/RightCollumn";

const PostCategoryPage: NextPage = () => {
  const router = useRouter();
  const { category_group, category } = router.query;

  return (
    <ContentWrapper>
      <PostFilterContextProvider>
        <CollumnWrapper>
          <LeftCollumn>
            PostCategoryPage {`${category_group} -> ${category}`}
          </LeftCollumn>
          <RightCollumn>
            <PostFilterBox />
            <CategoryTree />
          </RightCollumn>
        </CollumnWrapper>
      </PostFilterContextProvider>
    </ContentWrapper>
  );
};

export default PostCategoryPage;
