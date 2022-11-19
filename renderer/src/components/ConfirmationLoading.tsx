import React, { useEffect, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/router";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSync } from "@fortawesome/free-solid-svg-icons";

const AUTH_SERVICE_URL =
  process.env.AUTH_SERVICE_URL ?? "http://localhost:8080";

enum ConfirmationStatus {
  IN_PROGGRES = "IN_PROGGRES",
  CONFIRMED = "CONFIRMED",
  EXPIRED = "EXPIRED",
  ERROR = "ERROR",
}

const statusMessages: { [key in ConfirmationStatus]?: any } = {
  ERROR: "Wystąpił nieoczekiwany problem",
  CONFIRMED: "Pomyślnie zatwierdzono konto",
  EXPIRED: "Twój link do potwierdzania kota wygasł. wysłaliśmy nowy",
  IN_PROGGRES: "Trwa weryfikacja konta",
};

const ConfirmationLoading = () => {
  const [confirmationStatus, setConfirmationStatus] =
    useState<ConfirmationStatus>(ConfirmationStatus.IN_PROGGRES);
  const router = useRouter();
  const { token } = router.query;

  useEffect(() => {
    if (!token) {
      return;
    }

    const sendConfirmation = async () => {
      const data = await fetch(
        `${AUTH_SERVICE_URL}/registration/confirm_registration?token=${token}`
      );

      if (data.ok) {
        setConfirmationStatus(ConfirmationStatus.CONFIRMED);
      } else if (data.status == 410) {
        setConfirmationStatus(ConfirmationStatus.EXPIRED);
      } else {
        setConfirmationStatus(ConfirmationStatus.ERROR);
      }
    };

    try {
      sendConfirmation();
    } catch (e: any) {
      setConfirmationStatus(ConfirmationStatus.ERROR);
    }
  }, [token]);

  return (
    <div className="h-full max-w-xl min-w-[16rem] w-full bg-base-100 p-8 flex flex-col gap-5 rounded-md">
      <h1 className="text-4xl mb-10 text-center">
        {statusMessages[confirmationStatus]}
      </h1>
      <div>
        {confirmationStatus == ConfirmationStatus.IN_PROGGRES ? (
          <FontAwesomeIcon
            className="fadeIn fa-spin w-full mb-10"
            icon={faSync}
            size="10x"
          />
        ) : (
          <FontAwesomeIcon
            className="fadeIn w-full mb-10"
            icon={faSync}
            size="10x"
          />
        )}
      </div>
      {confirmationStatus == ConfirmationStatus.CONFIRMED ? (
        <Link href="/login">
          <a className="btn btn-primary w-full max-w-xs mx-auto my-2 relative">
            Zaloguj
          </a>
        </Link>
      ) : confirmationStatus == ConfirmationStatus.ERROR ? (
        <Link href="/register">
          <a className="btn btn-primary w-full max-w-xs mx-auto my-2 relative">
            Zarejestruj się
          </a>
        </Link>
      ) : confirmationStatus == ConfirmationStatus.EXPIRED ? (
        <h3 className="text-4xl mb-10 text-center">
          Nowy link został wysłany na Twojego emaila
        </h3>
      ) : (
        <div></div>
      )}
    </div>
  );
};

export default ConfirmationLoading;
