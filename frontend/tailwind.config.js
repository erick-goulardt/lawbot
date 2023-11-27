/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}"
  ],
  theme: {
    extend: {
      fontFamily: {
        lightInter: ['Inter'],
        boldInter: ['Inter'],
        breeSerif: ['Bree Serif']
      }
    },
  },
  plugins: [],
}

