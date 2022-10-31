import React, { PropsWithChildren } from "react";

type Props = {};

const ContentWrapper = ({ children }: PropsWithChildren) => {
  return (
    <div className="max-w-6xl mx-auto px-0 py-7 flex flex-wrap-reverse justify-center items-end">
      {children}
    </div>
  );
};

export default ContentWrapper;
