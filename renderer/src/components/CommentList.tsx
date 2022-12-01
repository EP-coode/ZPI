import React, { useEffect, useMemo, useState } from "react";
import { LoadingState } from "../common/LoadingState";
import { CreateCommentDto } from "../dto/request/CreateCommentDto";
import {commentService} from "../services/api/CommentService"
import { CommentOrdering } from "../services/interfaces/CommentService";
import LoadingPlaceholder from "./LoadingPlaceholder";
import PaginationPicker from "./PaginationPicker";
import { Comment } from "../model/Comment";
import CommentBox from "./CommentBox";
import CommentForm from "./CommentForm";
import { isLoggedIn } from "../utils/auth";

type Props = {
  postId: number;
  postPerPage: number;
}


const CommentList = ({postId, postPerPage}: Props) =>{
    const [comments, setComments] = useState<Comment[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalComments, setTotalComments] = useState(0);
    const [commentLoadingState, setCommentLoadingState] = useState(LoadingState.IDDLE);
    const [showCommentForm, setShowCommentForm] = useState<boolean>(false);

    const updateComments = async () =>{
      const returnedComments = await commentService.getComments(
        postId,
        { currentPage, postPerPage },
      CommentOrdering.DateDsc);
      setComments(returnedComments.comments);
      setTotalComments(returnedComments.totalComments);
    };

    const deleteComment = async (commentId: number) =>{
      setComments(comments.filter(comment => comment.commentId != commentId))
      setTotalComments(totalComments - 1)
      const result = await commentService.deleteComment(postId, commentId);
      console.log(result);
      if(currentPage != 0 && comments.length == 1){
        setCurrentPage(currentPage-1);
      }
      else{
        await updateComments();
      }
    };

    const createComment = async (content: string) =>{
      const comment = await commentService.createComment(postId, {content});
      if(currentPage != 0){
        setCurrentPage(0);
      }
      else{
        comments.unshift(comment);
        setTotalComments(totalComments + 1);
        if(totalComments >= postPerPage){
          comments.pop();
        }
      }
    };

    useEffect(() => {
      let isCanceled = false;
      setCommentLoadingState(LoadingState.LOADING);
  
      const f = async () => {
        const returnedComments = await commentService.getComments(
          postId,
          { currentPage, postPerPage },
          CommentOrdering.DateDsc);
  
        if (!isCanceled) {
          setComments(returnedComments.comments)
          setTotalComments(returnedComments.totalComments);
          setCommentLoadingState(LoadingState.LOADED);
        }
      };
      
      f();
  
      return () => {
        isCanceled = true;
      };
    }, [currentPage]);

    useEffect(() => {
      if(isLoggedIn()){
        setShowCommentForm(true);
      }       
    },[]);

    const handlePageSelect = (page: number) => {
        setCurrentPage(page);
    };

    return (
        <div className="flex flex-col gap-5 m-2 md:m-0 min-h-max">
          {showCommentForm ? (
            <CommentForm onSubmit={createComment}/>
          ) : (
            <div></div>
          )}
          {totalComments == 0 && LoadingState.LOADED == commentLoadingState && (
            <div className="w-full my-auto text-center">Brak komentarzy</div>
          )}
          {LoadingState.LOADING == commentLoadingState && <LoadingPlaceholder />}
          {LoadingState.LOADED == commentLoadingState &&
            comments?.map((comment) => (
              <div className="w-full" key={comment.commentId}>
                <CommentBox comment={comment} onDelete={deleteComment} />
              </div>
            ))}
          <PaginationPicker
            className="mb-0 mt-auto mx-auto"
            onPageSelect={handlePageSelect}
            currentPage={currentPage}
            totalPages={Math.ceil(totalComments / postPerPage)}
          />
        </div>
      );

};

export default CommentList;