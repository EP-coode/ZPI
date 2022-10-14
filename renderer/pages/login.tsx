import type { NextPage } from "next";
import FullPageFormWrapper from "../src/layout/FullPageFormWrapper";
import LoginForm from "../src/components/LoginForm";

const LoginPage: NextPage = () => {
  return (
    <FullPageFormWrapper>
      <LoginForm />
    </FullPageFormWrapper>
  );
};

export default LoginPage;
