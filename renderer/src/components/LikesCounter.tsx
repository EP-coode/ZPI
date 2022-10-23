import classNames from "classnames";
import React from "react";

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
  return (
    <div className="flex flex-wrap bg-base-300 rounded-xl w-fit h-fit text-xl items-center overflow-hidden">
      <div className="flex w-4/12 h-full">
        <button
          onClick={() => {
            onDisLike && onDisLike();
          }}
          className={classNames(
            "btn no-animation w-full text-2xl text-red-700 rounded-r-none",
            {
              "pointer-events-none btn-active": !(isLiked ?? true),
              "btn-ghost": (isLiked ?? true),
            }
          )}
        >
          -
        </button>
      </div>
      <div className="flex w-4/12">
        <div className="w-full text-center select-none">{totalLikes}</div>
      </div>
      <div className="flex flex-col w-4/12 h-full">
        <button
          onClick={() => {
            onLike && onLike();
          }}
          className={classNames(
            "btn no-animation w-full text-2xl text-green-700 rounded-l-none",
            {
              "pointer-events-none btn-active":  (isLiked ?? false),
              "btn-ghost":  !(isLiked ?? false),
            }
          )}
        >
          +
        </button>
      </div>
    </div>
  );
};
