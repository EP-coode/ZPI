import {
  GetServerSideProps,
  InferGetServerSidePropsType,
  NextPage,
} from "next";
import React from "react";
import PostEditor from "../../src/components/PostEditor";
import ContentPane from "../../src/layout/ContentPane";
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
      <ContentPane>
        <h2 className="text-2xl font-semibold m-3">Utwórz post</h2>
        <PostEditor
          onPostSubmit={handlePostSubmit}
          categoryGroups={categoryGroups}
        />
      </ContentPane>
    </ContentWrapper>
  );
};

export default CreatePostPage;
