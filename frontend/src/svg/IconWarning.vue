<template>
  <svg :width="width" :height="height" viewBox="0 0 54 54" fill="none" xmlns="http://www.w3.org/2000/svg">
    <g clip-path="url(#clip0_2582_46359)">
    <rect width="54" height="54" fill="white" fill-opacity="0.01"/>
    <rect x="24.2998" y="8.1001" width="5.4" height="27" rx="2" :fill="fill"/>
    <rect x="24.2998" y="40.5" width="5.4" height="5.4" rx="2" :fill="fill"/>
    <path d="M27 50.625C20.7343 50.625 14.7252 48.1359 10.2946 43.7054C5.86406 39.2748 3.375 33.2657 3.375 27C3.375 20.7343 5.86406 14.7252 10.2946 10.2946C14.7252 5.86406 20.7343 3.375 27 3.375C33.2657 3.375 39.2748 5.86406 43.7054 10.2946C48.1359 14.7252 50.625 20.7343 50.625 27C50.625 33.2657 48.1359 39.2748 43.7054 43.7054C39.2748 48.1359 33.2657 50.625 27 50.625ZM27 54C34.1608 54 41.0284 51.1554 46.0919 46.0919C51.1554 41.0284 54 34.1608 54 27C54 19.8392 51.1554 12.9716 46.0919 7.90812C41.0284 2.84464 34.1608 0 27 0C19.8392 0 12.9716 2.84464 7.90812 7.90812C2.84464 12.9716 0 19.8392 0 27C0 34.1608 2.84464 41.0284 7.90812 46.0919C12.9716 51.1554 19.8392 54 27 54Z" :fill="fill"/>
    </g>
    <defs>
    <clipPath id="clip0_2582_46359">
    <rect width="54" height="54" fill="white"/>
    </clipPath>
    </defs>
  </svg>
</template>

<script>
import { COLOR_RED_1 } from '../constants/color';

export default {
  props: {
    iconName: {
      type: String,
      default: "icon-edit",
    },
    width: {
      type: [Number, String],
      default: 54,
    },
    height: {
      type: [Number, String],
      default: 54,
    },
    fill: {
      type: String,
      default: COLOR_RED_1,
    },
  },
};
</script>
