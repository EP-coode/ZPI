import classNames from "classnames";
import Link from "next/link";
import React, { useMemo, useState } from "react";
import ErrorSvg from "../icons/ErrorSvg";

type Props = {};

interface FormInputError {
  affectedFields?: string[];
  msg: string;
}

interface FormFields {
  email: { value: string };
  password: { value: string };
  password_rpt: { value: string };
}

const RegisterForm = (props: Props) => {
  const [errors, setErrors] = useState<FormInputError[]>([]);

  const wrongFields = useMemo<Set<string>>(
    () =>
      errors.reduce<Set<string>>((acc, current) => {
        current.affectedFields?.forEach((field) => acc.add(field));
        return acc;
      }, new Set<string>()),
    [errors]
  );

  const validateForm = (formFields: FormFields): FormInputError[] => {
    const errors: FormInputError[] = [];

    if (formFields.password.value != formFields.password_rpt.value) {
      errors.push({
        msg: "Hasła nie zgadzają się",
        affectedFields: ["password", "password_rpt"],
      });
    }

    if (formFields.password.value.length < 7) {
      errors.push({
        msg: "Hasło musi mieć conajmniej 7 znaków",
        affectedFields: ["password"],
      });
    }

    // todo walidacjaa maila 

    return errors;
  };

  const handleFormChange = (e: React.SyntheticEvent) => {
    if (errors.length > 0) setErrors([]);
  };

  const handleFormSubmit = (e: React.SyntheticEvent) => {
    e.preventDefault();

    const target = e.target as typeof e.target & FormFields;

    const errors = validateForm(target);

    if (errors.length > 0) {
      setErrors(errors);
      return;
    }

    alert(`Rejestruje jako: ${target.email.value}, ${target.password.value}`);

    //TODO podpięcie do serwera
    setErrors([{ msg: "Nie podięte do serwera" }]);
  };

  return (
    <form
      onSubmit={handleFormSubmit}
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
          placeholder="email"
          className={classNames("input input-bordered w-full", {
            "input-error": wrongFields.has("email"),
          })}
        />
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
            "input-error": wrongFields.has("password"),
          })}
        />
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
            "input-error": wrongFields.has("password_rpt"),
          })}
        />
      </div>

      {errors.map((error, index) => (
        <div className="alert alert-error shadow-lg" key={index}>
          <div>
            <ErrorSvg />
            <span>{error.msg}</span>
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
