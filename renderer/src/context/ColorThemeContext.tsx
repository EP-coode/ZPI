import React, { createContext, useEffect, useState } from "react";

export enum Theme {
  DEFAULT = "",
  LIGHT = "light",
  DARK = "dark",
  CYBERPUNK = "cyberpunk",
}

export interface IThemeContext {
  currentTheme: Theme;
  setTheme: (theme: Theme) => void;
}

export const THEME_STORAGE_KEY = "prefered_theme";

export const ThemeContext = createContext<IThemeContext | null>(null);

export const ThemeContextProvider = ({ children }: React.PropsWithChildren) => {
  const [theme, setTheme] = useState<Theme>(Theme.DEFAULT);

  const handleSetTheme = (theme: Theme) => {
    localStorage.setItem(THEME_STORAGE_KEY, theme);
    setTheme(theme);
  };

  useEffect(() => {
    const savedTheme = localStorage.getItem(THEME_STORAGE_KEY) as Theme;
    setTheme(savedTheme);
  },[]);

  return (
    <ThemeContext.Provider
      value={{ currentTheme: theme, setTheme: handleSetTheme }}
    >
      <div data-theme={theme}>{children}</div>
    </ThemeContext.Provider>
  );
};
