<template>
  <svg
    :fill="fill"
    :height="height"
    :width="width"
    version="1.1"
    id="Layer_1"
    xmlns="http://www.w3.org/2000/svg"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    viewBox="0 0 512 512"
    xml:space="preserve"
  >
    <g id="SVGRepo_bgCarrier" stroke-width="0" />

    <g
      id="SVGRepo_tracerCarrier"
      stroke-linecap="round"
      stroke-linejoin="round"
    />

    <g id="SVGRepo_iconCarrier">
      <g>
        <g>
          <path
            d="M55.652,0v512h44.522v-33.391h33.391V512h55.652v-33.391h33.391V512h66.783v-33.391h33.391V512h55.652v-33.391h33.391V512 h44.522V0H55.652z M278.261,389.565H122.435v-33.391h155.826V389.565z M278.261,289.391H122.435V256h155.826V289.391z M278.261,200.348H122.435v-33.391h155.826V200.348z M389.565,389.565h-66.783v-33.391h66.783V389.565z M389.565,289.391h-66.783 V256h66.783V289.391z M389.565,200.348h-66.783v-33.391h66.783V200.348z M389.565,111.304h-267.13V77.913h267.13V111.304z"
          />
        </g>
      </g>
    </g>
  </svg>
</template>

<script>
import { COLOR_GRAY_1 } from "../constants/color";

export default {
  props: {
    iconName: {
      type: String,
      default: "icon-bill",
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
