import Link from "next/link";
import React from "react";

export type Crumb = { title: string; href: string | null };

type BreadCrumbsProps = {
  crumbs: Crumb[];
};

export const BreadCrumbs = ({ crumbs }: BreadCrumbsProps) => {
  return (
    <div className="text-sm breadcrumbs px-5">
      <ul>
        {crumbs.map(({ title, href }) => (
          <li key={title}>
            {href ? (
              <Link href={href}>
                <a>{title}</a>
              </Link>
            ) : (
              <>{title}</>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};
