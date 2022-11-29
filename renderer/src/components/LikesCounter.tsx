import classNames from "classnames";
import React, { useContext, useEffect, useState } from "react";
import ArrowSvg from "../icons/ArrowSvg";
import { LikeOrDislike } from "../services/interfaces/LikeService";
import { fetchWithJWT } from "../utils/fetchWithJWT";
import { Post } from "../model/Post";
import { isLoggedIn } from "../utils/auth";
import { ModalContext } from "../context/ModalContext";
import { useRouter } from "next/router";

type Props = {
  postId: number;
  isLiked: boolean | null;
  totalLikes: number;
  onLike?: (x: number) => Promise<LikeOrDislike>;
  onDisLike?: (x: number) => Promise<LikeOrDislike>;
};

export const LikesCounter = ({
  postId,
  totalLikes,
  onLike,
  onDisLike,
}: Props) => {
  const [disliked, setDisliked] = useState<boolean>(false);
  const [liked, setLiked] = useState<boolean>(false);
  const [likesCount, setLikesCount] = useState<number>(totalLikes);
  const modalContext = useContext(ModalContext);
  const router = useRouter();

  const likeOrDislikeButton = async (like: boolean) => {    
    if (!isLoggedIn()) {
      modalContext.setupModal(
        "Musisz być zalogowany, aby dokonywać oceny",
        true,
        [{ label: "Zaloguj", onClick: () => router.push("/login") }]
      );
      modalContext.show();
      return;
    }

    try {
      const response = like ? onLike?.(postId) : onDisLike?.(postId);
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
    if(!isLoggedIn()) return;

    const setIsLiked = async () => {
      const post = await fetchWithJWT<Post>(`posts/${postId}`, {
        method: "GET",
      });
      setDisliked(typeof post.isLiked == "boolean" && !post.isLiked);
      setLiked(post.isLiked ?? false);
    };
    setIsLiked().catch((e) => console.log("użytkownik nie zalogowany"));
  }, [postId]);

  return (
    <div className="flex flex-wrap bg-base-300 rounded-xl w-fit h-10 text-xl items-center overflow-hidden">
      <button
        onClick={() => likeOrDislikeButton(false)}
        className={classNames(
          `flex h-full text-red-700 bg-base-300 border-none rounded-r-none p-2 `,
          { "transition-transform hover:translate-y-1 ease-in": !disliked }
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
          { "transition-transform hover:-translate-y-1 ease-in": !liked }
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
