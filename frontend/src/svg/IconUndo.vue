<template>
  <svg
    class="svg-icon"
    :style="[{ width, height, fill }, 'overflow: hidden; vertical-align: middle;']"
    viewBox="0 0 1024 1024"
    version="1.1"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      d="M862.72 896a42.666667 42.666667 0 0 1-42.666667-32.426667 380.16 380.16 0 0 0-332.8-285.44v47.786667a75.946667 75.946667 0 0 1-46.506666 69.973333A85.333333 85.333333 0 0 1 349.013333 682.666667l-215.893333-188.16a75.093333 75.093333 0 0 1 0-114.346667l215.893333-188.586667a85.333333 85.333333 0 0 1 93.013334-12.8 75.946667 75.946667 0 0 1 46.506666 69.973334V298.666667A464.64 464.64 0 0 1 917.333333 757.333333a439.04 439.04 0 0 1-13.226666 106.24 42.666667 42.666667 0 0 1-42.666667 32.426667z"
    />
  </svg>
</template>

<script>
import { COLOR_WHITE_1 } from "@/constants/color";

export default {
  props: {
    iconName: {
      type: String,
      default: "icon-undo",
    },
    width: {
      type: [Number, String],
      default: '16px',
    },
    height: {
      type: [Number, String],
      default: '16px',
    },
    fill: {
      type: String,
      default: COLOR_WHITE_1,
    },
  },
};
</script>
