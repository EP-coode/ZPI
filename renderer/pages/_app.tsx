import "../styles/globals.css";
import type { AppProps } from "next/app";
import { LoginContextProvider } from "../src/context/LoginContext";
import { useRouter } from "next/router";
import { NavBar } from "../src/components/NavBar";
import { ThemeContextProvider } from "../src/context/ColorThemeContext";

function MyApp({ Component, pageProps }: AppProps) {
  const router = useRouter();
  const routesWithoutNavbar = ["/register", "/login"];
  const renderNavbar = !routesWithoutNavbar.some(
    (route) => router.pathname == route
  );

  return (
    <ThemeContextProvider>
      <LoginContextProvider>
        {renderNavbar && <NavBar />}
        <Component {...pageProps} />
      </LoginContextProvider>
    </ThemeContextProvider>
  );
}

export default MyApp;
