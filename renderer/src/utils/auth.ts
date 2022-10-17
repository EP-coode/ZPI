import jwt_decode from "jwt-decode";
import { User } from "../model/User";

const REFRESH_TOKEN = "rt";
const ACCES_TOKEN = "at";
const KEEP_LOGGED_IN = "keep_logged_in";
const AUTH_SERVICE_URL = process.env.AUTH_SERVICE_URL ?? "localhost:3000";

interface TokenPayload {
  iat: number;
  exp: number;
}

interface RefreshTokenPayload extends TokenPayload {
  user: User;
}

/**
 * @returns information about if there is valid refresh token stored
 * in memory of client browser
 */
export function isLoggedIn(): boolean {
  const rt = getRefreshToken();

  if (!rt) return false;

  return isTokenValid(rt);
}

/**
 * Fetches refresh and acces token, then stores it in local storage
 * or in session storege if user don't want to be keept logged in.
 * @param email
 * @param password
 * @param remember
 */
export async function login(
  email: string,
  password: string,
  remember: boolean
) {
  try {
    const req = await fetch(`${AUTH_SERVICE_URL}/api/auth/login`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email: email,
        password: password,
      }),
    });

    if (!req.ok) throw new Error("Coś poszło nie tak");

    const { rt, at } = await req.json();

    localStorage.setItem(KEEP_LOGGED_IN, remember ? "1" : "0");

    if (remember) {
      localStorage.setItem(REFRESH_TOKEN, rt);
      sessionStorage.setItem(ACCES_TOKEN, at);
    } else {
      sessionStorage.setItem(REFRESH_TOKEN, rt);
      sessionStorage.setItem(ACCES_TOKEN, at);
    }
  } catch (e: any) {
    throw new Error("Coś poszło nie tak");
  }
}

export function logout() {
  // TODO: FETCH TO LOGOUT FORM SERVER AND REMOVE RT
  sessionStorage.removeItem(REFRESH_TOKEN);
  localStorage.removeItem(REFRESH_TOKEN);
  sessionStorage.removeItem(ACCES_TOKEN);
  localStorage.removeItem(KEEP_LOGGED_IN);
}

/**
 * @returns metadata about user storet in refresh token
 * if no user is logged in then returns undefined
 */
export function getUserData(): User | undefined {
  const rt = getRefreshToken();

  if (!rt) return undefined;

  const { user } = jwt_decode<RefreshTokenPayload>(rt);

  return user;
}

export async function refreshTokens() {
  const rt = getRefreshToken();

  const req = await fetch(`${AUTH_SERVICE_URL}/api/auth/rt`, {
    headers: {
      Accept: "application/json",
      Authorization: `Bearer ${rt}`,
    },
  });

  const { rt: newRt, at: newAt } = await req.json();

  const remember = localStorage.getItem(KEEP_LOGGED_IN);

  if (remember) {
    localStorage.setItem(REFRESH_TOKEN, newRt);
    sessionStorage.setItem(ACCES_TOKEN, newAt);
  } else {
    sessionStorage.setItem(REFRESH_TOKEN, newRt);
    sessionStorage.setItem(ACCES_TOKEN, newAt);
  }
}

// TODO: storing tokens in cokies is safer agains some attacks
export function getAccesToken(): string | undefined {
  return (
    localStorage.getItem(ACCES_TOKEN) ??
    sessionStorage.getItem(ACCES_TOKEN) ??
    undefined
  );
}

export function isTokenValid(token: string): boolean {
  const rtPayload = jwt_decode<TokenPayload>(token);
  const currentTime = new Date().getTime();
  const isValid = rtPayload.exp - currentTime > 0;
  return isValid;
}

function getRefreshToken(): string | undefined {
  return (
    localStorage.getItem(REFRESH_TOKEN) ??
    sessionStorage.getItem(REFRESH_TOKEN) ??
    undefined
  );
}
