import React, { createContext, useEffect, useState } from "react";
import { useRouter } from "next/router";
import { User } from "../model/User";
import {
  getAccesToken,
  getUserData,
  isLoggedIn,
  login,
  logout,
  refreshTokens,
} from "../utils/auth";

export interface ILoginContext {
  errorMsgs: string[];
  user?: User;
  at?: string;
  login: (
    email: string,
    password: string,
    remember: boolean
  ) => Promise<boolean>;
  logout: () => void;
}

export const LoginContext = createContext<ILoginContext | null>(null);

export const LoginContextProvider = ({ children }: React.PropsWithChildren) => {
  const [user, setUser] = useState<User>();
  const [errors, setErrors] = useState<string[]>([]);
  const [at, setAt] = useState<string>();

  const router = useRouter();

  const handleLogin = async (
    email: string,
    password: string,
    remember: boolean
  ) => {
    try {
      if (errors.length > 0) setErrors([]);
      await login(email, password, remember);
      const [user, at] = await Promise.all([getUserData(), getAccesToken()]);
      setUser(user);
      setAt(at);
      return true;
    } catch (e: any) {
      if (e instanceof Error) {
        setErrors([e.message]);
      }
      return false;
    }
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
        errorMsgs: errors,
        at,
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
