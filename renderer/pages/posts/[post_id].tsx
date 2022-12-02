import {
  GetServerSideProps,
  InferGetServerSidePropsType,
  NextPage,
} from "next";
import { useRouter } from "next/router";
import React, { Suspense, useEffect } from "react";
import { PostDetails } from "../../src/components/PostContent";
import PostInfo from "../../src/components/PostInfo";
import UserInfoCard from "../../src/components/PostAuthorInfo";
import { LeftCollumn } from "../../src/layout/LeftCollumn";
import RightCollumn from "../../src/layout/RightCollumn";
import { Post } from "../../src/model/Post";
import { postsService } from "../../src/services";
import { BreadCrumbs } from "../../src/components/BreadCrumbs";
import { ContentWrapper } from "../../src/layout/ContentWrapper";
import { CollumnWrapper } from "../../src/layout/CollumnWrapper";
import dynamic from "next/dynamic";
import LoadingPlaceholder from "../../src/components/LoadingPlaceholder";

const DynamicCommentSection = dynamic(
  () => import("../../src/components/comments/CommentSection"),
  {
    suspense: true,
  }
);

interface PostDetailPageProps {
  post: Post;
}

export const getServerSideProps: GetServerSideProps<
  PostDetailPageProps
> = async (context) => {
  try {
    const post_id = context.query["post_id"];

    if (!post_id || typeof post_id != "string") {
      return {
        notFound: true,
      };
    }

    const parsed_post_id = parseInt(post_id);
    const post = await postsService.getPost(parsed_post_id);

    return {
      props: {
        post,
      },
    };
  } catch (e) {
    return {
      notFound: true,
    };
  }
};
const PostDetailPage: NextPage<
  InferGetServerSidePropsType<typeof getServerSideProps>
> = ({ post }) => {
  const crumbs = [
    { title: "Główna", href: "/" },
    {
      title: post.category.postCategoryGroup.displayName,
      href: `/posts/category/${post.category.postCategoryGroup.displayName}`,
    },
    {
      title: post.category.displayName,
      href: `/posts/category/${post.category.postCategoryGroup.displayName}/${post.category.displayName}`,
    },
  ];

  return (
    <ContentWrapper>
      <BreadCrumbs crumbs={crumbs} />
      <CollumnWrapper>
        <LeftCollumn>
          <PostDetails
            contentMarkdown={post.markdownContent}
            benerImageUrl={post.imageUrl}
            title={post.title}
            postId={post.postId}
            totalLikes={post.totalLikes - post.totalDislikes}
            isLiked={post.isLiked}
          />
          <Suspense fallback={<LoadingPlaceholder/>}>
            <DynamicCommentSection postId={post.postId} commentsPerPage={10} />
          </Suspense>
        </LeftCollumn>
        <RightCollumn>
          <UserInfoCard user={post.author} />
          <PostInfo post={post} />
        </RightCollumn>
      </CollumnWrapper>
    </ContentWrapper>
  );
};

export default PostDetailPage;
