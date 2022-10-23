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
        <div className="bg-base-300 min-h-screen ">
          {renderNavbar && <NavBar />}
          <Component {...pageProps} />
        </div>
      </LoginContextProvider>
    </ThemeContextProvider>
  );
}

export default MyApp;