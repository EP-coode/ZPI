import classNames from "classnames";
import React from "react";
import ArrowSvg from "../icons/ArrowSvg";

type Props = {
  isLiked?: boolean;
  totalLikes: number;
  onLike?: () => {};
  onDisLike?: () => {};
};

export const LikesCounter = ({
  totalLikes,
  onLike,
  onDisLike,
  isLiked,
}: Props) => {
  const isDisliked = typeof isLiked  == "boolean" && !isLiked;
  isLiked = isLiked ?? false;
  return (
    <div className="flex flex-wrap bg-base-300 rounded-xl w-fit h-10 text-xl items-center overflow-hidden">
      <button
        onClick={() => {
          onDisLike && onDisLike();
        }}
        className={classNames(
          `flex h-full text-red-700 bg-base-300 border-none rounded-r-none p-2 `,
          { "transition-transform hover:translate-y-1 ease-in": !isDisliked }
        )}
      >
        <ArrowSvg
          className={classNames("rotate-180",{
            "fill-transparent": !isDisliked,
          })}
        />
      </button>
      <div className="flex grow mx-2">
        <div className="w-full text-center select-none">{totalLikes}</div>
      </div>
      <button
        onClick={() => {
          onLike && onLike();
        }}
        className={classNames(
          `flex h-full text-green-700 bg-base-300 border-none rounded-r-none p-2 `,
          { "transition-transform hover:-translate-y-1 ease-in": !isLiked }
        )}
      >
        <ArrowSvg
          className={classNames({
            "fill-transparent": !isLiked,
          })}
        />
      </button>
    </div>
  );
};
