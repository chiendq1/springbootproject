<template>
  <el-select
    v-model="selectedItem"
    :placeholder="$t('trip_item.right_menu.manual_input_model.unspecified')"
    popper-class="custom-dropdown-select"
    :popper-append-to-body="false"
  >
    <div class="bs-searchbox" onclick="event.stopPropagation()">
      <input
        type="text"
        class="form-control"
        autocomplete="off"
        v-model="searchName"
        role="tetxbox"
        aria-label="Search"
      />
    </div>
    <div v-if="!checkEmpty(filteredSearchData)">
      <el-option-group v-for="(parent, index) in filteredSearchData" :key="index" :label="parent.name">
        <el-option
          v-for="item in parent.data"
          :key="item.id"
          :label="item.name"
          :value="item.id"
          @click="isActionSelect = true"
        >
          <div class="el-custom-select-dropdown">
            <span class="dropdown-option-name">
              {{ item.name }}
            </span>
          </div>
          <li class="divider" />
          <hr />
        </el-option>
      </el-option-group>
    </div>
    <div v-else-if="checkEmpty(filteredSearchData) && !checkEmpty(listData)">
      <el-option value="" disabled>{{ $t('common.no_results_matched', { item: searchName }) }}</el-option>
    </div>
    <div v-else>
      <p class="no-data">{{ $t('common.no_data') }}</p>
    </div>
  </el-select>
</template>
<script>
import mixins from '@/helpers/mixins';

export default {
  name: 'SingleGroupOptionSelectExpenseCostItem',
  emits: ['handleSelectedParams'],
  mixins: [mixins],
  props: {
    listData: {
      type: Array,
      default: () => [],
    },
    labelShow: {
      type: String,
      default: () => 'name',
    },
    initItem: {
      type: [String, Number, Array, Object],
      default: () => {},
    },
  },
  data() {
    return {
      searchName: '',
      selectedItem: '',
      isActionSelect: false,
    };
  },
  created() {
    this.selectedItem = this.initItem;
  },
  watch: {
    selectedItem() {
      this.$emit('handleSelectedParams', this.selectedItem, this.isActionSelect);
    },
    initItem(val) {
      this.selectedItem = val;
    },
  },
  computed: {
    filteredSearchData() {
      const lowerCaseSearch = this.searchName.trim().toLowerCase();
      if (this.checkEmpty(lowerCaseSearch)) {
        return this.listData;
      } else {
        let result = [];
        this.listData.forEach(parent => {
          let listMatchedChild = [];
          parent.data.forEach(item => {
            if (item.name.toLowerCase().includes(lowerCaseSearch)) {
              listMatchedChild.push(item);
            }
          });
          if (!this.checkEmpty(listMatchedChild))
            result.push({
              name: parent.name,
              code: parent.code,
              tax_type: parent.tax_type,
              display_order: parent.display_order,
              data: listMatchedChild,
            });
        });
        return result;
      }
    },
  },
  methods: {
    resetDataSelect() {
      this.isActionSelect = false;
      this.searchName = '';
    },
  },
};
</script>
<style scoped>
.dropdown-option-icon {
  margin-top: 50%;
}
.dropdown-option-icon span.first-icon.icon-abui-checkbox-unchecked {
  font-size: 15px;
  color: rgba(152, 169, 176, 0.5);
}
.dropdown-option-icon span.second-icon.icon-abui-checkbox-checked {
  font-size: 15px;
  color: rgba(255, 255, 255, 0.8) !important;
}
.bs-searchbox {
  margin-bottom: 5px;
}
.no-hover {
  pointer-events: none !important;
}
.border {
  border-bottom: 1px solid #d0d6da;
}
</style>
