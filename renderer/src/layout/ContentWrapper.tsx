import React, { PropsWithChildren } from "react";

export const ContentWrapper = ({ children }: PropsWithChildren) => {
  return <div className="flex flex-col justify-center max-w-6xl mx-auto px-0 py-7">{children}</div>;
};
