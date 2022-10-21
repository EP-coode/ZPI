import "../styles/globals.css";
import type { AppProps } from "next/app";
import { LoginContextProvider } from "../src/context/LoginContext";

function MyApp({ Component, pageProps }: AppProps) {
  return (
    <LoginContextProvider>
      <Component {...pageProps} />
    </LoginContextProvider>
  );
}

export default MyApp;
