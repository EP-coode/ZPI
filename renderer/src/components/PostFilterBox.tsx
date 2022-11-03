import React, { ReactNode, useContext, useEffect, useState } from "react";
import { PostFilterContext } from "../context/PostFilterContext";
import CloseIcon from "../icons/CloseIcon";
import ContentPane from "../layout/ContentPane";

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

  return (
    <ContentPane className="flex-col">
      <h2 className="text-xl font-semibold">Filtruj po tagach</h2>
      <div>
        <h3>Aktywne filtry</h3>
        <ul className="flex flex-wrap gap-1">{tagBadges}</ul>
        <div>
          <h3>Szukaj tagów</h3>
          <div>Wyszukiwarka tagów ...</div>
        </div>
      </div>
    </ContentPane>
  );
};

export default PostFilterBox;
