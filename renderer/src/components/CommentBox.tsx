import React, { useEffect, useMemo, useState } from "react";
import { Comment } from "../model/Comment";
import Link from "next/link";
import { formatDate } from "../utils/dateFormating";
import { LikesCounter } from "./LikesCounter";
import {likeService} from "../services/api/LikeService";
import { isLoggedIn, getUserId } from "../utils/auth";

type Props = {
    comment: Comment;
    onDelete: (x: number)  => {};
};

const CommentBox = ({comment, onDelete}: Props) => {
    const [showDeleteButton, setShowDeleteButton] = useState<boolean>(false);

    useEffect(() => {
        if(isLoggedIn() && getUserId() == comment.creator.id){
            setShowDeleteButton(true);
        }       
    },[]);

    return(
        <div className="card relative bg-base-100 shadow-md min-h-fit min-w-[20rem] overflow-auto">
            <div className="absolute top-2 right-2 z-20 badge badge-ghost">
                {formatDate(comment.creationTime)}
            </div>

            <div className="absolute top-1 left-1 z-20">
                <Link href={`/posts/user/${comment.creator.id}`}>
                    <a className="btn btn-sm btn-ghost self-start w-fit ml-1">
                        {comment.creator.name}
                    </a>
                </Link>
            </div>

            <div className="flex-shrink flex-row basis-0 p-5 pt-8">
                <h1 className="card-text prose pt-3 pl-3 break-all">{comment.content}</h1>
            </div>

            <div className="flex flex-row basis-0 p-5 items-center justify-left gap-7">
                <LikesCounter resourceId={comment.commentId} totalLikes={comment.totalLikes - comment.totalDislikes} isLiked={false}
                    onDisLike={likeService.DislikeComment} 
                    onLike={likeService.LikeComment} 
                    setIsLiked={likeService.IsCommentLiked} 
                />
                {showDeleteButton ? (
                    <button className="btn bg-red-500 shadow-md" type="button" onClick={async () => await onDelete(comment.commentId)}>
                        USUŃ
                     </button>
                ): (
                    <div></div>
                )}
            </div>
        </div>
    );
};

export default CommentBox;