import * as React from "react";
import { SVGProps } from "react";

const SvgComponent = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    fill="currentColor"
    stroke="currentColor"
    xmlSpace="preserve"
    viewBox="0 0 24 24"
    className="inline-block h-4 stroke-current cursor-pointer"
    {...props}
  >
    <path d="M6 18 18 6M6 6l12 12" />
  </svg>
);

export default SvgComponent;
