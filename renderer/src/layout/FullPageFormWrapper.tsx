import Link from "next/link";
import React from "react";
import HomeSvg from "../icons/HomeSvg";

type Props = {
  children: React.ReactNode;
};

const FullPageFormWrapper = ({ children }: Props) => {
  return (
    <div
      className={`
            min-w-full
            min-h-screen
            flex flex-col p-2
            bg-gradient-to-br from-indigo-500 via-purple-500 to-pink-500`}
    >
      <Link href="/">
        <a className="btn btn-outline m-2 max-w-xs gap-2">
          Powrót do storny głównej
          <HomeSvg height="60%" />
        </a>
      </Link>
      <div
        className={`m-auto
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
