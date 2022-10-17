import React, { useState } from "react";
import Link from "next/link";

import { useFormik } from "formik";
import * as Yup from "yup";

import ErrorSvg from "../icons/ErrorSvg";
import classNames from "classnames";

type Props = {};

const LoginForm = (props: Props) => {
  const [errors, setErrors] = useState<string[]>([]);
  const formik = useFormik({
    initialValues: {
      email: "",
      password: "",
      remember: false,
    },
    validationSchema: Yup.object().shape({
      email: Yup.string()
        .email("E-mail jest niepoprawny")
        .required("Pole jest wymagane"),
      password: Yup.string().required("Pole jest wymagane"),
    }),
    validateOnChange: false,
    validateOnBlur: false,
    onSubmit: ({ email, password }) => {
      alert(`Loguje jako: ${email}, ${password}`);

      //TODO podpięcie do serwera
      setErrors(["Niepodpięte do serwera"]);
    },
  });

  const handleFormChange = (e: React.SyntheticEvent) => {
    const target = e.target as typeof e.target & {
      email: { value: string };
      password: { value: string };
    };

    setErrors([]);
  };

  return (
    <form
      onSubmit={formik.handleSubmit}
      onChange={handleFormChange}
      className="h-full max-w-xl min-w-[20rem] w-full bg-base-100 p-8 flex flex-col gap-5 rounded-md"
    >
      <h1 className="text-2xl mb-7">Witaj w StudentSociety</h1>
      <div className="form-control w-full">
        <label className="label">
          <span className="label-text">E-mail</span>
        </label>
        <input
          type="email"
          name="email"
          placeholder="example@mail.com"
          className={classNames("input input-bordered w-full", {
            "input-error": formik.errors.email,
          })}
          onChange={formik.handleChange}
          value={formik.values.email}
        />
        <label className="label">
          {formik.errors.email && (
            <span className="label-text text-error">{formik.errors.email}</span>
          )}
        </label>
      </div>

      <div className="form-control w-full">
        <label className="label">
          <span className="label-text">Hasło</span>
        </label>
        <input
          type="password"
          name="password"
          className={classNames("input input-bordered w-full", {
            "input-error": formik.errors.password,
          })}
          placeholder="twoje hasło"
          onChange={formik.handleChange}
          value={formik.values.password}
        />
        <label className="label">
          {formik.errors.password && (
            <span className="label-text text-error">
              {formik.errors.password}
            </span>
          )}
        </label>
      </div>

      <label className="label cursor-pointer">
        <span className="label-text">Pamiętaj na tym urządzeniu</span>
        <input
          type="checkbox"
          name="remember"
          checked={formik.values.remember}
          onChange={formik.handleChange}
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
