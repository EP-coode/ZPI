import React, { useEffect, useMemo, useState } from "react";
import { LoadingState } from "../../common/LoadingState";
import { CreateCommentDto } from "../../dto/request/CreateCommentDto";
import { commentService } from "../../services/api/CommentService";
import { CommentOrdering } from "../../services/interfaces/CommentService";
import LoadingPlaceholder from "../LoadingPlaceholder";
import PaginationPicker from "../PaginationPicker";
import { Comment } from "../../model/Comment";
import CommentBox from "./CommentBox";
import CommentForm from "../CommentForm";
import { isLoggedIn } from "../../utils/auth";
import ContentPane from "../../layout/ContentPane";

type Props = {
  postId: number;
  commentsPerPage: number;
};

const CommentList = ({ postId, commentsPerPage }: Props) => {
  const [comments, setComments] = useState<Comment[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalComments, setTotalComments] = useState(0);
  const [commentLoadingState, setCommentLoadingState] = useState(
    LoadingState.IDDLE
  );
  const [showCommentForm, setShowCommentForm] = useState<boolean>(false);

  const updateComments = async () => {
    let isCanceled = false;
    setCommentLoadingState(LoadingState.LOADING);

    const f = async () => {
      const returnedComments = await commentService.getComments(
        postId,
        { currentPage, postPerPage: commentsPerPage }, // TODO: refactor naming
        CommentOrdering.DateDsc
      );

      if (!isCanceled) {
        setComments(returnedComments.comments);
        setTotalComments(returnedComments.totalComments);
        setCommentLoadingState(LoadingState.LOADED);
        console.log(comments);
        console.log(totalComments);
      }
    };

    f();

    return () => {
      isCanceled = true;
    };
  };

  const deleteComment = async (commentId: number) => {
    setComments(comments.filter((comment) => comment.commentId != commentId));
    setTotalComments(totalComments - 1);
    await commentService.deleteComment(postId, commentId);
    if (currentPage != 0) {
      setCurrentPage(0);
    } else if (totalComments > commentsPerPage) {
      await updateComments();
    }
  };

  const createComment = async (content: string) => {
    const comment = await commentService.createComment(postId, { content });
    if (currentPage != 0) {
      setCurrentPage(0);
    } else if (totalComments >= commentsPerPage) {
      await updateComments();
    } else {
      comments.unshift(comment);
      setTotalComments(totalComments + 1);
    }
  };

  useEffect(() => {
    updateComments();
  }, [currentPage]);

  useEffect(() => {
    if (isLoggedIn()) {
      setShowCommentForm(true);
    }
  }, []);

  const handlePageSelect = (page: number) => {
    setCurrentPage(page);
  };

  return (
    <ContentPane>
      <h2 className="text-4xl font-semibold w-full float-left mb-7">
        Komentarze
      </h2>
      {showCommentForm ? <CommentForm onSubmit={createComment} /> : <div></div>}
      {totalComments == 0 && LoadingState.LOADED == commentLoadingState && (
        <div className="w-full my-auto text-center">Brak komentarzy</div>
      )}
      <div className="mt-7 w-full">
        {LoadingState.LOADING == commentLoadingState && <LoadingPlaceholder />}
        {LoadingState.LOADED == commentLoadingState &&
          comments?.map((comment) => (
            <div className="w-full" key={comment.commentId}>
              <CommentBox comment={comment} onDelete={deleteComment} />
            </div>
          ))}
      </div>
      <PaginationPicker
        className="mb-0 mt-auto mx-auto"
        onPageSelect={handlePageSelect}
        currentPage={currentPage}
        totalPages={Math.ceil(totalComments / commentsPerPage)}
      />
    </ContentPane>
  );
};

export default CommentList;
