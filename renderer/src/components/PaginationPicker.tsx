import classNames from "classnames";
import React from "react";

type Props = {
  onPageSelect: (pageNumber: number) => void;
  currentPage: number;
  totalPages: number;
  className?: string;
};

const PaginationPicker = ({ currentPage, totalPages, className }: Props) => {
  return (
    <div className={`btn-group ${className}`}>
      <button
        className={classNames("btn", { "btn-disabled": currentPage == 0 })}
      >
        «
      </button>
      <button className="btn btn-disabled ">Strona {currentPage + 1}</button>
      <button
        className={classNames("btn", {
          "btn-disabled": totalPages - 1 == currentPage,
        })}
      >
        »
      </button>
    </div>
  );
};

export default PaginationPicker;
