import { NextPage } from "next";
import { useRouter } from "next/router";
import React, { useContext, useMemo } from "react";
import { BreadCrumbs } from "../../../../../src/components/BreadCrumbs";
import CategoryTree from "../../../../../src/components/CategoryTree";
import PostFilterBox from "../../../../../src/components/PostFilterBox";
import SmallPostCard from "../../../../../src/components/SmallPostCard";
import {
  PostFilterContext,
  PostFilterContextProvider,
} from "../../../../../src/context/PostFilterContext";
import usePosts from "../../../../../src/hooks/usePost";
import { CollumnWrapper } from "../../../../../src/layout/CollumnWrapper";
import { ContentWrapper } from "../../../../../src/layout/ContentWrapper";
import { LeftCollumn } from "../../../../../src/layout/LeftCollumn";
import RightCollumn from "../../../../../src/layout/RightCollumn";
import {
  PostFilters,
  PostOrdering,
} from "../../../../../src/services/interfaces/PostService";

const PostCategoryPage: NextPage = () => {
  const postFilterContext = useContext(PostFilterContext);
  const router = useRouter();
  let { category_group, category } = router.query;


  // TODO: simplify it with REDUCERS
  const filters = useMemo<PostFilters>(() => {
    // only to match types
    if (typeof category_group != "string") {
      category_group = "";
    }

    if (typeof category != "string") {
      category = "";
    }
    return {
      categoryGroupId: category_group,
      categoryId: category,
      creatorId: null,
      maxPostDaysAge: postFilterContext?.maxPostAgeInDays ?? 30,
      orderBy: postFilterContext?.postOrdering ?? PostOrdering.LikesDsc,
      tagNames: postFilterContext?.activeTagFilters ?? [],
    };
  }, [
    category_group,
    category,
    postFilterContext?.maxPostAgeInDays,
    postFilterContext?.postOrdering,
    postFilterContext?.activeTagFilters,
  ]);

  const [posts, totalPost] = usePosts({
    page: 1,
    filters: filters,
    postsPerPage: 7,
  });

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
          <div className="flex flex-col gap-3">
            {posts.map((post) => (
              <div className="h-120 w-full" key={post.postId}>
                <SmallPostCard post={post} />
              </div>
            ))}
          </div>
        </LeftCollumn>
        <RightCollumn>
          <PostFilterBox />
          <CategoryTree />
        </RightCollumn>
      </CollumnWrapper>
    </ContentWrapper>
  );
};

export default PostCategoryPage;
