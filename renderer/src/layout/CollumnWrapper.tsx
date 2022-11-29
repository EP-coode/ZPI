import React, { PropsWithChildren } from "react";

export const CollumnWrapper = ({ children }: PropsWithChildren) => {
  return (
    <div className="grid grid-cols-1 grid-rows-[auto_] md:grid-cols-[2fr_minmax(16rem,_1fr)]">
      {children}
    </div>
  );
};
