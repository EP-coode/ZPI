import React, { useState } from "react";
import Image from "next/image";
import ReactMarkdown from "react-markdown";
import { LikesCounter } from "./LikesCounter";

type Props = {
  postId: number;
  contentMarkdown: string;
  benerImageUrl: string | null;
  title: string;
};

export const PostDetails = ({
  contentMarkdown,
  title,
  benerImageUrl,
}: Props) => {
  const [imageLoaded, setImageLoaded] = useState(false);

  return (
    <div className="flex flex-col relative w-full bg-base-100 shadow-md rounded-md justify-center items-center p-7">
      <div className="flex flex-row items-center justify-center gap-7">
        <h1 className="text-5xl font-semibold grow">{title}</h1>
        <div className="shrink-0">
          <LikesCounter totalLikes={75} />
        </div>
      </div>
      {benerImageUrl && (
        <figure className="relative h-96 w-full flex-shrink-0 flex-grow-0 my-10">
          <Image
            className="object-contain"
            src={benerImageUrl}
            layout="fill"
            alt="Ikona postu"
            onLoad={() => setImageLoaded(true)}
          />
          {!imageLoaded && (
            <div className="bg-gray-300 animate-pulse w-full h-full"></div>
          )}
        </figure>
      )}
      <article className="p-7 prose">
        <ReactMarkdown>{contentMarkdown}</ReactMarkdown>
      </article>
    </div>
  );
};
