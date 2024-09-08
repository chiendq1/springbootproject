<template>
  <el-select
    v-model="selectedItem"
    :placeholder="placeholder || $t('common.select_item')"
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
        <el-option v-for="item in parent.data" :key="item.id" :label="item.name" :value="item.id">
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
  name: 'SingleGroupOptionSelect',
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
    placeholder: {
      type: String,
      default: () => '',
    },
  },
  data() {
    return {
      searchName: '',
      selectedItem: '',
    };
  },
  created() {
    if (!this.checkEmpty(this.initItem)) {
      this.selectedItem = this.initItem;
    }
  },
  watch: {
    selectedItem() {
      this.$emit('handleSelectedParams', this.selectedItem);
    },
    initItem(val) {
      if (!this.checkEmpty(val)) {
        this.selectedItem = val;
      }
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
          parent.data.forEach(item => {
            if (item.name.toLowerCase().includes(lowerCaseSearch)) {
              result.push(parent);
            }
          });
        });
        return result;
      }
    },
  },
  methods: {
    resetDataSelect() {
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
