import classNames from "classnames";
import { useFormik } from "formik";
import React, { useEffect, useMemo, useState } from "react";
import * as Yup from "yup";
import { Comment } from "../model/Comment";

type Props = {
  onSubmit: (x: string) => Promise<void>;
};

const CommentForm = ({ onSubmit }: Props) => {
  const formik = useFormik({
    initialValues: {
      commentContent: "",
    },
    validationSchema: Yup.object().shape({
      commentContent: Yup.string()
        .required("Pole jest wymagane")
        .min(3, "Komentaż musi składać się z conejmniej 3 znaków")
        .max(250, "Komentaż możemieć max 250 zanków"),
    }),
    validateOnChange: false,
    validateOnBlur: false,
    onSubmit: async ({ commentContent }) => {
      await onSubmit(commentContent);
      formik.setSubmitting(false);
    },
  });

  return (
    <form
      onSubmit={formik.handleSubmit}
      onChange={formik.handleChange}
      className="w-full"
    >
      <div className="form-control">
        <textarea
          id="commentContent"
          autoFocus={false}
          placeholder={"Napisz komentarz..."}
          className={classNames("textarea textarea-bordered h-32", {
            "textarea-error": formik.errors.commentContent,
          })}
        />
        <label className="label">
          <span className="label-text-alt  text-error">{formik.errors.commentContent}</span>
          <span className="label-text-alt">{formik.values.commentContent.length} / 250 zanków</span>
        </label>
      </div>

      <button
        className={classNames("btn ml-auto mr-0 my-2 block relative", {
          loading: formik.isSubmitting,
        })}
      >
        <input
          className={classNames("w-full h-full cursor-pointer absolute", {
            "pointer-events-none": formik.isSubmitting,
          })}
          type="submit"
          value=""
        />
        Dodaj komentarz
      </button>
    </form>
  );
};
export default CommentForm;
