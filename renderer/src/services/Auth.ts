import jwt_decode from "jwt-decode";
import { json } from "stream/consumers";

export interface Role {
  canCreatePost: boolean;
  //TODO
}

export interface User {
  email: string;
  emailConfirmed: boolean;
  studentStatusConfirmed: boolean;
  avatarUrl: string;
  role: Role;
}

interface RtTokenPayload {
  iat: number;
  exp: number;
  user: User;
}

interface LoginResponse {
  user?: User;
  errorMsgs?: string[];
}

const REFRESH_TOKEN = "rt";
const ACCES_TOKEN = "at";

export default class AuthService {
  public onLoginStatusChange:
    | ((errorMessages: string[], at?: string, user?: User) => void)
    | undefined;

  public onLogout: (() => void) | undefined;

  private email?: string;
  private password?: string;
  private remember?: boolean;

  private logoutTimeout?: number;
  private atTiemout?: number;

  private rt?: string;
  private at?: string;

  constructor(private authServerUrl: string) {}

  private async getTokens() {
    const req = await fetch(`${this.authServerUrl}/login/rt`, {
      method: "POST",
      body: JSON.stringify({
        email: this.email,
        password: this.password,
      }),
    });

    if (!req.ok) {
      if (this.onLoginStatusChange) this.onLoginStatusChange(["coś poszło nie tak"]);
    }

    const res = await req.json();
    this.rt = res.rt;
    this.at = res.at;

    if (this.onLoginStatusChange) {
      const user = this.getUser();
      this.onLoginStatusChange([], this.at, user);
    }
  }

  private async refreshTokens() {
    const req = await fetch(`${this.authServerUrl}/login/at`, {
      headers: {
        Authorization: `Bearer ${this.rt}`,
      },
    });

    if (!req.ok) {
      if (this.onLoginStatusChange) this.onLoginStatusChange(["coś poszło nie tak"]);
    }

    const res = await req.json();
    this.at = res.at;
    this.rt = res.rt;

    if (this.onLoginStatusChange) {
      const user = this.getUser();
      this.onLoginStatusChange([], this.at, user);
    }
  }

  isLoggedIn(): boolean {
    if (!this.rt) return false;

    const currentTime = new Date().getTime();
    const isTokenExpired =
      jwt_decode<RtTokenPayload>(this.rt).exp > currentTime;
    return !isTokenExpired;
  }

  async init() {
    const rt = localStorage.getItem(REFRESH_TOKEN);

    if (!rt) return;

    this.refreshTokens();
    this.setTokenRefreshInterval();
  }

  getUser(): User | undefined {
    if (!this.rt) return undefined;

    const user = jwt_decode<RtTokenPayload>(this.rt).user;
    return user;
  }

  getAccesToken(): string | undefined {
    return this.at;
  }

  getRefreshToken(): string | undefined {
    return this.rt;
  }

  logout() {
    this.close();
    this.at = undefined;
    localStorage.removeItem(ACCES_TOKEN);

    if (!this.remember) {
      this.rt = undefined;
      localStorage.removeItem(REFRESH_TOKEN);
    }
  }

  close() {
    if (this.logoutTimeout) clearTimeout(this.logoutTimeout);
    if (this.atTiemout) clearTimeout(this.atTiemout);
    this.onLoginStatusChange = undefined;
    this.onLogout = undefined;
  }

  private setTokenRefreshInterval() {
    if (!this.at || !this.rt) return;
    const { iat, exp } = jwt_decode<{ exp: number; iat: number }>(this.at);
    const atRefreshItervalTime = Math.abs(exp - iat) - 10 * 1000;

    setInterval(async () => {
      this.refreshTokens();
    }, atRefreshItervalTime);
  }

  async login(email: string, password: string, remember: boolean) {
    this.email = email;
    this.password = password;
    this.remember = remember;

    try {
      await this.getTokens();
      this.setTokenRefreshInterval();
    } catch (e) {
      console.error(e);
    }
  }
}
