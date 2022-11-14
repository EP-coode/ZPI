import Link from "next/link";
import React, { useContext } from "react";
import { Theme, ThemeContext } from "../context/ColorThemeContext";
import { LoginContext } from "../context/LoginContext";
import BellSvg from "../icons/BellSvg";
import HomeSvg from "../icons/HomeSvg";
import StudentSvg from "../icons/StudentSvg";

type Props = {};

export const NavBar = (props: Props) => {
  const loginContext = useContext(LoginContext);
  const themeContext = useContext(ThemeContext);

  const themeChangeHandler = (e: React.ChangeEvent<HTMLSelectElement>) => {
    themeContext?.setTheme(e.target.value as Theme);
  };

  return (
    <div className="navbar bg-base-100 sticky top-0 z-50">
      <div className="flex-1">
        <Link href="/">
          <a className="btn btn-ghost normal-case text-xl">
            <HomeSvg className="block sm:hidden" height="50%" />
            <span className="hidden sm:block">Student Society</span>
          </a>
        </Link>
        <select
          onChange={themeChangeHandler}
          value={themeContext?.currentTheme || Theme.DEFAULT}
          className="select w-fit"
        >
          <option value={Theme.DEFAULT}>Domyślny</option>
          <option value={Theme.LIGHT}>Jasny</option>
          <option value={Theme.DARK}>Ciemny</option>
          <option value={Theme.CYBERPUNK}>Cyberpunk</option>
        </select>
      </div>
      {loginContext?.user ? (
        <div className="flex-1 flex-row gap-3 justify-end">
          <div className="dropdown dropdown-end">
            <div
              className="tooltip tooltip-bottom"
              data-tip="Obserwowane treści"
            >
              <label tabIndex={0} className="btn btn-ghost btn-circle">
                <BellSvg />
              </label>
            </div>
          </div>
          <div className="dropdown dropdown-end">
            <label tabIndex={0} className="btn btn-ghost btn-circle avatar">
              <div className="w-10 rounded-full">
                <StudentSvg />
              </div>
            </label>
            <ul
              tabIndex={0}
              className="menu menu-compact dropdown-content mt-3 p-2 shadow bg-base-100 rounded-box w-52"
            >
              <li>
                <a className="justify-between">Strona profilowa</a>
              </li>
              <li>
                <a className="justify-between">Opcje konta</a>
              </li>
              <li>
                <a onClick={() => loginContext.logout()}>Wyloguj</a>
              </li>
            </ul>
          </div>
        </div>
      ) : (
        <Link href="/login">
          <a className="btn">Zaloguj</a>
        </Link>
      )}
    </div>
  );
};
