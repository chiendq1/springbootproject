<template>
  <svg
    :width="width"
    :height="height"
    viewBox="0 0 16 16"
    :fill="fill"
    xmlns="http://www.w3.org/2000/svg"
  >
    <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
    <g
      id="SVGRepo_tracerCarrier"
      stroke-linecap="round"
      stroke-linejoin="round"
    ></g>
    <g id="SVGRepo_iconCarrier">
      <path
        fill-rule="evenodd"
        clip-rule="evenodd"
        d="M3 0H13V14H15V16H1V14H3V0ZM11 8C11 8.55229 10.5523 9 10 9C9.44772 9 9 8.55229 9 8C9 7.44772 9.44772 7 10 7C10.5523 7 11 7.44772 11 8Z"
        fill="#A6ABC8"
      ></path>
    </g>
  </svg>
</template>
<script>
import { COLOR_GRAY_1 } from "../constants/color";

export default {
  props: {
    iconName: {
      type: String,
      default: "icon-room",
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
