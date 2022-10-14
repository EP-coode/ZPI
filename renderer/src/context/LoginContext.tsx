import React, { createContext, useEffect, useState } from "react";
import { useRouter } from "next/router";
import AuthService, { User } from "../services/Auth";

interface ILoginContext {
  user?: User;
  login: (email: string, password: string, remember: boolean) => void;
  logout: () => void;
}

const LoginContext = createContext<ILoginContext | null>(null);

const authService = new AuthService("localhost:3000");

const LoginContextProvider = ({ children }: React.PropsWithChildren) => {
  const [user, setUser] = useState<User>();
  const [errors, setErrors] = useState<string[]>([])
  const [at, setAt] = useState<string>()

  const router = useRouter()
  
  const onAccesTokenChange = (errorMessages: string[], at?: string) => {
    setErrors(errorMessages);
    setAt(at)
  }

  const onLogout = () =>{
    setUser(undefined)
    setErrors([])
    setAt(undefined)
  }

  useEffect(()=>{
    authService.init();
    authService.onAccesTokenChange = onAccesTokenChange;
    authService.onLogout = onLogout;
    return ()=>{
        authService.close();
    }
  },[])

  return (
    <LoginContext.Provider value={{ user, logout: ()=>{
        authService.logout()
    }, login: async (email, password, remember)=>{
        const {errorMsgs, user} = await authService.login(email, password, remember)
        setErrors(errorMsgs ?? [])
        setUser(user)
    } }}>
      {children}
    </LoginContext.Provider>
  );
};
