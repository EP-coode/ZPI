import React, { useState } from "react";
import Link from "next/link";

import classNames from "classnames";
import { useFormik } from "formik";
import * as Yup from "yup";

import ErrorSvg from "../icons/ErrorSvg";

const AUTH_SERVICE_URL =
  process.env.AUTH_SERVICE_URL ?? "http://localhost:8080";

const RegisterForm = () => {
  const [errors, setErrors] = useState<string[]>([]);
  const [showSuccesModal, setShowSuccesModal] = useState(false);

  const formik = useFormik({
    initialValues: {
      email: "",
      name: "",
      password: "",
      password_rpt: "",
    },
    validationSchema: Yup.object().shape({
      email: Yup.string()
        .email("E-mail jest niepoprawny")
        .required("Pole jest wymagane"),
      name: Yup.string()
        .required("Pole jest wymagane")
        .min(3, "Nick użytkownika musi posiadać conajmniej 3 znaki"),
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
    onSubmit: async ({ email, password, name }) => {
      if (errors.length > 0) setErrors([]);

      const result = await fetch(
        `${AUTH_SERVICE_URL}/registration/register`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email,
            password,
            name,
          }),
        }
      );

      if (!result.ok) {
        setErrors([await result.text()]);
      } else {
        setShowSuccesModal(true);
        formik.resetForm();
        setErrors([]);
      }

      formik.setSubmitting(false);
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
          <span className="label-text">Podaj nick</span>
        </label>
        <input
          type="string"
          name="name"
          placeholder="nick..."
          className={classNames("input input-bordered w-full", {
            "input-error": formik.errors.name,
          })}
          onChange={formik.handleChange}
          value={formik.values.name}
        />
        <label className="label">
          {formik.errors.name && (
            <span className="label-text text-error">{formik.errors.name}</span>
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

      <button
        className={classNames(
          "btn btn-primary w-full max-w-xs mx-auto my-2 relative",
          {
            loading: formik.isSubmitting,
          }
        )}
      >
        <input
          className={classNames("w-full h-full cursor-pointer absolute", {
            "pointer-events-none": formik.isSubmitting,
          })}
          type="submit"
          value=""
        />
        Zarejestuj
      </button>

      <input
        type="checkbox"
        id="my-modal-3"
        className="modal-toggle"
        onChange={(e) => setShowSuccesModal(e.target.checked)}
        checked={showSuccesModal}
      />
      <div className="modal">
        <div className="modal-box relative">
          <label
            htmlFor="my-modal-3"
            className="btn btn-sm btn-circle absolute right-2 top-2"
          >
            ✕
          </label>
          <h3 className="text-lg font-bold">
            Pomyślnie zarejestrowano w portalu.
          </h3>
          <p className="py-4">
            Aby zacząć korzystać ze swojego konta musisz jescze potwierdzić swój
            adres emali kikając na wysłany przez nas link.
          </p>
          <div className="modal-action">
            <Link href={"/login"}>
              <a href="#" className="btn">
                ekran logowania
              </a>
            </Link>
            <Link href={"/"}>
              <a href="#" className="btn btn-primary">
                strona główna
              </a>
            </Link>
          </div>
        </div>
      </div>

      <div className="form-control ml-auto mr-0">
        <Link href={"/login"}>
          <a className="link">Masz konto? Kliknij tu aby zalogować.</a>
        </Link>
      </div>
    </form>
  );
};

export default RegisterForm;
