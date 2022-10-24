import { getAccesToken, isLoggedIn, isTokenValid, refreshTokens } from "./auth";

const SERVICE_URL = process.env.DATA_PROVIDER_URL ?? "localhost:3000";

export async function fetchWithJWT<T>(
  endpoint: string,
  config: RequestInit
): Promise<T> {
  if (!isLoggedIn()) {
    throw new Error("Nie jesteś zalogowany");
  }

  let accesToken = getAccesToken();

  if (!accesToken || !isTokenValid(accesToken)) {
    await refreshTokens();
    accesToken = getAccesToken();
  }

  config.headers = { ...config.headers, Authorization: `Bearer ${accesToken}` };

  const request = new Request(`${SERVICE_URL}/${endpoint}`, config);
  const response = await fetch(request);

  if (!response.ok) {
    throw new Error("Coś poszło nie tak");
  }

  return await response.json();
}
