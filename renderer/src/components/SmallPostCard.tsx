import classNames from "classnames";
import Image from "next/image";
import Link from "next/link";
import React, { useState } from "react";
import ReactMarkdown from "react-markdown";
import { Post } from "../model/Post";
import { formatDate } from "../utils/dateFormating";
import { LikesCounter } from "./LikesCounter";

type Props = {
  post: Post;
  className?: string
};

const SmallPostCard = ({ post, className }: Props) => {
  const [imageLoaded, setImageLoaded] = useState(false);

  return (
    <div className={`card relative bg-base-100 shadow-md h-full min-w-[20rem] ${className}`}>
      <div className="absolute top-2 right-2 z-20 badge badge-ghost">
        {formatDate(post.creationTime)}
      </div>
      {post.imageUrl && (
        <figure className="relative h-2/5 flex-shrink-0 flex-grow-0">
          <Image
            className="object-cover"
            src={post.imageUrl}
            layout="fill"
            alt="Ikona postu"
            onLoad={() => setImageLoaded(true)}
          />
          <div className="absolute -bottom-4 right-4 z-20">
            <LikesCounter totalLikes={post.totalLikes - post.totalDislikes} />
          </div>
          {!imageLoaded && (
            <div className="bg-gray-300 animate-pulse w-full h-full z-10"></div>
          )}
        </figure>
      )}
      <div
        className={classNames(
          "card-body flex-shrink flex-grow basis-0 p-5 pt-8",
          {
            "pt-12": !post.imageUrl,
          }
        )}
      >
        {!!post.imageUrl || (
          <div className="self-end">
            <LikesCounter totalLikes={post.totalLikes - post.totalDislikes} />
          </div>
        )}
        <h2 className="card-title">{post.title}</h2>
        <div>
          Autor:
          <Link href={`/posts/user/${post.author.id}`}>
            <a className="btn btn-sm btn-ghost self-start w-fit ml-1">
              {post.author.email}
            </a>
          </Link>
        </div>
        <article className="relative overflow-y-clip flex-shrink flex-grow basis-0 min-h-0">
          <ReactMarkdown>{post.markdownContent}</ReactMarkdown>
          <div className="bg-gradient-to-t from-base-100 via-transparent to-transparent absolute top-0 w-full h-full"></div>
        </article>
        <div className="card-actions justify-end flex-none">
          <Link href={`/posts/${post.postId}`}>
            <a className="btn btn-primary btn-md">Czytaj dalej</a>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default SmallPostCard;
