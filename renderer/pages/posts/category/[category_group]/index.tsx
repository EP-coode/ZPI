import {
  GetServerSideProps,
  InferGetServerSidePropsType,
  NextPage,
} from "next";
import { useRouter } from "next/router";
import React, { useContext } from "react";
import { BreadCrumbs } from "../../../../src/components/BreadCrumbs";
import CategoryTree from "../../../../src/components/CategoryTree";
import PostFilterBox from "../../../../src/components/PostFilterBox";
import PostList from "../../../../src/components/PostList";
import { PostFilterContext, PostFilterContextProvider } from "../../../../src/context/PostFilterContext";
import { CollumnWrapper } from "../../../../src/layout/CollumnWrapper";
import { ContentWrapper } from "../../../../src/layout/ContentWrapper";
import { LeftCollumn } from "../../../../src/layout/LeftCollumn";
import RightCollumn from "../../../../src/layout/RightCollumn";
import { PostOrdering } from "../../../../src/services/interfaces/PostService";

type Crumbs = { title: string; href: string };

type PageProps = {
  crumbs: Crumbs[];
  category_group: string;
};

export const getServerSideProps: GetServerSideProps<PageProps> = async (
  context
) => {
  let category_group = context.params?.category_group;

  // only to match types
  if (typeof category_group != "string") category_group = "";

  const crumbs: Crumbs[] = [{ title: "Główna", href: "/" }];

  if (typeof category_group == "string" && category_group.length > 0) {
    crumbs.push({
      title: category_group,
      href: `/posts/category/${category_group}`,
    });
  }

  return {
    props: {
      crumbs,
      category_group,
    },
  };
};

const POST_PER_PAGE = 7;

const PostCategoryGroupPage: NextPage<
  InferGetServerSidePropsType<typeof getServerSideProps>
> = ({ crumbs, category_group }) => {
  const postFilterContext = useContext(PostFilterContext);

  return (
    <ContentWrapper>
      <BreadCrumbs crumbs={crumbs} />
      <CollumnWrapper>
        <LeftCollumn>
          <PostList
            categoryGroupId={category_group}
            categoryId={null}
            tagNames={postFilterContext?.activeTagFilters ?? []}
            postPerPage={POST_PER_PAGE}
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

export default PostCategoryGroupPage;
