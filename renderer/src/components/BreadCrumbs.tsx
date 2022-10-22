import React from 'react'

type BreadCrumbsEntry = {
    text: string,
    linkUrl: string
}

type Props = {
    breadCrumbs: BreadCrumbsEntry[]
}

const BreadCrumbs = (props: Props) => {
  return (
    <div>BreadCrumbs</div>
  )
}