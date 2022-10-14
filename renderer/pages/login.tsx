import type { NextPage } from "next";
import FullPageFormWrapper from "../src/layout/FullPageFormWrapper";
import LoginForm from "../src/components/LoginForm";

const Login: NextPage = () => {
  return (
    <FullPageFormWrapper>
      <LoginForm />
    </FullPageFormWrapper>
  );
};

export default Login;
