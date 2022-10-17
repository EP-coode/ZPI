import React, { useState } from "react";
import Link from "next/link";

import classNames from "classnames";
import { useFormik } from "formik";
import * as Yup from "yup";

import ErrorSvg from "../icons/ErrorSvg";

type Props = {};

const RegisterForm = (props: Props) => {
  const [errors, setErrors] = useState<string[]>([]);
  const formik = useFormik({
    initialValues: {
      email: "",
      password: "",
      password_rpt: "",
    },
    validationSchema: Yup.object().shape({
      email: Yup.string()
        .email("E-mail jest niepoprawny")
        .required("Pole jest wymagane"),
      password: Yup.string()
        .min(7, "Hasło musi mieć min. 7 znaków")
        .max(32, "Hasło może mieć max 32 znaki")
        .oneOf([Yup.ref("password_rpt")], "Hasła nie zgadzają się")
        .required("Pole jest wymagane"),
      password_rpt: Yup.string()
        .oneOf([Yup.ref("password")], "Hasła nie zgadzają się")
        .required("Pole jest wymagane"),
    }),
    validateOnChange: false,
    validateOnBlur: false,
    onSubmit: ({ email, password }) => {
      alert(`Rejestruje jako: ${email}, ${password}`);

      //TODO podpięcie do serwera
      setErrors(["Nie podięte do serwera"]);
    },
  });
  const handleFormChange = (e: React.SyntheticEvent) => {
    if (errors.length > 0) setErrors([]);
  };

  return (
    <form
      onSubmit={formik.handleSubmit}
      onChange={handleFormChange}
      className="h-full max-w-xl min-w-[20rem] w-full bg-base-100 p-8 flex flex-col gap-5 rounded-md"
    >
      <h1 className="text-2xl mb-7">Zarejestruj się w StudentSociety</h1>

      <div className="form-control w-full">
        <label className="label">
          <span className="label-text">Podaj email</span>
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
          <span className="label-text">Podaj hasło</span>
        </label>
        <input
          type="password"
          name="password"
          placeholder="hasło"
          className={classNames("input input-bordered w-full", {
            "input-error": formik.errors.password,
          })}
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

      <div className="form-control w-full">
        <label className="label">
          <span className="label-text">Potwierdź hasło</span>
        </label>
        <input
          type="password"
          name="password_rpt"
          placeholder="hasło"
          className={classNames("input input-bordered w-full", {
            "input-error": formik.errors.password_rpt,
          })}
          onChange={formik.handleChange}
          value={formik.values.password_rpt}
        />
        <label className="label">
          {formik.errors.password_rpt && (
            <span className="label-text text-error">
              {formik.errors.password_rpt}
            </span>
          )}
        </label>
      </div>

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
        className="btn btn-primary w-full max-w-xs mx-auto my-3 mt-7"
        value="Zarejestruj"
      />

      <div className="form-control ml-auto mr-0">
        <Link href={"/login"}>
          <a className="link">Masz konto? Kliknij tu aby zalogować.</a>
        </Link>
      </div>
    </form>
  );
};

export default RegisterForm;
