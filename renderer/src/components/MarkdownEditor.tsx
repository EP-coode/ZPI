import "@uiw/react-md-editor/markdown-editor.css";
import "@uiw/react-markdown-preview/markdown.css";
import dynamic from "next/dynamic";
import React, { lazy, useState } from "react";
import { MDEditorProps } from "@uiw/react-md-editor";
import ramarkGfm from "remark-gfm";

// For some reason susense wont work for this lib
const MDEditor = dynamic<MDEditorProps>(() => import("@uiw/react-md-editor"), {
  ssr: false,
});
const mde = lazy(() => import("@uiw/react-md-editor"));

type Props = {
  onValueChange?: (mdContent: string) => void;
};

const MarkdownEditor = ({ onValueChange }: Props) => {
  const [value, setValue] = useState("**Hello world!!!**");
  return (
    <div className="min-h-64 w-full">
      <MDEditor
        value={value}
        onChange={(v) => {
          setValue(v ?? "");
          onValueChange && onValueChange(v ?? "");
        }}
        minHeight={200}
        height={300}
        previewOptions={{ remarkPlugins: [ramarkGfm] }}
      />
    </div>
  );
};

export default MarkdownEditor;

export const MemoMarkdownEditor = React.memo(
  MarkdownEditor,
  (prev, current) => {
    return prev.onValueChange == current.onValueChange;
  }
);
