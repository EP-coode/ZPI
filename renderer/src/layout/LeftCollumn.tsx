import React, { PropsWithChildren } from "react";

type Props = {};

export const LeftCollumn = ({ children }: PropsWithChildren) => {
  return <div className="h-full w-2/3 min-w-[20rem] grow py-3 md:px-1.5 gap-3">{children}</div>;
};
