import { useFormik } from "formik";
import * as Yup from "yup";
import React, { useState } from "react";
import { Post } from "../model/Post";
import classNames from "classnames";
import { CategoryGroup } from "../model/CategoryGroup";
import TagsPicker from "./TagsPicker";

type Props = {
  onPostSubmit: (post: Post) => void;
  editedPost?: Post;
  categoryGroups: Required<CategoryGroup>[];
};

const PostEditor = ({ onPostSubmit, editedPost, categoryGroups }: Props) => {
  const [activeTags, setActiveTags] = useState<string[]>([]);

  const formik = useFormik({
    initialValues: {
      title: "",
      markdownContent: "",
      tags: [],
      image: null,
    },
    validationSchema: Yup.object().shape({
      title: Yup.string()
        .min(3, "Tytół musi mieć minimum 3 znaków")
        .required("Pole jest wymagane"),
    }),
    validateOnChange: false,
    validateOnBlur: false,
    onSubmit: async (data) => {
      console.log("form data ", data);
      formik.setSubmitting(false);
    },
  });

  const handleAddTag = (tagName: string) => {
    setActiveTags([...activeTags, tagName]);
  };

  const handleRemoveTag = (tagName: string) => {
    setActiveTags(activeTags.filter((tag) => tag.toLowerCase() != tagName.toLowerCase()));
  };

  return (
    <form onSubmit={formik.handleSubmit} className="flex flex-col items-center max-w-3xl w-full">
      {/* TITLE INPUT */}
      <div className="form-control w-full">
        <label className="label">
          <span className="label-text">Tytół postu</span>
        </label>
        <input
          type="text"
          name="title"
          placeholder="Podaj tytół..."
          className={classNames("input input-bordered w-full", {
            "input-error": formik.errors.title,
          })}
          onChange={formik.handleChange}
          value={formik.values.title}
        />
        <label className="label">
          {formik.errors.title && (
            <span className="label-text text-error">{formik.errors.title}</span>
          )}
        </label>
      </div>

      {/* CATEGORY SELECT */}
      <div className="form-control w-full">
        <label className="label">
          <span className="label-text">Wybierz dział postu</span>
        </label>
        <select className="select select-bordered w-full">
          {categoryGroups.map(({ categories, name, totalPosts }) => (
            <optgroup label={`${name}`} key={name}>
              {categories.map(({ displayName }) => (
                <option key={displayName}>{displayName}</option>
              ))}
            </optgroup>
          ))}
        </select>
      </div>

      {/* IMAGE INPUT */}
      <div className="form-control w-full">
        <label className="label">
          <span className="label-text">Dodaj obraz do swojego postu</span>
        </label>
        <input
          type="file"
          name="image"
          accept="image/*"
          className={classNames(
            "file-input file-input-bordered w-full",
            {
              "input-error": formik.errors.image,
            }
          )}
          onChange={(e) => {
            const imageFile = e.currentTarget.files
              ? e.currentTarget.files[0]
              : null;
            formik.setFieldValue("image", imageFile ?? "");
          }}
        />
        <label className="label">
          {formik.errors.image && (
            <span className="label-text text-error">{formik.errors.image}</span>
          )}
        </label>
      </div>

      {/* TAGS */}
      <div className="form-control w-full mt-5 mb-10 flex items-center">
        <TagsPicker
          noTagsSelectedMsg="Nie wybrano żadnych tagów"
          pickedLabel="Wybrane tagi:"
          activeTags={activeTags}
          onAddTag={handleAddTag}
          onRemoveTag={handleRemoveTag}
          enableCustomTags
        />
      </div>

      {/* SUBMIT */}
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
        Dodaj post
      </button>
    </form>
  );
};

export default PostEditor;
