const { tokens } = require("./src/commons/styles/theme");

/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  presets: [require("nativewind/preset")],
  theme: {
    extend: {
      colors: {
        primary: tokens.colors.primary,
        background: tokens.colors.background,
        surface: tokens.colors.surface,
        stroke: tokens.colors.stroke,
        "text-primary": tokens.colors.text.primary,
        "text-secondary": tokens.colors.text.secondary,
        "status-success": tokens.colors.status.success,
        "status-warning": tokens.colors.status.warning,
        "status-error": tokens.colors.status.error
      },
      borderRadius: {
        premium: `${tokens.radius.premium}px`,
        card: `${tokens.radius.card}px`,
      },
      fontFamily: {
        sans: ["Inter-Regular"],
        semibold: ["Inter-SemiBold"],
        bold: ["Inter-Bold"],
      },
    },
  },
  plugins: [],
}