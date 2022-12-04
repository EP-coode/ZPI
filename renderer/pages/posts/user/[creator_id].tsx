import {
  GetServerSideProps,
  InferGetServerSidePropsType,
  NextPage,
} from "next";
import React from "react";
import { BreadCrumbs, Crumb } from "../../../src/components/BreadCrumbs";
import UserInfoCard from "../../../src/components/PostAuthorInfo";
import PostList from "../../../src/components/PostList";
import { CollumnWrapper } from "../../../src/layout/CollumnWrapper";
import { ContentWrapper } from "../../../src/layout/ContentWrapper";
import { LeftCollumn } from "../../../src/layout/LeftCollumn";
import RightCollumn from "../../../src/layout/RightCollumn";
import { User } from "../../../src/model/User";
import { userService } from "../../../src/services";
import { PostOrdering } from "../../../src/services/interfaces/PostService";

type PageProps = {
  user: User;
  crumbs: Crumb[]
};

export const getServerSideProps: GetServerSideProps<PageProps> = async (
  context
) => {
  let creator_id = context.params?.creator_id;

  // only to match types
  if (typeof creator_id != "string") creator_id = "";

  const crumbs: Crumb[] = [{ title: "Główna", href: "/" }];

  const creator_id_parsed = parseInt(creator_id);

  if (isNaN(creator_id_parsed)) {
    return {
      notFound: true,
    };
  }

  const user = await userService.getUserDetails(creator_id_parsed);

  crumbs.push({title: `Posty użytkownika ${user.name}`, href: null})

  return {
    props: {
      user,
      crumbs
    },
  };
};

const POST_PER_PAGE = 7;

const PostCreatorPage: NextPage<
  InferGetServerSidePropsType<typeof getServerSideProps>
> = ({ user, crumbs }) => {
  return (
    <ContentWrapper>
       <BreadCrumbs crumbs={crumbs} />
      <CollumnWrapper>
        <LeftCollumn>
          <PostList
            categoryGroupId={null}
            categoryId={null}
            tagNames={null}
            postPerPage={POST_PER_PAGE}
            creatorId={user.id}
            maxPostDaysAge={-1}
            orderBy={PostOrdering.DateDsc}
          />
        </LeftCollumn>
        <RightCollumn>
          <UserInfoCard user={user} />
        </RightCollumn>
      </CollumnWrapper>
    </ContentWrapper>
  );
};

export default PostCreatorPage;
