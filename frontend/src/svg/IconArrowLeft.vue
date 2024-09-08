<template>
  <svg 
  :width="width" 
  :height="height" 
  viewBox="0 0 18 18" 
  fill="none" 
  xmlns="http://www.w3.org/2000/svg">
  
  <path d="M9.62042 13.0463L4.08625 7.5L9.62042 1.95375L7.91667 0.25L0.666672 7.5L7.91667 14.75L9.62042 13.0463Z"
      :fill="fill" />
  </svg>
</template>

<script>
import { COLOR_GRAY_1 } from "../constants/color";

export default {
  props: {
    iconName: {
      type: String,
      default: "icon-arrow-left",
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
      default: COLOR_GRAY_1,
    },
  },
};
</script>
