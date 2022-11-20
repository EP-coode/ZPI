import { useRouter } from "next/router";
import React, { createContext, useEffect, useState } from "react";
import { useCallback } from "react";
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

const DEFAULT_FILTERS: IPostFilterContext = {
  activeTagFilters: [],
  postOrdering: PostOrdering.LikesDsc,
  maxPostAgeInDays: 30,
  setPostOrdering: () => {},
  setMaxPostAge: () => {},
  removeTag: () => {},
  addTag: () => {},
};

export const PostFilterContext =
  createContext<IPostFilterContext>(DEFAULT_FILTERS);

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
    if (!router.pathname.startsWith("/posts")) {
      return;
    }

    let parsedTags: string[] = [];

    if (typeof tags == "string") {
      parsedTags = tags.split(",").filter((s) => s.length > 0);
    } else if (tags) {
      parsedTags = tags;
    }

    setActiveFilters(parsedTags);
  }, [router.pathname, tags]);

  const handleAddTagFilter = useCallback(
    (tagName: string) => {
      const newActiveTagFilters = [...activeTagFilters, tagName];
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
    },
    [activeTagFilters, router]
  );

  const handlePostOrderingChange = useCallback((postOrdering: PostOrdering) => {
    setPostOrdering(postOrdering);
  }, []);

  const handleMaxPostAgeInDaysChange = useCallback(
    (maxPostAgeInDays: number) => {
      setMaxPostAgeInDays(maxPostAgeInDays);
    },
    []
  );

  const handleRemoveTagFilter = useCallback(
    (tagName: string) => {
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
    },
    [activeTagFilters, router]
  );

  return (
    <PostFilterContext.Provider
      value={{
        activeTagFilters: activeTagFilters,
        maxPostAgeInDays: maxPostAgeInDays,
        postOrdering: postOrdering,
        setMaxPostAge: handleMaxPostAgeInDaysChange,
        setPostOrdering: handlePostOrderingChange,
        addTag: handleAddTagFilter,
        removeTag: handleRemoveTagFilter,
      }}
    >
      {children}
    </PostFilterContext.Provider>
  );
};
