import { useFormik } from "formik";
import * as Yup from "yup";
import React, { useEffect, useState } from "react";
import { Post } from "../model/Post";
import classNames from "classnames";
import { CategoryGroup } from "../model/CategoryGroup";
import TagsPicker from "./TagsPicker";
import { MemoMarkdownEditor } from "./MarkdownEditor";
import Image from "next/image";

type Props = {
  onPostSubmit: (post: Post) => void;
  editedPost?: Post;
  categoryGroups: Required<CategoryGroup>[];
};

const PostEditor = ({ onPostSubmit, editedPost, categoryGroups }: Props) => {
  const [activeTags, setActiveTags] = useState<string[]>([]);
  const [imagePreviewFile, setImagePreviewFile] = useState<File | null>(null);
  const [imagePreviewFileUrl, setImagePreviewFileUrl] = useState<string>();

  const formik = useFormik({
    initialValues: {
      title: editedPost?.title ?? "",
      markdownContent: editedPost?.markdownContent ?? "",
      tags: [],
      image: "",
    },
    validationSchema: Yup.object().shape({
      title: Yup.string()
        .min(3, "Tytół musi mieć minimum 3 znaków")
        .required("Pole jest wymagane"),
      markdownContent: Yup.string()
        .min(10, "Treść musi miec conajmniej 10 znaków długości")
        .required("Post musi mieć treść"),
    }),
    validateOnChange: false,
    validateOnBlur: false,
    onSubmit: async (data) => {
      console.log("form data ", data);
      alert("Nie podpięte");
      formik.setSubmitting(false);
    },
  });

  const handlePickImage = (e: React.FormEvent<HTMLInputElement>) => {
    const imageFile = e.currentTarget.files ? e.currentTarget.files[0] : null;
    formik.setFieldValue("image", imageFile ?? "");
    setImagePreviewFile(imageFile);
  };

  useEffect(() => {
    let fileReader: FileReader | null = null;
    let isCancel = false;
    if (imagePreviewFile) {
      fileReader = new FileReader();
      fileReader.onload = (e) => {
        const fr = e.target;
        if (fr?.result && !isCancel) {
          setImagePreviewFileUrl(fr.result.toString());
        }
      };
      fileReader.readAsDataURL(imagePreviewFile);
    }
    return () => {
      isCancel = true;
      if (fileReader && fileReader.readyState === 1) {
        fileReader.abort();
      }
    };
  }, [imagePreviewFile]);

  const handleAddTag = (tagName: string) => {
    setActiveTags([...activeTags, tagName]);
  };

  const handleRemoveTag = (tagName: string) => {
    setActiveTags(
      activeTags.filter((tag) => tag.toLowerCase() != tagName.toLowerCase())
    );
  };

  return (
    <form
      onSubmit={formik.handleSubmit}
      className="flex flex-col items-center max-w-4xl w-full"
    >
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

      {/* IMAGE INPUT */}
      <div className="form-control w-full">
        <label className="label">
          <span className="label-text">Dodaj obraz do swojego postu</span>
        </label>
        <input
          type="file"
          name="image"
          accept="image/png, image/jpeg"
          className={classNames("file-input file-input-bordered w-full", {
            "input-error": formik.errors.image,
          })}
          onChange={handlePickImage}
        />
        <label className="label">
          {formik.errors.image && (
            <span className="label-text text-error">{formik.errors.image}</span>
          )}
        </label>
      </div>
      {imagePreviewFileUrl && (
        <figure className="relative h-96 w-full flex-shrink-0 flex-grow-0 my-10">
          <Image
            className="object-contain"
            src={imagePreviewFileUrl}
            layout="fill"
            alt="Ikona postu"
          />
        </figure>
      )}

      {/* MD EDITOR */}
      <div className="form-control w-full">
        <label className="label">
          <span className="label-text">Wpisz treść swojego postu</span>
        </label>
        <div
          className={classNames({
            "border-2 border-red-600 rounded-md": formik.errors.markdownContent,
          })}
        >
          <MemoMarkdownEditor
            onValueChange={(v) => {
              formik.setFieldValue("markdownContent", v);
            }}
          />
        </div>
        <label className="label">
          {formik.errors.markdownContent && (
            <span className="label-text text-error">
              {formik.errors.markdownContent}
            </span>
          )}
        </label>
      </div>

      {/* SUBMIT */}
      <button
        className={classNames(
          "btn btn-primary w-full max-w-xs mx-auto mb-2 mt-5 relative",
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
