import { NextPage } from "next";
import { useRouter } from "next/router";
import React from "react";

const PostCreatorPage: NextPage = () => {
  const router = useRouter();
  const { creator } = router.query;

  return <div>Post creator page {creator}</div>;
};

export default PostCreatorPage;