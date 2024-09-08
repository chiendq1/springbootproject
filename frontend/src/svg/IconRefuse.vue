<template>
  <svg version="1.0" xmlns="http://www.w3.org/2000/svg" :width="width" :height="height" viewBox="0 0 50.000000 50.000000"
    preserveAspectRatio="xMidYMid meet">
    <g transform="translate(0.000000,50.000000) scale(0.100000,-0.100000)" :fill="fill" stroke="none">
      <path d="M163 374 c-26 -39 -48 -60 -75 -71 -21 -8 -49 -29 -63 -45 -22 -26
        -25 -39 -25 -110 l0 -80 33 7 c17 4 45 13 61 21 24 12 31 12 49 -1 12 -8 24
        -15 28 -15 6 0 169 242 169 251 0 4 -31 27 -70 53 l-70 46 -37 -56z m97 -6
        c27 -18 50 -36 50 -41 0 -4 -10 -22 -21 -40 l-22 -32 -33 33 c-26 26 -41 33
        -59 30 -22 -5 -23 -3 -13 16 17 32 40 66 44 66 2 0 27 -15 54 -32z m-110 -110
        c27 16 30 15 50 -3 16 -14 20 -27 17 -51 -5 -33 -41 -94 -57 -94 -22 0 -18 22
        12 65 30 42 31 46 15 62 -16 16 -19 15 -47 -12 -37 -35 -58 -24 -38 20 9 20
        14 24 16 13 3 -15 6 -15 32 0z" 
      />
      <path d="M236 343 c-11 -11 -6 -23 9 -23 8 0 15 4 15 9 0 13 -16 22 -24 14z" />
      <path d="M364 415 c-10 -26 3 -266 15 -299 16 -40 39 -55 84 -56 l37 0 0 65
        c0 57 -2 65 -19 65 -23 0 -44 52 -63 160 -10 57 -17 76 -31 78 -9 2 -20 -4
        -23 -13z"
      />
    </g>
  </svg>
</template>

<script>
import { COLOR_RED_1 } from "@/constants/color";

export default {
  props: {
    iconName: {
      type: String,
      default: "icon-refuse",
    },
    width: {
      type: [Number, String],
      default: 20,
    },
    height: {
      type: [Number, String],
      default: 20,
    },
    fill: {
      type: String,
      default: COLOR_RED_1,
    },
  },
};
</script>
