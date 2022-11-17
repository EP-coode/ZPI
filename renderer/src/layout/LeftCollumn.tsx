import React, { PropsWithChildren } from "react";

type Props = {};

export const LeftCollumn = ({ children }: PropsWithChildren) => {
  return <div className="h-full min-w-[20rem] grow py-1.5 md:px-1.5 gap-3 col-start-1 row-start-2 sm:row-start-1">{children}</div>;
};
