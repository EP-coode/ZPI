import classNames from "classnames";
import Image from "next/image";
import Link from "next/link";
import React from "react";
import ReactMarkdown from "react-markdown";
import { Post } from "../model/Post";
import { LikesCounter } from "./LikesCounter";

type Props = {
  post: Post;
};

const SmallPostCard = ({ post }: Props) => {
  return (
    <div className="card bg-base-100 shadow-xl h-full w-96 min-w-[20rem]">
      {post.imageUrl && (
        <figure className="relative h-40 flex-shrink-0 flex-grow-0">
          <Image
            className="object-cover"
            src={post.imageUrl}
            layout="fill"
            alt="Ikona postu"
          />
          <div className="absolute -bottom-5 right-4">
            <LikesCounter totalLikes={post.totalLikes - post.totalDislikes} />
          </div>
        </figure>
      )}
      <div
        className={classNames(
          "card-body flex-shrink flex-grow basis-0 p-5 pt-8",
          {
            "pt-20": !post.imageUrl,
          }
        )}
      >
        {!!post.imageUrl || (
          <div className="absolute top-5 right-5">
            <LikesCounter totalLikes={post.totalLikes - post.totalDislikes} />
          </div>
        )}
        <h2 className="card-title">{post.title}</h2>
        <h3>{post.author.email}</h3>
        <p className="relative overflow-y-clip flex-shrink flex-grow basis-0">
          <ReactMarkdown>{post.markdownContent}</ReactMarkdown>
          <div className="bg-gradient-to-t from-base-100 via-transparent to-transparent absolute top-0 w-full h-full"></div>
        </p>
        <div className="card-actions justify-end flex-none">
          <Link href={`/post/${post.postId}`}>
            <a className="btn btn-primary">Czytaj dalej</a>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default SmallPostCard;
