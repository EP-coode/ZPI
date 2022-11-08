import { NextPage } from "next";
import { useRouter } from "next/router";
import React from "react";

const PostDetailPage: NextPage = () => {
  const router = useRouter();
  const { post_id } = router.query;

  return <div>PostDetailPage {post_id}</div>;
};

export default PostDetailPage;