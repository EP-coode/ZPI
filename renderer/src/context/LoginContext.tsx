import React, { createContext, useEffect, useState } from "react";
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
  login: (
    email: string,
    password: string,
    remember: boolean
  ) => Promise<void>;
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
    await login(email, password, remember);
    const user = await getUserData();
    setUser(user);
  };

  useEffect(() => {
    const f = async () => {
      if (isLoggedIn()) {
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
