import * as React from "react"
import { SVGProps } from "react"

const ArrowSvg = (props: SVGProps<SVGSVGElement>) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 -2 26 32"
    xmlSpace="preserve"
    height="100%"
    fill="currentColor"
    stroke="currentColor"
    strokeWidth="3"
    {...props}
  >
    <path
      d="m13.915.379 8.258 9.98s1.252 1.184-.106 1.184h-4.653v14.523s.184.709-.885.709h-6.55c-.765 0-.749-.592-.749-.592V11.84H4.933c-1.654 0-.408-1.24-.408-1.24S11.55 1.275 12.526.295c.714-.709 1.389.084 1.389.084z"
    />
  </svg>
)

export default ArrowSvg
