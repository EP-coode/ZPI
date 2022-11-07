import React, {
  ChangeEvent,
  ReactNode,
  useContext
} from "react";
import { PostFilterContext } from "../context/PostFilterContext";
import CloseIcon from "../icons/CloseIcon";
import ContentPane from "../layout/ContentPane";
import { PostOrdering } from "../services/interfaces/PostService";

import { tagsService } from "../services"

type Props = {};

const PostFilterBox = (props: Props) => {
  const postFilterContext = useContext(PostFilterContext);
  const tagBadges: ReactNode[] = [];

  postFilterContext?.activeTagFilters.forEach((tagName) => {
    tagBadges.push(
      <li className="badge badge-info badge-lg gap-2 " key={tagName}>
        <CloseIcon onClick={() => postFilterContext?.removeTag(tagName)} />
        {tagName}
      </li>
    );
  });

  if (tagBadges.length == 0) {
    tagBadges.push(<div>Brak katywnych filtrów</div>);
  }

  const handleSelectMaxAgeChange = (e: ChangeEvent<HTMLSelectElement>) => {
    const selectedMaxAgeInDays = parseInt(e.target.value);
    postFilterContext?.setMaxPostAge(selectedMaxAgeInDays);
  };

  return (
    <ContentPane className="flex-col">
      <h2 className="text-xl font-semibold">Filtruj posty</h2>
      <div>
        <h3>Musi zazwierać tagi:</h3>
        <ul className="flex flex-wrap gap-1">{tagBadges}</ul>
        <div>
          <h3>Szukaj tagów</h3>
          <div>
            
          </div>
        </div>
        <div>
          <h3>Szukaj postów z:</h3>
          <select
            className="select select-bordered w-full max-w-xs"
            onSelect={handleSelectMaxAgeChange}
          >
            <option
              value={1}
              selected={postFilterContext?.maxPostAgeInDays == 1}
            >
              Ostatni dzień
            </option>
            <option
              value={7}
              selected={postFilterContext?.maxPostAgeInDays == 7}
            >
              Ostatni tydzień
            </option>
            <option
              value={30}
              selected={postFilterContext?.maxPostAgeInDays == 30}
            >
              Ostatni miesiąc
            </option>
            <option
              value={-1}
              selected={postFilterContext?.maxPostAgeInDays == -1}
            >
              Wszyskie
            </option>
          </select>
        </div>
        <div>
          <h3>Sortuj po:</h3>
          <select className="select select-bordered w-full max-w-xs">
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
