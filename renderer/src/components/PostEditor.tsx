import { useFormik } from "formik";
import * as Yup from "yup";
import React, { useContext, useEffect, useState } from "react";
import { Post } from "../model/Post";
import classNames from "classnames";
import { CategoryGroup } from "../model/CategoryGroup";
import TagsPicker from "./TagsPicker";
import { MemoMarkdownEditor } from "./MarkdownEditor";
import Image from "next/image";
import { CreatePostDto } from "../dto/request/CreatePostDto";
import { ModalContext } from "../context/ModalContext";
import { useRouter } from "next/router";

type Props = {
  onPostSubmit: (post: CreatePostDto) => Promise<Post>;
  editedPost?: Post;
  categoryGroups: Required<CategoryGroup>[];
};

const PostEditor = ({ onPostSubmit, editedPost, categoryGroups }: Props) => {
  const [imagePreviewFile, setImagePreviewFile] = useState<File | null>(null);
  const [imagePreviewFileUrl, setImagePreviewFileUrl] = useState<string>();
  const modalContext = useContext(ModalContext);
  const router = useRouter();

  const formik = useFormik({
    initialValues: {
      activeTags: [],
      title: editedPost?.title ?? "",
      markdownContent: editedPost?.markdownContent ?? "",
      image: "",
      categoryName: categoryGroups[0].postCategories[0].displayName ?? "",
    },
    validationSchema: Yup.object().shape({
      title: Yup.string()
        .min(3, "Tytuł musi mieć minimum 3 znaków")
        .required("Pole jest wymagane"),
      markdownContent: Yup.string()
        .min(10, "Treść musi miec conajmniej 10 znaków długości")
        .required("Post musi mieć treść"),
    }),
    validateOnChange: false,
    validateOnBlur: false,
    onSubmit: async ({ title, markdownContent, categoryName, activeTags }) => {
      try {
        const { postId } = await onPostSubmit({
          title,
          markdownContent,
          tagNames: activeTags,
          photo: imagePreviewFile,
          categoryName,
        });
        formik.setSubmitting(false);
        handleResetFrom();
        modalContext.setupModal(null, "Pomyślnie utworzono post", true, [
          {
            label: "Powrót do głównej",
            onClick: () => router.push(`/`),
          },
          {
            label: "Zobacz post",
            onClick: () => router.push(`/posts/${postId}`),
            classNames: "btn-primary"
          },
        ]);
        modalContext.show();
      } catch (e: any) {
        modalContext.setupModal(null, "Coś poszło nie tak", true, []);
        modalContext.show();
      }
    },
  });

  const handleResetFrom = () => {
    formik.resetForm();
    setImagePreviewFileUrl(undefined);
    setImagePreviewFile(null);
  };

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
    formik.setFieldValue("activeTags", [...formik.values.activeTags, tagName]);
  };

  const handleRemoveTag = (tagName: string) => {
    formik.setFieldValue(
      "activeTags",
      formik.values.activeTags.filter(
        (tag) => (tag as string).toLowerCase() != tagName.toLowerCase()
      )
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
          <span className="label-text">Tytuł postu</span>
        </label>
        <input
          type="text"
          name="title"
          placeholder="Podaj tytuł..."
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
        <select
          name="categoryName"
          className="select select-bordered w-full"
          onChange={formik.handleChange}
          value={formik.values.categoryName}
        >
          {categoryGroups.map(({ postCategories, displayName, totalPosts }) => (
            <optgroup label={`${displayName}`} key={displayName}>
              {postCategories.map(({ displayName }) => (
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
          activeTags={formik.values.activeTags}
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
