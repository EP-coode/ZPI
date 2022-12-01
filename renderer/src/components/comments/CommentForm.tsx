import classNames from "classnames";
import { useFormik } from "formik";
import React, { useEffect, useMemo, useState } from "react";
import * as Yup from "yup";

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
        .min(3, "Komentarz musi składać się z conejmniej 3 znaków")
        .max(250, "Komentarz możemieć max 250 zanków"),
    }),
    validateOnChange: false,
    validateOnBlur: false,
    onSubmit: async ({ commentContent }) => {
      await onSubmit(commentContent);
      formik.resetForm();
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
          value={formik.values.commentContent}
          autoFocus={false}
          placeholder={"Napisz komentarz..."}
          className={classNames("textarea textarea-bordered h-32", {
            "textarea-error": formik.errors.commentContent,
          })}
          disabled={formik.isSubmitting}
        />
        <label className="label">
          <span className="label-text-alt  text-error">
            {formik.errors.commentContent}
          </span>
          <span className="label-text-alt">
            {formik.values.commentContent.length} / 250 zanków
          </span>
        </label>
      </div>

      <button
        className={classNames("btn ml-auto mr-0 my-2 float-right", {
          "pointer-events-none loading": formik.isSubmitting,
        })}
      >
        Dodaj komentarz
      </button>
    </form>
  );
};
export default CommentForm;
