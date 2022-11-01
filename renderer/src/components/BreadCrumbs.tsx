import { useRouter } from "next/router";
import React from "react";

export const BreadCrumbs = () => {
  const router = useRouter();
  return (
    <div className="text-sm breadcrumbs">
      <ul>
        <li>
          <a>Home</a>
        </li>
        <li>
          <a>Documents</a>
        </li>
        <li>Add Document</li>
      </ul>
    </div>
  );
};
