import { NextPage } from "next";
import RegisterForm from "../src/components/RegisterForm";
import FullPageFormWrapper from "../src/layout/FullPageFormWrapper";

const Register: NextPage = () => {
  return (
    <FullPageFormWrapper>
      <RegisterForm />
    </FullPageFormWrapper>
  );
};

export default Register;
