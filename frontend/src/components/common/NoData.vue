<template>
  <div class="content">
      <IconNotFound :width="width" :height="height"/>
      {{ message || $t("common.no_data") }}
  </div>
</template>
<script>
import IconNotFound from "@/svg/IconNotFound.vue";

export default {
  components: {
    IconNotFound,
  },
  props: {
    width: {
      type: Number,
      default: 64,
    },
    height: {
      type: Number,
      default: 40,
    },
    message: {
      type: String,
      default: '',
    },
  },
  setup() {}
};
</script>

<style lang="scss" scoped>
.content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
</style>
