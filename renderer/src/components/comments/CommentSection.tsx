import React, { useEffect, useMemo, useState } from "react";
import { LoadingState } from "../../common/LoadingState";
import { commentService } from "../../services/api/CommentService";
import { CommentOrdering } from "../../services/interfaces/CommentService";
import LoadingPlaceholder from "../LoadingPlaceholder";
import PaginationPicker from "../PaginationPicker";
import { Comment } from "../../model/Comment";
import CommentComponent from "./Comment";
import CommentForm from "./CommentForm";
import ContentPane from "../../layout/ContentPane";
import { isLoggedIn } from "../../utils/auth";

type Props = {
  postId: number;
  commentsPerPage: number;
  refreshFrequencyMs?: number;
};

const CommentList = ({
  postId,
  commentsPerPage,
  refreshFrequencyMs = 3000,
}: Props) => {
  const [comments, setComments] = useState<Comment[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalComments, setTotalComments] = useState(0);
  const [commentLoadingState, setCommentLoadingState] = useState(
    LoadingState.IDDLE
  );
  const [showCommentForm, setShowCommentForm] = useState(false);

  const handlePageSelect = (page: number) => {
    setCurrentPage(page);
  };

  const deleteComment = async (commentId: number) => {
    await commentService.deleteComment(postId, commentId);
    const updatedComments = comments.filter((c) => c.commentId != commentId);
    if (updatedComments.length > 0) setComments(updatedComments) 
    else setCurrentPage(Math.max(currentPage - 1, 0));
  };

  const createComment = async (content: string) => {
    await commentService.createComment(postId, { content });
    if (currentPage != 0) setCurrentPage(0);
    const returnedComments = await commentService.getComments(
      postId,
      { currentPage, itemsPerPage: commentsPerPage }, // TODO: refactor naming on backend
      CommentOrdering.DateDsc
    );
    setComments(returnedComments.comments);
    setTotalComments(returnedComments.totalComments);
    setCommentLoadingState(LoadingState.LOADED);
  };

  useEffect(() => {
    let isCanceled = false;
    setCommentLoadingState(LoadingState.LOADING);

    const updateComments = async () => {
      const returnedComments = await commentService.getComments(
        postId,
        { currentPage, itemsPerPage: commentsPerPage }, // TODO: refactor naming on backend
        CommentOrdering.DateDsc
      );

      if (!isCanceled) {
        setComments(returnedComments.comments);
        setTotalComments(returnedComments.totalComments);
        setCommentLoadingState(LoadingState.LOADED);
      }
    };

    updateComments();
    const refreshInterval = setInterval(
      () => updateComments(),
      refreshFrequencyMs
    );

    return () => {
      isCanceled = true;
      clearInterval(refreshInterval);
    };
  }, [commentsPerPage, currentPage, postId, refreshFrequencyMs]);

  useEffect(() => {
    if (isLoggedIn()) {
      setShowCommentForm(true);
    }
  }, []);

  return (
    <ContentPane className="p-5">
      <h2 className="text-4xl font-semibold w-full float-left mb-7">
        Komentarze
      </h2>
      {showCommentForm && <CommentForm onSubmit={createComment} />}
      <div className="mt-7 w-full">
        {LoadingState.LOADING == commentLoadingState && <LoadingPlaceholder />}
        {LoadingState.LOADED == commentLoadingState &&
          comments?.map((comment, i) => (
            <div className="w-full" key={comment.commentId}>
              <CommentComponent comment={comment} onDelete={deleteComment} />
              {i != comments.length - 1 && <div className="divider"></div>}
            </div>
          ))}
        {totalComments == 0 && LoadingState.LOADED == commentLoadingState && (
          <div className="w-full my-auto text-center">Brak komentarzy</div>
        )}
      </div>
      <div className="mt-7">
        <PaginationPicker
          className="mb-0 mt-auto mx-auto"
          onPageSelect={handlePageSelect}
          currentPage={currentPage}
          totalPages={Math.ceil(totalComments / commentsPerPage)}
        />
      </div>
    </ContentPane>
  );
};

export default CommentList;
