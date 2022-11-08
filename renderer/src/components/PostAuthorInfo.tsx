import classNames from "classnames";
import Image from "next/image";
import React from "react";
import ContentPane from "../layout/ContentPane";
import { User } from "../model/User";

type Props = {
  user: User;
};

const UserInfoCard = ({ user }: Props) => {
  return (
    <ContentPane>
      {user.avatarUrl ? (
        <div className="avatar my-5 mx-2 w-fit h-fit">
          <div className="relative w-24 rounded-full ring ring-primary ring-offset-base-100 ring-offset-2">
            <Image
              src={user.avatarUrl}
              layout="fill"
              objectFit="contain"
              className=""
            />
          </div>
        </div>
      ) : null}
      <div className="w-fit flex flex-col gap-3 p-3">
        <div>
          <span className="font-semibold">Autor:</span>
          <h2 className="inline-block ml-2">{user.email}</h2>
        </div>
        <div>
          <span className="font-semibold">Potiwierdzony student:</span>
          <h2
            className={classNames("inline-block ml-2", {
              "text-green-700": user.emailConfirmed,
              "text-red-700": !user.emailConfirmed,
            })}
          >
            {user.emailConfirmed ? "TAK" : "NIE"}
          </h2>
        </div>
        <div>
          <span className="font-semibold">Rola:</span>
          <h2 className="inline-block ml-2">{user.role.roleName}</h2>
        </div>
      </div>
    </ContentPane>
  );
};

export default UserInfoCard;
