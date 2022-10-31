import {
  GetServerSideProps,
  InferGetServerSidePropsType,
  NextPage,
} from "next";
import { useRouter } from "next/router";
import React from "react";
import { PostDetails } from "../../src/components/PostContent";
import PostInfo from "../../src/components/PostInfo";
import UserInfoCard from "../../src/components/PostAuthorInfo";
import ContentWrapper from "../../src/layout/CollumnWrapper";
import { LeftCollumn } from "../../src/layout/LeftCollumn";
import RightCollumn from "../../src/layout/RightCollumn";
import { Post } from "../../src/model/Post";
import { postsService } from "../../src/services";

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
  const router = useRouter();
  const { post_id } = router.query;

  return (
    <ContentWrapper>
      <LeftCollumn>
        <PostDetails
          contentMarkdown={post.markdownContent}
          benerImageUrl={post.imageUrl}
          title={post.title}
          postId={post.postId}
        />
      </LeftCollumn>
      <RightCollumn>
        <UserInfoCard user={post.author} />
        <PostInfo />
      </RightCollumn>
    </ContentWrapper>
  );
};

export default PostDetailPage;
