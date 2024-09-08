<template>
  <el-select
    v-model="selectedItem"
    :popper-append-to-body="false"
    popper-class="custom-dropdown-select"
    @visible-change="resetDataSelect"
    title="All"
    :placeholder="placeholder || ''"
    :disabled="isDisabled"
    :clearable="showClearable"
    ref="multipleOptionSelect"
    multiple
  >
    <div class="bs-searchbox" onclick="event.stopPropagation()">
      <input
        v-if="isDisplaySearch && !isRemote"
        type="text"
        class="form-control"
        autocomplete="off"
        v-model="searchName"
        role="tetxbox"
        aria-label="Search"
      />
      <input
        v-if="isDisplaySearch && isRemote"
        type="text"
        class="form-control"
        autocomplete="off"
        @keyup="remoteSearch($event)"
        role="tetxbox"
        aria-label="Search"
      />
    </div>
    <div v-if="!checkEmpty(filteredSearchData)">
      <el-option
        v-for="item in filteredSearchData"
        :key="item.id"
        :label="item.value"
        :value="item.id"
        :class="item.id == -1 && 'border-top'"
        :disabled="usingListItems.includes(item.id)"
      >
        <div class="el-custom-select-dropdown">
          <span class="dropdown-option-name">
            {{ item.value }}
          </span>
        </div>
      </el-option>
    </div>
    <div
      v-else-if="
        (checkEmpty(filteredSearchData) && !checkEmpty(listData)) || isSearching
      "
    >
      <el-option value="" disabled>{{
        $t('common.no_results_found')
      }}</el-option>
    </div>
    <div v-else>
      <p class="no-data">{{ $t('common.no_data') }}</p>
    </div>
  </el-select>
</template>
<script>
import mixins from "@/helpers/mixins";

export default {
  name: "SingleOptionSelect",
  emits: ["handleSelectedParams"],
  mixins: [mixins],
  props: {
    listData: {
      type: Array,
      default: () => [],
    },
    showClearable: {
      type: Boolean,
      default: false,
    },
    usingListItems: {
      type: Array,
      default: () => [],
    },
    isDisplaySearch: {
      type: Boolean,
      default: true,
    },
    defaultList: {
      type: [String, Number, Array],
      default: () => [] || 0 || "",
    },
    labelShow: {
      type: String,
      default: "name",
    },
    isDisabled: {
      type: Boolean,
      default: false,
    },
    isCurrency: {
      type: Boolean,
      default: false,
    },
    optionIndex: {
      type: Object,
      default: () => {
        return {
          haveIndex: false,
          index: 0,
        };
      },
    },
    placeholder: {
      type: String,
      default: "",
    },
    haveSelectAllOption: {
      type: Boolean,
      default: false,
    },
    isRemote: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      searchName: "",
      selectedItem: "",
      isSearching: false,
    };
  },
  created() {
    if (!this.checkEmpty(this.defaultList)) {
      this.selectedItem = this.defaultList;
    }
  },
  computed: {
    filteredSearchData() {
      let lowerCaseSearch = this.searchName.toLowerCase() || "";
      let searchList = [];
      searchList = this.listData.filter((item) =>
        item.value.toLowerCase().includes(lowerCaseSearch)
      );
      //Add option select all
      if (this.haveSelectAllOption && !this.checkEmpty(searchList))
        searchList.push({
          id: -1,
          value: "All",
        });
      return searchList;
    },
  },
  watch: {
    defaultList(value) {
      this.selectedItem = value;
    },
    selectedItem() {
      if (this.optionIndex.haveIndex) {
        this.$emit(
          "handleSelectedParams",
          this.selectedItem,
          this.optionIndex.index
        );
      } else {
        this.$emit("handleSelectedParams", this.selectedItem);
      }
    },
  },
  methods: {
    resetDataSelect() {
      this.searchName = "";
    },
    remoteSearch($event) {
      this.isSearching = true;
      this.$emit("remoteSearch", $event.target.value);
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
.item-option-none-checkbox {
  padding-left: 0 !important;
}
.bs-searchbox {
  margin-bottom: 5px;
  padding-top: 0;
}
.no-hover {
  pointer-events: none !important;
}
.el-select-dropdown__item {
  padding-left: 12px !important;
}
.dropdown-option-name {
  display: inline-block;
  width: 100%;
  white-space: nowrap;
  overflow: hidden !important;
  text-overflow: ellipsis;
}
</style>
