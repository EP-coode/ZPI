import React, {
  ReactNode,
  useCallback,
  useEffect,
  useMemo,
  useState,
} from "react";
import { LoadingState } from "../common/LoadingState";
import CloseIcon from "../icons/CloseIcon";
import PlusIcon from "../icons/PlusIcon";
import { tagsService } from "../services";
import { debounce } from "../utils/debounce";
import LoadingPlaceholder from "./LoadingPlaceholder";

type Props = {
  pickedLabel: string;
  noTagsSelectedMsg: string
  onRemoveTag: (tagName: string) => void;
  onAddTag: (tagName: string) => void;
  activeTags: string[];
};

const TagsPicker = ({
  pickedLabel,
  activeTags,
  onAddTag,
  onRemoveTag,
  noTagsSelectedMsg
}: Props) => {
  const [tagSearchResult, setTagSearchResult] = useState<string[]>([]);
  const [tagSearchState, setTagSearchState] = useState(LoadingState.IDDLE);
  const [tagFilterPrefix, setTagFilterPrefix] = useState<string>("");

  const tagBadges: ReactNode[] = (() => {
    const tagBadges = activeTags.map((tagName) => (
      <li className="badge badge-primary badge-lg gap-2 " key={tagName}>
        <CloseIcon onClick={() => onRemoveTag(tagName)} />
        {tagName}
      </li>
    ));
    if (tagBadges.length == 0) {
      tagBadges.push(<div key="none" className="m-auto">{noTagsSelectedMsg}</div>);
    }
    return tagBadges;
  })();

  const searchResultTagBadges: ReactNode[] = (() => {
    const searchResultTagBadges = tagSearchResult
      .filter(
        (tag) =>
          !activeTags.some(
            (activeTag) => activeTag.toLowerCase() == tag.toLowerCase()
          )
      )
      .map((tagName) => (
        <li className="badge badge-secondary badge-lg gap-2" key={tagName}>
          {tagName}
          <PlusIcon
            onClick={() => {
              onAddTag(tagName);
              setTagSearchResult(
                tagSearchResult.filter((tag) => tag != tagName)
              );
            }}
            className="cursor-pointer"
          />
        </li>
      ));

    if (searchResultTagBadges.length == 0) {
      searchResultTagBadges.push(<div key="none" className="m-auto mt-3">Brak wyników</div>);
    }

    return searchResultTagBadges;
  })();

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
  }, [debouncedFetchTags, tagFilterPrefix]);

  return (
    <div className="flex flex-col max-w-xl gap-3 w-full">
      <div>
        <h3 className="font-semibold">{pickedLabel}</h3>
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
          <ul className="flex flex-wrap gap-2 mt-3">{searchResultTagBadges}</ul>
        )}
      </div>
    </div>
  );
};

export default TagsPicker;
