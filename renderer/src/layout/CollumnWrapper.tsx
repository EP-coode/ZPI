import React, { PropsWithChildren } from "react";

export const CollumnWrapper = ({ children }: PropsWithChildren) => {
  return (
    <div className="flex flex-wrap-reverse justify-center items-end">
      {children}
    </div>
  );
};
