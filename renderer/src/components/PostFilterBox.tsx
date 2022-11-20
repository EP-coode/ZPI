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
import CloseIcon from "../icons/CloseIcon";
import ContentPane from "../layout/ContentPane";
import { PostOrdering } from "../services/interfaces/PostService";

import { tagsService } from "../services";
import PlusIcon from "../icons/PlusIcon";
import { LoadingState } from "../common/LoadingState";
import LoadingPlaceholder from "./LoadingPlaceholder";
import { debounce } from "../utils/debounce";

type Props = {};

const PostFilterBox = (props: Props) => {
  const postFilterContext = useContext(PostFilterContext);
  const [tagFilterPrefix, setTagFilterPrefix] = useState<string>("");
  const [tagSearchResult, setTagSearchResult] = useState<string[]>([]);
  const [tagSearchState, setTagSearchState] = useState(LoadingState.IDDLE);
  const [isOpen, setIsOpen] = useState(true)

  const tagBadges: ReactNode[] = useMemo(() => {
    const tagBadges = postFilterContext.activeTagFilters.map((tagName) => (
      <li className="badge badge-primary badge-lg gap-2 " key={tagName}>
        <CloseIcon onClick={() => postFilterContext.removeTag(tagName)} />
        {tagName}
      </li>
    ));
    if (tagBadges.length == 0) {
      tagBadges.push(<div key="none">Brak atywnych filtrów</div>);
    }
    return tagBadges;
    // TODO: Why is this yieling at me ??
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [postFilterContext.removeTag, postFilterContext.activeTagFilters]);

  const searchResultTagBadges: ReactNode[] = useMemo(() => {
    const searchResultTagBadges = tagSearchResult
      .filter(
        (tag) =>
          !postFilterContext.activeTagFilters.some(
            (activeTag) => activeTag.toLowerCase() == tag.toLowerCase()
          )
      )
      .map((tagName) => (
        <li className="badge badge-secondary badge-lg gap-2" key={tagName}>
          {tagName}
          <PlusIcon
            onClick={() => {
              postFilterContext.addTag(tagName);
              setTagSearchResult(
                tagSearchResult.filter((tag) => tag != tagName)
              );
            }}
            className="cursor-pointer"
          />
        </li>
      ));

    if (searchResultTagBadges.length == 0) {
      searchResultTagBadges.push(<div key="none">Brak wyników</div>);
    }

    return searchResultTagBadges;
    // TODO: Why is this yieling at me ??
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [
    tagSearchResult,
    postFilterContext.activeTagFilters,
    postFilterContext.addTag,
  ]);

  const fetchTags = async (
    tagFilterPrefix: string,
    { isCanceled }: { isCanceled: boolean }
  ) => {

    const f = async () => {
      const tags = await tagsService.getTagsByPrefix(tagFilterPrefix, 15);
      const newTagsResult = tags.tags.map((tag) => tag.name);

      if (!isCanceled) {
        setTagSearchResult(newTagsResult);
        setTagSearchState(LoadingState.LOADED);
      }
    };

    f();
  };

  // eslint-disable-next-line react-hooks/exhaustive-deps
  const debouncedFetchTags = useCallback(debounce(fetchTags, 1000), []);

  useEffect(() => {
    // TRICK TO PASS BOLEAN AS REFENECE WE WRAP IT IN OBJECT
    let status = {
      isCanceled: false,
    };
    setTagSearchState(LoadingState.LOADING);
    debouncedFetchTags(tagFilterPrefix, status);

    return () => {
      status.isCanceled = true;
    };
    // TODO: można to rozwiązać jakoś sprytniej?
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [tagFilterPrefix]);

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
        <div className="flex flex-col max-w-xl gap-3 w-full collapse-content">
          <div>
            <h3 className="font-semibold">Musi zazwierać tagi:</h3>
            <ul className="flex flex-wrap gap-2 mt-1">{tagBadges}</ul>
          </div>
          <div className="min-h-48">
            <h3 className="font-semibold">Dodaj tagi do wyszukiwania:</h3>
            <input
              placeholder="Wyszukaj tagi po nazwie"
              type="text"
              onChange={(e) => setTagFilterPrefix(e.target.value)}
              value={tagFilterPrefix}
              className="input input-bordered w-full mt-1"
            />
            {tagSearchState == LoadingState.LOADING && (
              <LoadingPlaceholder className="w-32" />
            )}
            {tagSearchState == LoadingState.LOADED && (
              <ul className="flex flex-wrap gap-2 mt-3">
                {searchResultTagBadges}
              </ul>
            )}
          </div>
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
