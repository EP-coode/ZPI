import React, {
  ChangeEvent,
  ReactNode,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from "react";
import { PostFilterContext } from "../context/PostFilterContext";
import ContentPane from "../layout/ContentPane";
import { PostOrdering } from "../services/interfaces/PostService";

import TagsPicker from "./TagsPicker";

type Props = {};

const PostFilterBox = (props: Props) => {
  const postFilterContext = useContext(PostFilterContext);

  const handleSelectMaxAgeChange = (e: ChangeEvent<HTMLSelectElement>) => {
    const selectedMaxAgeInDays = parseInt(e.target.value);

    postFilterContext?.setMaxPostAge(selectedMaxAgeInDays);
  };

  const handleSelectOrdering = (e: ChangeEvent<HTMLSelectElement>) => {
    const postOrdering = e.target.value as PostOrdering;
    postFilterContext?.setPostOrdering(postOrdering);
  };

  return (
    <ContentPane className="flex-col justify-center align-middle">
      <div
        tabIndex={0}
        className="collapse collapse-arrow border sm:border-none border-base-300 rounded-box sm:collapse-open w-full"
      >
        <input type="checkbox" />
        <h2 className="text-xl font-semibold collapse-title">Filtruj posty</h2>
        <div className="collapse-content flex flex-col max-w-xl gap-3 w-full">
          <TagsPicker
            noTagsSelectedMsg="Nie wybrano tagów do filtrowania"
            pickedLabel="Musi zazwierać tagi:"
            activeTags={postFilterContext.activeTagFilters}
            onAddTag={postFilterContext.addTag}
            onRemoveTag={postFilterContext.removeTag}
          />
          <div className="form-control w-full">
            <label className="label">
              <span className="label-text">Szukaj postów z okresu</span>
            </label>
            <select
              className="select select-bordered w-full mt-1"
              onChange={handleSelectMaxAgeChange}
              defaultValue={postFilterContext?.maxPostAgeInDays ?? 30}
            >
              <option value={1}>Ostatni dzień</option>
              <option value={7}>Ostatni tydzień</option>
              <option value={30}>Ostatni miesiąc</option>
              <option value={-1}>Wszyskie</option>
            </select>
          </div>
          <div className="form-control w-full">
            <label className="label">
              <span className="label-text">Sortuj po</span>
            </label>
            <select
              className="select select-bordered w-full mt-1"
              onChange={handleSelectOrdering}
              defaultValue={
                postFilterContext?.postOrdering ?? PostOrdering.LikesDsc
              }
            >
              <option value={PostOrdering.DateAsc}>Najstarsze</option>
              <option value={PostOrdering.DateDsc}>Najnowsze</option>
              <option value={PostOrdering.LikesAsc}>Najgorzej oceniane</option>
              <option value={PostOrdering.LikesDsc}>Najlepiej oceniane</option>
            </select>
          </div>
        </div>
      </div>
    </ContentPane>
  );
};

export default PostFilterBox;
