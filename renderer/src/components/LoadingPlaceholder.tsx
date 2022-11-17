import React from "react";

type Props = {};

const LoadingPlaceholder = (props: Props) => {
  return (
    <div className="w-100 m-auto p-10"> 
      <progress className="progress w-56"></progress>
    </div>
  );
};

export default LoadingPlaceholder;
