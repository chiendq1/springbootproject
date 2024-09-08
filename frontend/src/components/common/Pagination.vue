<template>
  <div class="pagination">
    <ul class="page-unit" v-if="hasOptionPaginate">
      <li class="page-unit__list" :class="number === 10 && 'active'">
        <a href="#" @click="handleSizeChange(10)">10</a>｜
      </li>
      <li class="page-unit__list" :class="number === 50 && 'active'">
        <a href="#" @click="handleSizeChange(50)">50</a>｜
      </li>
      <li class="page-unit__list" :class="number === 100 && 'active'">
        <a href="#" @click="handleSizeChange(100)">100</a>
      </li>
    </ul>

    <Paginate
      :value="currentPage"
      :page-count="paginate.last_page"
      :click-handler="clickCallback"
      container-class="pagination__list"
      page-class="pagination__list-item"
      prev-class="pagination__list-item previous"
      next-class="pagination__list-item next"
      disabled-class="pagination__list-item disabled"
      :prev-text="`<span class='icon-abui-arrow-left'></span>`"
      :next-text="`<span class='icon-abui-arrow-right'></span>`"
    ></Paginate>
  </div>
</template>

<script>
import Paginate from 'vuejs-paginate/src/components/Paginate.vue';

export default {
  emits: ['change-number', 'change-page'],
  components: {
    Paginate,
  },
  props: {
    paginate: {
      type: Object,
      default: () => {
        return {
          page: 1,
          per_page: 10,
        };
      },
      require: true,
    },
    hasOptionPaginate: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      currentPage: this.paginate.page,
      number: this.paginate.per_page,
      options: [10, 50, 100],
    };
  },
  watch: {
    paginate: {
      handler: function (newValue) {
        this.currentPage = newValue.current_page;
        this.number = newValue.per_page;
      },
      deep: true,
    },
  },
  methods: {
    clickCallback(pageNum) {
      this.$emit('change-page', pageNum);
    },
    handleSizeChange(val) {
      this.number = val;
      this.$emit('change-number', val);
    },
  },
};
</script>
