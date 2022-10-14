import React, { createContext, useEffect, useState } from "react";
import { useRouter } from "next/router";
import AuthService, { User } from "../services/Auth";

interface ILoginContext {
  errorMsgs: string[];
  user?: User;
  at?: string;
  login: (email: string, password: string, remember: boolean) => void;
  logout: () => void;
}

const LoginContext = createContext<ILoginContext | null>(null);

// TODO: Przetestować działanie serwisu na działającym serwerze  
const authService = new AuthService("localhost:3000");

const LoginContextProvider = ({ children }: React.PropsWithChildren) => {
  const [user, setUser] = useState<User>();
  const [errors, setErrors] = useState<string[]>([]);
  const [at, setAt] = useState<string>();

  const router = useRouter();

  const onLoginStatusChange = (
    errorMessages: string[],
    at?: string,
    user?: User
  ) => {
    setErrors(errorMessages);
    setAt(at);
    setUser(user);
  };

  const onLogout = () => {
    setUser(undefined);
    setErrors([]);
    setAt(undefined);
  };

  useEffect(() => {
    authService.init();
    authService.onLoginStatusChange = onLoginStatusChange;
    authService.onLogout = onLogout;
    return () => {
      authService.close();
    };
  }, []);

  return (
    <LoginContext.Provider
      value={{
        user,
        errorMsgs: errors,
        at,
        logout: () => {
          authService.logout();
        },
        login: (email, password, remember) => {
          authService.login(email, password, remember);
        },
      }}
    >
      {children}
    </LoginContext.Provider>
  );
};
