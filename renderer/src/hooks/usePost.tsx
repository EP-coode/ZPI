import React, { useEffect, useState } from "react";
import { Post } from "../model/Post";
import { PostFilters } from "../services/interfaces/PostService";
import { postsService } from "../services";

type Props = {
  page: number;
  filters: PostFilters;
  postsPerPage: number;
};

const usePosts = ({ page, filters,postsPerPage = 15 }: Props) : [Post[], number] => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [totalPosts, setTotalPosts] = useState(0);

  console.log("DUPA");
  
  useEffect(() => {
    let isCanceled = false;

    const f = async () => {
      const returnedPosts = await postsService.getPosts(
        { currentPage: page, postPerPage: postsPerPage },
        filters
      );

      if (!isCanceled) {
        setPosts(returnedPosts.posts);
        setTotalPosts(returnedPosts.postCount.itemsCount);
      }
    };

    f();

    return () => {
      isCanceled = true;
    };
  }, [page, postsPerPage, filters]);

  return [posts, totalPosts];
};

export default usePosts