import React from "react";

type Props = {
  children: React.ReactNode;
};

const FullPageFormWrapper = ({ children }: Props) => {
  return (
    <div
      className={`
            min-w-full
            min-h-screen
            bg-gradient-to-br from-indigo-500 via-purple-500 to-pink-500`}
    >
      <div
        className={`fixed top-1/2 left-1/2 -translate-y-1/2 -translate-x-1/2  
            w-4/5
            min-w-fit
            flex justify-around
        `}
      >
        {children}
      </div>
    </div>
  );
};

export default FullPageFormWrapper;
