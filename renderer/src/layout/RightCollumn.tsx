import React, { PropsWithChildren } from "react";

type Props = {};

const RightCollumn = ({ children }: PropsWithChildren) => {
  return (
    <div className="h-full w-1/3 min-w-[20rem] grow py-1.5 md:px-1.5 flex flex-col gap-3 sticky top-20">
      {children}
    </div>
  );
};

export default RightCollumn;
