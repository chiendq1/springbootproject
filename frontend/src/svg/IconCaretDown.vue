<template>
  <svg
    :width="width"
    :height="height"
    viewBox="0 0 1024 1024"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path d="M512 640L256 384H768L512 640Z" :fill="fill" />
  </svg>
</template>

<script>
import { COLOR_WHITE_2 } from "../constants/color";

export default {
  props: {
    iconName: {
      type: String,
      default: "icon-caret-down",
    },
    width: {
      type: [Number, String],
      default: 18,
    },
    height: {
      type: [Number, String],
      default: 18,
    },
    fill: {
      type: String,
      default: COLOR_WHITE_2,
    },
  },
};
</script>
