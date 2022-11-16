class AuthError extends Error {
  constructor(msg: string) {
    super(msg);
  }
}

export default AuthError;
