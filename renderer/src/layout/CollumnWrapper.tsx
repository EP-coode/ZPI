import React, { PropsWithChildren } from "react";

export const CollumnWrapper = ({ children }: PropsWithChildren) => {
  return (
    <div className="flex flex-wrap justify-center items-start">
      {children}
    </div>
  );
};
