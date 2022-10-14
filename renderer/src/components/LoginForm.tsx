import Link from "next/link";
import React, { useState } from "react";

type Props = {};

const LoginForm = (props: Props) => {
  const [rememberOnThisDevice, setRememberOnThisDevice] = useState(false);

  const handleFormSubmit = (e: React.SyntheticEvent) => {
    e.preventDefault();

    const target = e.target as typeof e.target & {
      email: { value: string };
      password: { value: string };
    };

    alert(`Loguje jako: ${target.email.value}, ${target.password.value}`)

    //TODO podpięcie do serwera
  
  };

  return (
    <form
      onSubmit={handleFormSubmit}
      className="h-full max-w-xl min-w-[20rem] w-full bg-base-100 p-8 flex flex-col gap-5 rounded-md"
    >
      <h1 className="text-2xl mb-7">Witaj w StudentSociety</h1>
      <label className="input-group">
        <span className="w-20">Email</span>
        <input
          type="text"
          name="email"
          placeholder="example@mail.com"
          className="input input-bordered w-0 flex-grow"
        />
      </label>

      <label className="input-group">
        <span className="w-20">Hasło</span>
        <input
          type="password"
          name="password"
          className="input input-bordered w-0 flex-grow"
        />
      </label>

      <label className="label cursor-pointer">
        <span className="label-text">Pamiętaj na tym urządzeniu</span>
        <input
          type="checkbox"
          checked={rememberOnThisDevice}
          onClick={() => {
            setRememberOnThisDevice((prevState) => !prevState);
          }}
          className="checkbox"
        />
      </label>

      <input
        type="submit"
        className="btn btn-primary w-full max-w-xs mx-auto my-2"
        value="Zaloguj"
      />

      <div className="form-control ml-auto mr-0">
        <Link href={"/register"}>
          <a className="link">Utwórz konto</a>
        </Link>
      </div>
    </form>
  );
};

export default LoginForm;
