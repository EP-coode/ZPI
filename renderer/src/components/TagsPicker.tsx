import { faPlus } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import classNames from "classnames";
import React, { ReactNode, useCallback, useEffect, useState } from "react";
import { LoadingState } from "../common/LoadingState";
import CloseIcon from "../icons/CloseIcon";
import { tagsService } from "../services";
import { debounce } from "../utils/debounce";
import LoadingPlaceholder from "./LoadingPlaceholder";

type Props = {
  pickedLabel: string;
  noTagsSelectedMsg: string;
  onRemoveTag: (tagName: string) => void;
  onAddTag: (tagName: string) => void;
  activeTags: string[];
  enableCustomTags?: boolean;
};

const TagsPicker = ({
  pickedLabel,
  activeTags,
  onAddTag,
  onRemoveTag,
  noTagsSelectedMsg,
  enableCustomTags = false,
}: Props) => {
  const [tagSearchResult, setTagSearchResult] = useState<string[]>([]);
  const [tagSearchState, setTagSearchState] = useState(LoadingState.IDDLE);
  const [tagFilterPrefix, setTagFilterPrefix] = useState<string>("");

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

  const handleAddNewTagClick = (tagName: string) => {
    onAddTag(tagName);
  };

  return (
    <div className="flex flex-col gap-3 w-full">
      <div>
        <h3 className="font-semibold">{pickedLabel}</h3>
        <ul className="flex flex-wrap gap-2 mt-1">
          {activeTags.map((tagName) => (
            <li className="badge badge-primary badge-lg gap-2 " key={tagName}>
              <CloseIcon onClick={() => onRemoveTag(tagName)} />
              {tagName}
            </li>
          ))}
          {activeTags.length == 0 && (
            <div className="m-auto">{noTagsSelectedMsg}</div>
          )}
        </ul>
      </div>
      <div className="min-h-48 flex flex-col align-middle">
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
            {tagSearchResult
              .filter(
                (tag) =>
                  !activeTags.some(
                    (activeTag) => activeTag.toLowerCase() == tag.toLowerCase()
                  )
              )
              .map((tagName) => (
                <li
                  className="badge badge-secondary badge-lg gap-2"
                  key={tagName}
                >
                  {tagName}
                  <FontAwesomeIcon
                    icon={faPlus}
                    onClick={() => {
                      onAddTag(tagName);
                      setTagSearchResult(
                        tagSearchResult.filter((tag) => tag != tagName)
                      );
                    }}
                    className="cursor-pointer"
                  />
                </li>
              ))}
            {tagSearchResult.length == 0 && (
              <div className="m-auto my-3">Brak wyników</div>
            )}
          </ul>
        )}
        {enableCustomTags && (
          <button
            className={classNames("btn mx-auto my-3", {
              "btn-disabled":
                LoadingState.LOADING == tagSearchState ||
                tagFilterPrefix.length < 3 ||
                tagSearchResult.some(
                  (tag) =>
                    tag.toLowerCase() == tagFilterPrefix.toLocaleLowerCase()
                ) ||
                activeTags.some(
                  (tag) =>
                    tag.toLowerCase() == tagFilterPrefix.toLocaleLowerCase()
                ),
            })}
            onClick={() => handleAddNewTagClick(tagFilterPrefix)}
            type="button"
          >
            Utwórz nowy tag
          </button>
        )}
      </div>
    </div>
  );
};

export default TagsPicker;
