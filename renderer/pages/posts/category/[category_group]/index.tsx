import { NextPage } from "next";
import { useRouter } from "next/router";
import React from "react";

const PostCategoryGroupPage: NextPage = () => {
  const router = useRouter();
  const { category_group } = router.query;

  return <div>PostCategoryGroupPage {category_group}</div>;
};

export default PostCategoryGroupPage