import type {
  GetServerSideProps,
  InferGetServerSidePropsType,
  NextPage,
} from "next";
import { categoryGroupService, postsService } from "../src/services";
import { Pagination } from "../src/services/interfaces/Pagination";
import CategoryGroupSlider from "../src/components/CategoryGroupSlider";
import { CountDown } from "../src/components/CountDown";
import { CategoryGroup } from "../src/model/CategoryGroup";
import { Post } from "../src/model/Post";
import RightCollumn from "../src/layout/RightCollumn";
import { LeftCollumn } from "../src/layout/LeftCollumn";
import { ContentWrapper } from "../src/layout/ContentWrapper";

const PAGE_SIZE = 3;

interface IndexPageProps {
  categoryGroups: CategoryGroup[];
  categoryGroupsPosts: { [key: string]: Post[] };
}

export const getServerSideProps: GetServerSideProps<
  IndexPageProps
> = async () => {
  const pagination: Pagination = {
    currentPage: 0,
    postPerPage: PAGE_SIZE,
  };

  const categoryGroups = await categoryGroupService.getCategoryGroups();
  const categoryGroupsPosts: { [key: string]: Post[] } = {};
  await Promise.all(
    categoryGroups.map(async (categoryGroup) => {
      const posts = await postsService.getPosts(pagination, {
        categoryGroup: categoryGroup.name,
        category: null,
        creatorId: null,
        tagNames: null,
      });
      categoryGroupsPosts[categoryGroup.name] = posts.posts;
    })
  );

  return {
    props: {
      categoryGroups,
      categoryGroupsPosts,
    },
  };
};

const Home: NextPage<
  InferGetServerSidePropsType<typeof getServerSideProps>
> = ({ categoryGroups, categoryGroupsPosts }) => {
  return (
    <ContentWrapper>
      <div className="relative flex flex-row justify-center items-center gap-7 p-7 bg-base-200 p- w-min mx-auto rounded-xl my-7">
        <h2 className="text-3xl writing-vertical sm:writing-normal">
          Do sesji pozostało
        </h2>
        <CountDown toDate={new Date("03/02/2023")} />
      </div>
      <div className="flex flex-col gap-7">
        {categoryGroups.map((categoryGroup) => (
          <CategoryGroupSlider
            categoryGroup={categoryGroup}
            categoryGroupPosts={categoryGroupsPosts[categoryGroup.name]}
            key={categoryGroup.name}
          />
        ))}
      </div>
    </ContentWrapper>
  );
};

export default Home;
