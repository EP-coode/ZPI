import { useRouter } from "next/router";
import React, { createContext, useEffect, useState } from "react";
import { PostOrdering } from "../services/interfaces/PostService";

export interface IPostFilterContext {
  activeTagFilters: string[];
  postOrdering: PostOrdering;
  maxPostAgeInDays: number;
  setPostOrdering: (postOrder: PostOrdering) => void;
  setMaxPostAge: (postAgeInDays: number) => void;
  removeTag: (tagName: string) => void;
  addTag: (tagName: string) => void;
}

export const PostFilterContext = createContext<IPostFilterContext | null>(null);

export const PostFilterContextProvider = ({
  children,
}: React.PropsWithChildren) => {
  const [activeTagFilters, setActiveFilters] = useState<string[]>([]);
  const [postOrdering, setPostOrdering] = useState<PostOrdering>(
    PostOrdering.LikesDsc
  );
  
  const [maxPostAgeInDays, setMaxPostAgeInDays] = useState<number>(30);

  const router = useRouter();
  const { tags } = router.query;

  useEffect(() => {
    let parsedTags: string[] = [];

    if (typeof tags == "string") {
      parsedTags = tags.split(",").filter((s) => s.length > 0);
    } else if (tags) {
      parsedTags = tags;
    }

    setActiveFilters(parsedTags);
  }, [tags]);

  const handleAddTagFilter = (tagName: string) => {
    setActiveFilters((prev) => [...prev, tagName]);
  };

  const handlePostOrderingChange = (postOrdering: PostOrdering) => {
    setPostOrdering(postOrdering);
  };

  const handleMaxPostAgeInDaysChange = (maxPostAgeInDays: number) => {
    setMaxPostAgeInDays(maxPostAgeInDays);
  };

  const handleRemoveTagFilter = (tagName: string) => {
    const newActiveTagFilters = activeTagFilters.filter(
      (_tagName) => _tagName != tagName
    );
    setActiveFilters(newActiveTagFilters);

    const query = { ...router.query };

    if (newActiveTagFilters.length > 0) {
      query["tags"] = newActiveTagFilters.join(",");
    } else {
      delete query.tags;
    }

    router.replace(
      {
        pathname: router.pathname,
        query,
      },
      undefined,
      { shallow: true }
    );
  };

  return (
    <PostFilterContext.Provider
      value={{
        activeTagFilters: activeTagFilters,
        maxPostAgeInDays: maxPostAgeInDays,
        postOrdering: postOrdering,
        setMaxPostAge: setMaxPostAgeInDays,
        setPostOrdering: setPostOrdering,
        addTag: handleAddTagFilter,
        removeTag: handleRemoveTagFilter,
      }}
    >
      {children}
    </PostFilterContext.Provider>
  );
};
