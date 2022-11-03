import { useRouter } from "next/router";
import React, { createContext, useEffect, useState } from "react";

export interface IPostFilterContext {
  activeTagFilters: string[];
  removeTag: (tagName: string) => void;
  addTag: (tagName: string) => void;
}

export const PostFilterContext = createContext<IPostFilterContext | null>(null);

export const PostFilterContextProvider = ({
  children,
}: React.PropsWithChildren) => {
  const [activeTagFilters, setActiveFilters] = useState<string[]>([]);
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
        addTag: handleAddTagFilter,
        removeTag: handleRemoveTagFilter,
      }}
    >
      {children}
    </PostFilterContext.Provider>
  );
};
