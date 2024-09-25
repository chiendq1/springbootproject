<template>
  <div class="text-center" 
    v-if="listData.length < totalItems"
  >
    <span
      @click="$emit('loadMore')"
      
      class="loadmore"
    >
      {{ $t("common.load_more") }}
    </span>
  </div>
</template>
<script>
export default {
  props: {
    listData: {
      type: Array,
      default: [],
    },
    totalItems: {
      type: Number,
      default: 0,
    },
  },
  setup() {},
};
</script>

<style lang="scss" scoped></style>
