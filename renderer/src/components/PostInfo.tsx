import Link from "next/link";
import React, { useEffect, useState, useContext } from "react";
import ContentPane from "../layout/ContentPane";
import { Post } from "../model/Post";
import { formatDate } from "../utils/dateFormating";
import { isLoggedIn, getUserId } from "../utils/auth";
import { postsService } from "../services/api/PostService";
import { ModalContext } from "../context/ModalContext";
import { useRouter } from "next/router";

type Props = {
  post: Post;
};

const PostInfo = ({ post }: Props) => {
  const [showDeleteButton, setShowDeleteButton] = useState<boolean>(false);
  const modalContext = useContext(ModalContext);
  const router = useRouter();

  const handleDeleteClick = async () => {
    await postsService.deletePost(post.postId).catch((e) => e.message);
    modalContext.setupModal("Sukces!", "Post został usunięty.", false, [
      { label: "Strona główna", onClick: () => router.push("/") },
      {
        label: "Strona profilowa",
        onClick: () => router.push(`/posts/user/${post.author.id}`),
        classNames: "btn-primary",
      },
    ]);
    modalContext.show();
  };

  useEffect(() => {
    if (isLoggedIn() && getUserId() == post.author.id) {
      setShowDeleteButton(true);
    }
  }, [post.author.id]);

  return (
    <ContentPane>
      <div className="w-fit flex flex-col flex-wrap justify-center items-center mx-auto gap-2">
        <h2 className="font-semibold text-xl mb-3">Informacje o poście</h2>
        <div className="w-full">
          <h3 className="font-semibold inline">Utworzono: </h3>
          {formatDate(post.creationTime)}
        </div>
        <div className="w-full">
          <h3 className="font-semibold inline">Tagi: </h3>
          {post.postTags.length > 0
            ? post.postTags.map((tag) => (
                <Link
                  href={`category/${post.category.postCategoryGroup?.displayName}/${post.category.displayName}?tags=${tag.tagName}`}
                  key={tag.tagName}
                >
                  <a className="btn btn-sm btn-outline m-1">
                    {tag.tagName}
                    <span className="badge ml-2">{tag.totalPosts}</span>
                  </a>
                </Link>
              ))
            : "Brak"}
        </div>
        <div className="flex-grow">
          {showDeleteButton && (
            <button
              className="btn btn-sm btn-error mr-auto ml-0 mt-5"
              onClick={handleDeleteClick}
            >
              USUŃ POST
            </button>
          )}
        </div>
      </div>
    </ContentPane>
  );
};

export default PostInfo;
