<template>
  <svg
    fill="#ff0000"
    :height="height"
    :width="width"
    version="1.1"
    xmlns:sketch="http://www.bohemiancoding.com/sketch/ns"
    xmlns="http://www.w3.org/2000/svg"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    viewBox="0 0 24 24"
    xml:space="preserve"
    stroke="#ff0000"
  >
    <g id="SVGRepo_bgCarrier" stroke-width="0" />

    <g
      id="SVGRepo_tracerCarrier"
      stroke-linecap="round"
      stroke-linejoin="round"
    />

    <g id="SVGRepo_iconCarrier">
      <g id="inactive">
        <path
          d="M13.6,23.9c-7.8,1-14.5-5.6-13.5-13.5c0.7-5.3,5-9.7,10.3-10.3c7.8-1,14.5,5.6,13.5,13.5C23.2,18.9,18.9,23.2,13.6,23.9z M13.7,2.1C6.9,1,1,6.9,2.1,13.7c0.7,4.1,4,7.5,8.2,8.2C17.1,23,23,17.1,21.9,10.3C21.2,6.2,17.8,2.8,13.7,2.1z"
        />
        <polyline points="5.6,4.2 19.8,18.3 18.4,19.8 4.2,5.6 " />
      </g>
    </g>
  </svg>
</template>

<script>

export default {
  props: {
    iconName: {
      type: String,
      default: "icon-inactive",
    },
    width: {
      type: [Number, String],
      default: 18,
    },
    height: {
      type: [Number, String],
      default: 18,
    }
  },
};
</script>
