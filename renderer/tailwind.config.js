/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./pages/**/*.{js,ts,jsx,tsx}", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      height: {
        112: "28rem",
        112: "28rem",
        120: "30rem",
      },
      minHeight: {
        32: "8rem",
        48: "12rem",
        64: "16rem",
      },
    },
  },
  plugins: [require("@tailwindcss/typography"), require("daisyui")],
};
