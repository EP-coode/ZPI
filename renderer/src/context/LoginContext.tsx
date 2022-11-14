import React, { createContext, useEffect, useState } from "react";
import AuthError from "../errors/AuthError";
import { User } from "../model/User";
import {
  getUserData,
  isLoggedIn,
  login,
  logout,
  refreshTokens,
} from "../utils/auth";

export interface ILoginContext {
  user?: User;
  login: (email: string, password: string, remember: boolean) => Promise<void>;
  logout: () => void;
}

export const LoginContext = createContext<ILoginContext | null>(null);

export const LoginContextProvider = ({ children }: React.PropsWithChildren) => {
  const [user, setUser] = useState<User>();

  const handleLogin = async (
    email: string,
    password: string,
    remember: boolean
  ) => {
    try {
      await login(email, password, remember);
      const user = await getUserData();
      setUser(user);
    } catch (e: any) {
      if (e instanceof AuthError) {
        throw e;
      } else {
        console.error(e);
        throw new AuthError("Coś poszło nie tak");
      }
    }
  };

  useEffect(() => {
    const f = async () => {
      const is_logged_in = isLoggedIn();
      if (is_logged_in) {
        await refreshTokens();
        setUser(await getUserData());
      }
    };

    f();
  }, []);

  return (
    <LoginContext.Provider
      value={{
        user,
        logout: () => {
          logout();
        },
        login: handleLogin,
      }}
    >
      {children}
    </LoginContext.Provider>
  );
};
