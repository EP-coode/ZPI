import React, { useEffect, useMemo, useState } from "react";
import { Comment } from "../model/Comment";

type Props = {
    onSubmit: (x: string) => {};
};

const CommentForm = ({onSubmit}: Props) => {
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = async () => {
        if(message.length > 250){
            setError("Twój komentarz jest za długi");
            return;
        }
        setMessage("");
        onSubmit(message);
    };

    return(
        <form>
        <div className="comment-form-row">
          <textarea
            autoFocus={false}
            placeholder={"Napisz komentarz..."}
            value={message}
            onChange={e => {setMessage(e.target.value); setError("")}}
            id="textAreaExample"
            rows={4}
            className="form-control shadow-md w-full max-h-[100rem] resize-none overflow-auto p-5 pt-5"
          />
          <button className="mt-2 float-right btn shadow-md" type="button" onClick={_ => handleSubmit()}>
            Opublikuj
          </button>
        </div>
        <div className="text-red-500 font-semibold">{error}</div>
      </form>
    );

};
export default CommentForm;