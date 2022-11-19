import { NextPage } from "next";
import ConfirmationLoading from "../src/components/ConfirmationLoading";
import FullPageFormWrapper from "../src/layout/FullPageFormWrapper";

const Confirmation: NextPage = () => {
  return (
    <FullPageFormWrapper>
      <ConfirmationLoading></ConfirmationLoading>
    </FullPageFormWrapper>
  );
};

export default Confirmation;
