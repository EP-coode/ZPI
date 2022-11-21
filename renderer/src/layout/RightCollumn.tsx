import React, { PropsWithChildren } from "react";

type Props = {
  className?: string
  children: React.ReactNode
};

const RightCollumn = ({ children, className }: Props) => {
  return (
    <div className={`h-full grow py-1.5 md:px-1.5 flex flex-col gap-3 col-start-1 row-start-1 md:col-start-2 ${className}`}>
      {children}
    </div>
  );
};

export default RightCollumn;
