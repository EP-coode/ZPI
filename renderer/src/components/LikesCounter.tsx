import classNames from "classnames";
import React, { useContext, useEffect, useState } from "react";
import ArrowSvg from "../icons/ArrowSvg";
import { LikeOrDislike } from "../services/interfaces/LikeService";
import { fetchWithJWT } from "../utils/fetchWithJWT";
import { Post } from "../model/Post";
import { isLoggedIn } from "../utils/auth";
import { ModalContext } from "../context/ModalContext";
import { useRouter } from "next/router";
import PostDetailPage from "../../pages/posts/[post_id]";

type Props = {
  className?: string;
  variant?: "BIG" | "SMALL";
  resourceId: number;
  isLiked: boolean | null;
  totalLikes: number;
  onLike?: (x: number) => Promise<LikeOrDislike>;
  onDisLike?: (x: number) => Promise<LikeOrDislike>;
  setIsLiked: (x: number) => Promise<boolean | null>;
};

export const LikesCounter = ({
  variant = "BIG",
  resourceId,
  totalLikes,
  onLike,
  onDisLike,
  setIsLiked,
  className,
}: Props) => {
  const [disliked, setDisliked] = useState<boolean>(false);
  const [liked, setLiked] = useState<boolean>(false);
  const [likesCount, setLikesCount] = useState<number>(totalLikes);
  const modalContext = useContext(ModalContext);
  const router = useRouter();

  const likeOrDislikeButton = async (like: boolean) => {
    if (!isLoggedIn()) {
      modalContext.setupModal(
        null,
        "Musisz być zalogowany, aby dokonywać oceny",
        true,
        [{ label: "Zaloguj", onClick: () => router.push("/login") }]
      );
      modalContext.show();
      return;
    }

    try {
      const response = like ? onLike?.(resourceId) : onDisLike?.(resourceId);
      if (response == undefined) {
        throw new Error("Coś poszło nie tak");
      }

      const result = await response;
      setLikesCount(result.totalLikes);
      setDisliked(typeof result.isLiked == "boolean" && !result.isLiked);
      setLiked(result.isLiked ?? false);
    } catch (e: any) {
      console.error(e);
    }
  };

  useEffect(() => {
    if (!isLoggedIn()) return;

    const updateIsLiked = async () => {
      const isLiked = await setIsLiked(resourceId);
      setDisliked(typeof isLiked == "boolean" && !isLiked);
      setLiked(isLiked ?? false);
    };
    updateIsLiked().catch((e) => console.log("użytkownik nie zalogowany"));
  }, [resourceId]);

  return (
    <div
      className={classNames(
        `flex flex-wrap bg-base-300 rounded-xl w-full items-center overflow-hidden ${className}`,
        {
          "h-10 text-xl": variant == "BIG",
          "h-8 text-md": variant == "SMALL",
        }
      )}
    >
      <button
        onClick={() => likeOrDislikeButton(false)}
        className={classNames(
          `flex h-full text-red-700 bg-base-300 border-none rounded-r-none`,
          {
            "transition-transform hover:translate-y-1 ease-in": !disliked,
            "p-2": variant == "BIG",
            "p-1.5": variant == "SMALL",
          }
        )}
      >
        <ArrowSvg
          className={classNames("rotate-180", {
            "fill-transparent": !disliked,
          })}
        />
      </button>
      <div className="flex grow mx-2">
        <div className="w-full text-center select-none">{likesCount}</div>
      </div>
      <button
        onClick={() => likeOrDislikeButton(true)}
        className={classNames(
          `flex h-full text-green-700 bg-base-300 border-none rounded-r-none p-2 `,
          {
            "transition-transform hover:-translate-y-1 ease-in": !liked,
            "p-2": variant == "BIG",
            "p-1.5": variant == "SMALL",
          }
        )}
      >
        <ArrowSvg
          className={classNames({
            "fill-transparent": !liked,
          })}
        />
      </button>
    </div>
  );
};
