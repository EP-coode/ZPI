import "@uiw/react-md-editor/markdown-editor.css";
import "@uiw/react-markdown-preview/markdown.css";
import dynamic from "next/dynamic";
import React, { lazy, useContext, useState } from "react";
import { MDEditorProps } from "@uiw/react-md-editor";
import ramarkGfm from "remark-gfm";
import { ThemeContext } from "../context/ColorThemeContext";

// For some reason susense wont work for this lib
const MDEditor = dynamic<MDEditorProps>(() => import("@uiw/react-md-editor"), {
  ssr: false,
});
const mde = lazy(() => import("@uiw/react-md-editor"));

type Props = {
  onValueChange?: (mdContent: string) => void;
  initialContent?: string
};

const MarkdownEditor = ({ onValueChange, initialContent = "" }: Props) => {
  const themeContext = useContext(ThemeContext)
  const [value, setValue] = useState(initialContent);
  return (
    <div className="min-h-64 w-full" data-color-mode={themeContext?.currentTheme ?? "dark"}>
      <MDEditor
        value={value}
        onChange={(v) => {
          setValue(v ?? "");
          onValueChange && onValueChange(v ?? "");
        }}
        minHeight={200}
        height={300}
        previewOptions={{ remarkPlugins: [ramarkGfm] }}
        preview="live"

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
