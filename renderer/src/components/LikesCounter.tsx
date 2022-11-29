import classNames from "classnames";
import React, { useEffect, useState }from "react";
import ArrowSvg from "../icons/ArrowSvg";
import { LikeOrDislike } from "../services/interfaces/LikeService";
import { fetchWithJWT } from "../utils/fetchWithJWT";
import { Post } from "../model/Post";

type Props = {
  postId: number;
  isLiked: boolean | null;
  totalLikes: number;
  onLike?: (x: any) => Promise<LikeOrDislike>;
  onDisLike?: (x: any) => Promise<LikeOrDislike>;
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

  const likeOrDislikeButton = async (onLikeOrDislike: any) => {
    try{
      const response = onLikeOrDislike && onLikeOrDislike(postId);
      if(response != undefined){
        setLikesCount((await response).totalLikes)
        setDisliked(typeof (await response).isLiked  == "boolean" && !(await response).isLiked);
        setLiked((await response).isLiked ?? false);
      }
    }catch(e: any){
      window.alert("Musisz się zalogować aby oceniać posty!");
    }
  };

  useEffect(() =>{
    const setIsLiked = async () => {
      const post = await fetchWithJWT<Post>(`posts/${postId}`, {method: "GET"});
      setDisliked(typeof post.isLiked  == "boolean" && !post.isLiked);
      setLiked(post.isLiked ?? false);
    }
    setIsLiked().catch(e => console.log("użytkownik nie zalogowany"));
  }, []);

  return (
    <div className="flex flex-wrap bg-base-300 rounded-xl w-fit h-10 text-xl items-center overflow-hidden">
      <button
        onClick={() => { likeOrDislikeButton(onDisLike)}}
        className={classNames(
          `flex h-full text-red-700 bg-base-300 border-none rounded-r-none p-2 `,
          { "transition-transform hover:translate-y-1 ease-in": !disliked }
        )}
      >
        <ArrowSvg
          className={classNames("rotate-180",{
            "fill-transparent": !disliked,
          })}
        />
      </button>
      <div className="flex grow mx-2">
        <div className="w-full text-center select-none">{likesCount}</div>
      </div>
      <button
        onClick={() => {likeOrDislikeButton(onLike)}}
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
