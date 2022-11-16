import React, {
  ChangeEvent,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";
import { PostFilterContext } from "../context/PostFilterContext";
import CloseIcon from "../icons/CloseIcon";
import ContentPane from "../layout/ContentPane";
import { PostOrdering } from "../services/interfaces/PostService";

import { tagsService } from "../services";
import { Tag } from "../model/Tag";
import PlusIcon from "../icons/PlusIcon";

type Props = {};

const PostFilterBox = (props: Props) => {
  const postFilterContext = useContext(PostFilterContext);
  const [tagFilterPrefix, setTagFilterPrefix] = useState<string>("");
  const [tagSearchResult, setTagSearchResult] = useState<string[]>([]);
  const tagBadges: ReactNode[] = [];
  const searchResultTagBadges: ReactNode[] = [];

  postFilterContext?.activeTagFilters.forEach((tagName) => {
    tagBadges.push(
      <li className="badge badge-primary badge-lg gap-2 " key={tagName}>
        <CloseIcon onClick={() => postFilterContext?.removeTag(tagName)} />
        {tagName}
      </li>
    );
  });

  tagSearchResult?.forEach((tagName) => {
    searchResultTagBadges.push(
      <li className="badge badge-secondary badge-lg gap-2" key={tagName}>
        {tagName}
        <PlusIcon
          onClick={() => postFilterContext?.addTag(tagName)}
          className="cursor-pointer"
        />
      </li>
    );
  });

  useEffect(() => {
    let isCanceled = false;

    const f = async () => {
      const tags = await tagsService.getTagsByPrefix(tagFilterPrefix, 15);
      const newTagsResult = tags.tags
        .map((tag) => tag.name)
        .filter(
          (tag) =>
            !postFilterContext?.activeTagFilters.some(
              (activeTag) => activeTag.toLowerCase() == tag.toLowerCase()
            )
        );

      if (!isCanceled) {
        setTagSearchResult(newTagsResult);
      }
    };

    f();

    return () => {
      isCanceled = true;
    };
  }, [postFilterContext?.activeTagFilters, tagFilterPrefix]);

  if (tagBadges.length == 0) {
    tagBadges.push(<div key="none">Brak atywnych filtrów</div>);
  }

  if (tagSearchResult.length == 0) {
    searchResultTagBadges.push(<div key="none">Brak wyników</div>);
  }

  const handleSelectMaxAgeChange = (e: ChangeEvent<HTMLSelectElement>) => {
    const selectedMaxAgeInDays = parseInt(e.target.value);
    console.log("DUPA");
    
    postFilterContext?.setMaxPostAge(selectedMaxAgeInDays);
  };

  const handleSelectOrdering = (e: ChangeEvent<HTMLSelectElement>) => {
    const postOrdering = e.target.value as PostOrdering;
    console.log("DUP DUP");
    postFilterContext?.setPostOrdering(postOrdering);
  };

  return (
    <ContentPane className="flex-col justify-center align-middle">
      <h2 className="text-xl font-semibold">Filtruj posty</h2>
      <div className="flex flex-col max-w-xl gap-3 w-full mt-3">
        <div>
          <h3 className="font-semibold">Musi zazwierać tagi:</h3>
          <ul className="flex flex-wrap gap-2 mt-1">{tagBadges}</ul>
        </div>
        <div>
          <h3 className="font-semibold">Dodaj tagi do wyszukiwania:</h3>
          <input
            placeholder="Wyszukaj tagi po nazwie"
            type="text"
            onChange={(e) => setTagFilterPrefix(e.target.value)}
            value={tagFilterPrefix}
            className="input input-bordered w-full mt-1"
          />
          <ul className="flex flex-wrap gap-2 mt-3">{searchResultTagBadges}</ul>
        </div>
        <div>
          <h3 className="font-semibold">Szukaj postów z:</h3>
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
        <div>
          <h3 className="font-semibold">Sortuj po:</h3>
          <select
            className="select select-bordered w-full mt-1"
            onChange={handleSelectOrdering}
            defaultValue={postFilterContext?.postOrdering ?? PostOrdering.LikesDsc}
          >
            <option value={PostOrdering.DateAsc}>Najstarsze</option>
            <option value={PostOrdering.DateDsc}>Najnowsze</option>
            <option value={PostOrdering.LikesAsc}>Najgorzej oceniane</option>
            <option value={PostOrdering.LikesDsc}>Najlepiej oceniane</option>
          </select>
        </div>
      </div>
    </ContentPane>
  );
};

export default PostFilterBox;
