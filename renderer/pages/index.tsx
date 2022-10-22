import type { NextPage } from "next";
import { useEffect, useState } from "react";
import { postsService } from "../src/api";
import { LikesCounter } from "../src/components/LikesCounter";
import SmallPostCard from "../src/components/SmallPostCard";
import { Post } from "../src/model/Post";

const Home: NextPage = () => {
  const [examplePost, setExamplePost] = useState<Post>();

  useEffect(() => {
    const loadPost = async () => {
      try {
        const post = await postsService.getPost(234234);
        setExamplePost(post);
      } catch (e) {
        console.error(e);
      }
    };
    loadPost();
  }, []);

  return (
    <div className="bg-base-300 min-h-screen p-10">
      <div className="h-96 flex flex-row gap-4 w-full overflow-x-auto min-w-full p-2">
        {examplePost && <SmallPostCard post={examplePost} />}
        {examplePost && <SmallPostCard post={examplePost} />}
        {examplePost && <SmallPostCard post={{...examplePost, imageUrl: undefined}} />}
      </div>
    </div>
  );
};

export default Home;
