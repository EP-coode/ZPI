import React, { useState } from "react";
import Image from "next/image";
import ReactMarkdown from "react-markdown";
import { LikesCounter } from "./LikesCounter";
import ContentPane from "../layout/ContentPane";
import ramarkGfm from "remark-gfm";

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
    <ContentPane>
      <div className="flex flex-row items-center justify-center gap-7">
        <h1 className="text-3xl sm:text-5xl font-semibold grow">{title}</h1>
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
      <article className="p-7 prose max-w-full">
        <ReactMarkdown remarkPlugins={[ramarkGfm]}>
          {contentMarkdown}
        </ReactMarkdown>
      </article>
    </ContentPane>
  );
};
