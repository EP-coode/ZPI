import React, { PropsWithChildren } from "react";

type Props = {
  children: React.ReactNode;
  className?: string;
};

const ContentPane = ({ children, className }: Props) => {
  return (
    <div
      className={`flex flex-wrap w-full bg-base-100 shadow-md rounded-md justify-center items-center mx-auto py-5 px-3 ${className ?? ""}`}
    >
      {children}
    </div>
  );
};

export default ContentPane;
