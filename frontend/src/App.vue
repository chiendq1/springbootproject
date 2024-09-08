<template>
  <router-view></router-view>
</template>

<script>
import { ref } from "vue";

export default {
  setup() {
    const message = ref("Hello world");

    // Return it inside an object
    return { message };
  },
};
</script>

<style lang="scss">
@import "@/assets/styles/app.scss";

body {
  font-family: "Noto Sans JP", sans-serif;
  font-size: $font-size-base;
  color: $text-color;
  line-height: $line-height-base;
  background-color: $body-bg;
}

.main-layout {
  max-height: 91vh;
  overflow-x: hidden;
  overflow-y: auto;
  padding-top: 2px;
}
a {
  color: $link-color;
  text-decoration: none;
  &:hover,
  &:focus {
    color: $link-hover-color;
    text-decoration: none;
    outline: none !important;
  }
  &:focus {
    outline: none !important;
    outline-offset: -2px;
  }
}
.el-select__tags,
.el-tag__content {
  height: 100% !important;
}
</style>
