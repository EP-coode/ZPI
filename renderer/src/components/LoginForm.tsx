import Link from "next/link";
import React, { HtmlHTMLAttributes, useState } from "react";
import ErrorSvg from "../icons/ErrorSvg";

type Props = {};

const LoginForm = (props: Props) => {
  const [rememberOnThisDevice, setRememberOnThisDevice] = useState(false);
  const [errors, setErrors] = useState<string[]>([]);

  const handleFormSubmit = (e: React.SyntheticEvent) => {
    e.preventDefault();

    const target = e.target as typeof e.target & {
      email: { value: string };
      password: { value: string };
    };

    alert(`Loguje jako: ${target.email.value}, ${target.password.value}`);

    //TODO podpięcie do serwera
    setErrors(["Niepodpięte do serwera"]);
  };

  const handleFormChange = (e: React.SyntheticEvent) => {
    const target = e.target as typeof e.target & {
      email: { value: string };
      password: { value: string };
    };

    setErrors([]);
  };

  return (
    <form
      onSubmit={handleFormSubmit}
      onChange={handleFormChange}
      className="h-full max-w-xl min-w-[20rem] w-full bg-base-100 p-8 flex flex-col gap-5 rounded-md"
    >
      <h1 className="text-2xl mb-7">Witaj w StudentSociety</h1>
      <label className="input-group">
        <span className="w-20">Email</span>
        <input
          type="email"
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
          onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
            setRememberOnThisDevice(e.target.checked);
          }}
          className="checkbox"
        />
      </label>

      {errors.map((error, index) => (
        <div className="alert alert-error shadow-lg" key={index}>
          <div>
            <ErrorSvg />
            <span>{error}</span>
          </div>
        </div>
      ))}

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
