import React, { useEffect, useMemo, useState } from "react";
import { Comment } from "../../model/Comment";
import Link from "next/link";
import { formatDate } from "../../utils/dateFormating";
import { LikesCounter } from "../LikesCounter";
import { likeService } from "../../services/api/LikeService";
import { isLoggedIn, getUserId } from "../../utils/auth";
import { LoadingState } from "../../common/LoadingState";
import classNames from "classnames";

type Props = {
  comment: Comment;
  onDelete: (x: number) => Promise<any>;
};

const CommentBox = ({ comment, onDelete }: Props) => {
  const [showDeleteButton, setShowDeleteButton] = useState<boolean>(false);
  const [deleteStatus, setDeleteStatus] = useState(LoadingState.IDDLE);

  const handleDeleteClick = async () => {
    setDeleteStatus(LoadingState.LOADING);
    await onDelete(comment.commentId);
    setDeleteStatus(LoadingState.LOADED);
  };

  useEffect(() => {
    if (isLoggedIn() && getUserId() == comment.creator.id) {
      setShowDeleteButton(true);
    }
  }, [comment.creator.id]);

  return (
    <div>
      <div className="flex flex-row items-center gap-2">
        <div className="flex flex-row items-center flex-wrap gap-2">
          <Link href={`/posts/user/${comment.creator.id}`}>
            <a className="btn btn-sm btn-ghost text-md">
              {comment.creator.name}
            </a>
          </Link>
          <span className="badge badge-ghost ">
            {formatDate(comment.creationTime)}
          </span>
          <div className="flex-grow">
            {showDeleteButton && (
              <button
                className={classNames("btn btn-sm btn-error mr-auto ml-0", {
                  loading: deleteStatus == LoadingState.LOADING,
                })}
                onClick={handleDeleteClick}
              >
                USUŃ
              </button>
            )}
          </div>
        </div>
        <div className="mr-0 ml-auto basis-32">
          <LikesCounter
            resourceId={comment.commentId}
            totalLikes={comment.totalLikes - comment.totalDislikes}
            isLiked={false}
            onDisLike={likeService.DislikeComment}
            onLike={likeService.LikeComment}
            setIsLiked={likeService.IsCommentLiked}
            variant="SMALL"
          />
        </div>
      </div>

      <div className="prose mx-10 mt-5">{comment.content}</div>
    </div>
  );
};

export default CommentBox;
