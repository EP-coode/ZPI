import React, { useEffect, useMemo, useState } from "react";
import { LoadingState } from "../common/LoadingState";
import { Post } from "../model/Post";
import { postsService } from "../services";
import { Pagination } from "../services/interfaces/Pagination";
import { PostFilters, PostOrdering } from "../services/interfaces/PostService";
import LoadingPlaceholder from "./LoadingPlaceholder";
import PaginationPicker from "./PaginationPicker";
import SmallPostCard from "./SmallPostCard";

const PostList = ({
  categoryGroupId,
  categoryId,
  creatorId,
  maxPostDaysAge,
  orderBy,
  tagNames,
  postPerPage = 7,
}: PostFilters & { postPerPage: number }) => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPosts, setTotalPosts] = useState(0);
  const [postLoadingState, setPostLoadingState] = useState(LoadingState.IDDLE);

  useEffect(() => {
    let isCanceled = false;
    setPostLoadingState(LoadingState.LOADING);

    const f = async () => {
      const returnedPosts = await postsService.getPosts(
        { currentPage, postPerPage },
        {
          categoryGroupId,
          categoryId,
          creatorId,
          maxPostDaysAge,
          orderBy,
          tagNames,
        }
      );

      if (!isCanceled) {
        setPosts(returnedPosts.posts);
        setTotalPosts(returnedPosts.postCount.itemsCount);
        setPostLoadingState(LoadingState.LOADED);
      }
    };

    f();

    return () => {
      isCanceled = true;
    };
  }, [
    currentPage,
    postPerPage,
    categoryGroupId,
    categoryId,
    creatorId,
    maxPostDaysAge,
    orderBy,
    tagNames,
  ]);
  // TODO: remove this nasty dependency array

  return (
    <div className="flex flex-col gap-3 m-2 md:m-0 min-h-screen">
      {totalPosts == 0 && LoadingState.LOADED == postLoadingState && (
        <div className="w-full my-auto text-center">nie znaleziono postów</div>
      )}
      {LoadingState.LOADING == postLoadingState && <LoadingPlaceholder />}
      {posts.map((post) => (
        <div className="h-112 w-full" key={post.postId}>
          <SmallPostCard post={post} />
        </div>
      ))}
      <PaginationPicker
        className="mb-0 mt-auto mx-auto"
        onPageSelect={(page) => setCurrentPage(page)}
        currentPage={currentPage}
        totalPages={
          totalPosts / postPerPage + (totalPosts % postPerPage) == 0 ? 0 : 1
        }
      />
    </div>
  );
};

export default PostList;
