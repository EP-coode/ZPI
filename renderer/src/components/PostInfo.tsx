import Link from "next/link";
import React from "react";
import ContentPane from "../layout/ContentPane";
import { Post } from "../model/Post";
import { formatDate } from "../utils/dateFormating";

type Props = {
  post: Post;
};

const PostInfo = ({ post }: Props) => {
  return (
    <ContentPane>
      <div className="w-fit flex flex-col flex-wrap justify-center items-center mx-auto gap-2">
        <h2 className="font-semibold text-xl mb-3">Informacje o poście</h2>
        <div className="w-full">
          <h3 className="font-semibold inline">Utworzono: </h3>
          {formatDate(post.creationTime)}
        </div>
        <div className="w-full">
          <h3 className="font-semibold inline">Tagi: </h3>
          {post.tags.map((tag) => (
            <Link
              href={`category/${post.category.catyegoryGroup?.name}/${post.category.displayName}?tags=${tag.name}`}
              key={tag.name}
            >
              <a className="btn btn-sm btn-outline m-1">
                {tag.name}
                <span className="badge ml-2">{tag.totalPosts}</span>
              </a>
            </Link>
          ))}
        </div>
      </div>
    </ContentPane>
  );
};

export default PostInfo;
