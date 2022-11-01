import React, { PropsWithChildren } from "react";

const ContentPane = ({ children }: PropsWithChildren) => {
  return (
    <div className="flex flex-wrap w-full bg-base-100 shadow-md rounded-md justify-center items-center mx-auto py-5 px-3">
      {children}
    </div>
  );
};

export default ContentPane;
