import React, { PropsWithChildren } from "react";

type Props = {};

const RightCollumn = ({ children }: PropsWithChildren) => {
  return (
    <div className="h-full w-1/3 min-w-[20rem] grow py-3 md:px-1.5 flex flex-col gap-3">
      {children}
    </div>
  );
};

export default RightCollumn;
