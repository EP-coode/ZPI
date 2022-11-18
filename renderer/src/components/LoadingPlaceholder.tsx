import React from "react";

type Props = {
  className?: string
};

const LoadingPlaceholder = ({className}: Props) => {
  return (
    <div className="w-100 m-auto p-10 flex justify-center"> 
      <progress className={`progress w-56 ${className}`}></progress>
    </div>
  );
};

export default LoadingPlaceholder;
