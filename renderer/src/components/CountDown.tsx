import React, { useEffect, useRef, useState } from "react";

type Props = {
  toDate: Date;
};

export const CountDown = ({ toDate }: Props) => {
  const daysFirstHundredRef = useRef<HTMLElement>(null);
  const daysSecondHundredRef = useRef<HTMLElement>(null);
  const hoursRef = useRef<HTMLElement>(null);
  const minutesRef = useRef<HTMLElement>(null);
  const secondsRef = useRef<HTMLElement>(null);

  useEffect(() => {
    const tick = () => {
      const currentTime = new Date().getTime();
      const timeDiff = toDate.getTime() - currentTime;
      const days = timeDiff / (1000 * 60 * 60 * 24);
      const hours = (timeDiff / (1000 * 60 * 60)) % 24;
      const minutes = (timeDiff / (1000 * 60)) % 60;
      const seconds = (timeDiff / 1000) % 60;
      daysFirstHundredRef.current?.style.setProperty(
        "--value",
        (days % 100).toFixed()
      );
      daysSecondHundredRef.current?.style.setProperty(
        "--value",
        (days / 100 % 100).toFixed()
      );
      hoursRef.current?.style.setProperty("--value", hours.toFixed());
      minutesRef.current?.style.setProperty("--value", minutes.toFixed());
      secondsRef.current?.style.setProperty("--value", seconds.toFixed());
    };

    const tickInterval = setInterval(tick, 1000);
    return () => clearTimeout(tickInterval);
  }, [toDate]);

  return (
    <div className="grid grid-flow-row text-center sm:grid-flow-col gap-5 auto-cols-max">
      <div className="flex flex-col p-2 bg-neutral rounded-box text-neutral-content">
        <div>
          <span className="countdown font-mono text-5xl">
            <span
              style={{ "--value": 0 } as any}
              className="w-min mx-auto"
              ref={daysSecondHundredRef}
            ></span>
          </span>
          <span className="countdown font-mono text-5xl">
            <span
              style={{ "--value": 0 } as any}
              className="w-min mx-auto"
              ref={daysFirstHundredRef}
            ></span>
          </span>
        </div>
        dni
      </div>
      <div className="flex flex-col p-2 bg-neutral rounded-box text-neutral-content">
        <span className="countdown font-mono text-5xl">
          <span style={{ "--value": 0 } as any} className="w-min mx-auto" ref={hoursRef}></span>
        </span>
        godzin
      </div>
      <div className="flex flex-col p-2 bg-neutral rounded-box text-neutral-content">
        <span className="countdown font-mono text-5xl">
          <span style={{ "--value": 0} as any} className="w-min mx-auto" ref={minutesRef}></span>
        </span>
        minut
      </div>
      <div className="flex flex-col p-2 bg-neutral rounded-box text-neutral-content">
        <span className="countdown font-mono text-5xl">
          <span style={{ "--value": 0 } as any} className="w-min mx-auto" ref={secondsRef}></span>
        </span>
        sekund
      </div>
    </div>
  );
};
