import {
  GetServerSideProps,
  InferGetServerSidePropsType,
  NextPage,
} from "next";
import React from "react";
import PostEditor from "../../src/components/PostEditor";
import { ContentWrapper } from "../../src/layout/ContentWrapper";
import { CategoryGroup } from "../../src/model/CategoryGroup";
import { Post } from "../../src/model/Post";
import { categoryGroupService } from "../../src/services";

type PageProps = {
  categoryGroups: Required<CategoryGroup>[];
};

export const getServerSideProps: GetServerSideProps<PageProps> = async () => {
  const categoryGroups = await categoryGroupService.getCategoryGroups();

  return {
    props: {
      categoryGroups,
    },
  };
};

const CreatePostPage: NextPage<
  InferGetServerSidePropsType<typeof getServerSideProps>
> = ({ categoryGroups }) => {
  const handlePostSubmit = (post: Post) => {
    console.log(post);
  };
  return (
    <ContentWrapper>
      <PostEditor
        onPostSubmit={handlePostSubmit}
        categoryGroups={categoryGroups}
      />
    </ContentWrapper>
  );
};

export default CreatePostPage;
