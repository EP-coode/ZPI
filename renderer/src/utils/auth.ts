import jwt_decode from "jwt-decode";
import AuthError from "../errors/AuthError";
import { User } from "../model/User";

const REFRESH_TOKEN = "rt";
const ACCES_TOKEN = "at";
const KEEP_LOGGED_IN = "keep_logged_in";
const AUTH_SERVICE_URL =
  process.env.AUTH_SERVICE_URL ?? "http://localhost:8080";

interface TokenPayload {
  exp: number;
  iss: string;
  sub: string;
  roles: string[];
}

interface RefreshTokenPayload extends TokenPayload {
  user_id: number;
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
): Promise<void> {
  try {
    const req = await fetch(`${AUTH_SERVICE_URL}/auth/login`, {
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

    if (!req.ok) throw new AuthError(await req.text());

    const { refresh_token: rt, access_token: at } = await req.json();
    localStorage.setItem(KEEP_LOGGED_IN, remember ? "1" : "0");

    if (remember) {
      localStorage.setItem(REFRESH_TOKEN, rt);
      sessionStorage.setItem(ACCES_TOKEN, at);
    } else {
      sessionStorage.setItem(REFRESH_TOKEN, rt);
      sessionStorage.setItem(ACCES_TOKEN, at);
    }
  } catch (e: any) {
    if (e instanceof AuthError) {
      throw e;
    }
    console.error(e);
    throw new Error("Coś poszło nie tak");
  }
}

export async function logout(): Promise<void> {
  // TODO: FETCH TO LOGOUT FORM SERVER AND REMOVE RT
  sessionStorage.removeItem(REFRESH_TOKEN);
  localStorage.removeItem(REFRESH_TOKEN);
  sessionStorage.removeItem(ACCES_TOKEN);
  localStorage.removeItem(KEEP_LOGGED_IN);
  location.reload();
}

/**
 * @returns metadata about user storet in refresh token
 * if no user is logged in then returns undefined
 */
export async function getUserData(): Promise<User | undefined> {
  const rt = getRefreshToken();

  if (!rt) return undefined;

  const { user_id } = jwt_decode<RefreshTokenPayload>(rt);

  const userReq = await fetch(`${AUTH_SERVICE_URL}/users/${user_id}`);

  if(!userReq.ok) throw new Error("Nie można pobrać danych użytkownika")

  return await userReq.json();
}

export async function refreshTokens() {
  const rt = getRefreshToken();

  const req = await fetch(`${AUTH_SERVICE_URL}/auth/refresh_token`, {
    headers: {
      Accept: "application/json",
      Authorization: `Bearer ${rt}`,
    },
  });

  const { refresh_token: newRt, access_token: newAt } = await req.json();

  const remember = parseInt(localStorage.getItem(KEEP_LOGGED_IN) ?? "0") === 1;

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
  const isValid = rtPayload.exp * 1000 - currentTime > 0;

  return isValid;
}

function getRefreshToken(): string | undefined {
  return (
    localStorage.getItem(REFRESH_TOKEN) ??
    sessionStorage.getItem(REFRESH_TOKEN) ??
    undefined
  );
}
