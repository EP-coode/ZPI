import React, { PropsWithChildren } from "react";

type Props = {};

const ContentWrapper = ({ children }: PropsWithChildren) => {
  return <div className="max-w-6xl mx-auto px-0 md:px-3 py-7">{children}</div>;
};

export default ContentWrapper;
