<template>
  <div class="contract contract-list">
    <div class="contract-header">
      <h3 class="page__ttl">{{ $t("contract.title") }}</h3>
      <div class="contract-btn-box contract-import-box">
        <el-row
          v-if="highest_role == ADMIN || highest_role == LANDLORD"
          class="mb-4"
        >
          <el-button class="btn btn-save" @click=""
            >{{ $t("contract.add_new") }}
          </el-button>
        </el-row>
      </div>
    </div>
    <div class="contract-body">
      <div class="contract-search">
        <div class="contract-search-box col-md-9 col-lg-9">
          <p class="contract-search__ttl">
            {{ $t("contract.keyword") }}
          </p>
          <div class="mb-0 ruleform">
            <el-input
              :placeholder="$t('common.input_keyword')"
              @keyup.enter="submitForm"
              v-model="searchForms.searchValue"
              prop="searchValue"
            >
              <template #append>
                <span @click="handleSearchForm" class="btn-setting">
                  <IconSetting />
                </span>
              </template>
            </el-input>
          </div>
        </div>
        <div class="btn-search-select col-md-3 col-lg-3 contract-box-btn-all">
          <el-button class="btn btn-search" @click="submitForm()">
            {{ $t("common.search") }}</el-button
          >
          <el-button class="btn btn-clear" @click="handleClear()">
            {{ $t("common.clear") }}</el-button
          >
        </div>
      </div>
      <div class="form-search" :class="{ active: isShowBoxSearch }">
        <div class="close-form">
          <IconCircleClose @click="isShowBoxSearch = false" />
        </div>
        <div class="form-search-box">
          <div class="item">
            <el-form-item :label="$t('contract.date')">
              <el-date-picker
                v-model="searchForms.dateRange"
                type="daterange"
                range-separator="To"
                :start-placeholder="$t('common.start_date')"
                :end-placeholder="$t('common.end_date')"
                :size="default"
              />
            </el-form-item>
            <el-form-item :label="$t('contract.status')">
              <SingleOptionSelect
                v-model="searchForms.status"
                :listData="listStatuses"
                :placeholder="$t('contract.status_placeholder')"
                :isRemote="true"
                :disabled="isDisabled"
              />
            </el-form-item>
            <el-form-item :label="$t('contract.type')">
              <SingleOptionSelect
                v-model="searchForms.type"
                :listData="listTypes"
                :placeholder="$t('contract.type_placeholder')"
                :isRemote="true"
                :disabled="isDisabled"
              />
            </el-form-item>
          </div>
          <div class="item">
            <el-form-item :label="$t('contract.room')">
              <SingleOptionSelect
                v-model="searchForms.roomId"
                :listData="listRoomsByRole.value"
                :placeholder="$t('contract.room_placeholder')"
                :isRemote="true"
                :disabled="isDisabled"
              />
            </el-form-item>
            <el-form-item :label="$t('contract.tenant')">
              <SingleOptionSelect
                v-model="searchForms.tenant"
                :listData="listTenants.value"
                :placeholder="$t('contract.tenant_placeholder')"
                :isRemote="true"
                :disabled="isDisabled"
              />
            </el-form-item>
            <el-form-item :label="$t('contract.landlord')">
              <SingleOptionSelect
                v-model="searchForms.landlordId"
                :listData="listLandlords.value"
                :placeholder="$t('contract.landlord_placeholder')"
                :isRemote="true"
                :disabled="isDisabled"
              />
            </el-form-item>
          </div>
        </div>
      </div>
    </div>

    <div class="bidding-body-table" style="margin-top: 16px; min-height: 400px">
      <ContractTable :data="listContracts.value" />
      <LoadMore
        :listData="listContracts.value"
        :totalItems="totalItems.value"
        @loadMore="handleLoadMore"
      />
    </div>
  </div>
</template>

<script>
import IconSetting from "@/svg/IconSettingMain.vue";
import IconCircleClose from "@/svg/IconCircleClose.vue";
import { NUMBER_FORMAT } from "@/constants/application.js";
import SingleOptionSelect from "@/components/common/SingleOptionSelect.vue";
import { onMounted, ref } from "vue";
import { ADMIN, LANDLORD } from "@/constants/roles.js";
import { useI18n } from "vue-i18n";
import LoadMore from "@/components/common/LoadMore.vue";
import Cookies from "js-cookie";
import ContractTable from "./item/ContractTable.vue";
import { useContractStore } from "@/store/contracts.js";
import { useUserStore } from "@/store/users.js";
import { useRoomStore } from "@/store/rooms.js";

export default {
  name: "UserList",
  components: {
    IconSetting,
    IconCircleClose,
    SingleOptionSelect,
    LoadMore,
    ContractTable,
  },
  setup() {
    const searchForms = ref({
      searchValue: "",
      status: null,
      roomId: null,
      startDate: "",
      endDate: "",
      tenant: null,
      type: null,
      landlordId: null,
      pageNo: 0,
    });
    const contractStore = useContractStore();
    const userStore = useUserStore();
    const roomStore = useRoomStore();
    const { listContracts, getListContracts, totalItems, currentPage } = contractStore;
    const { getListUsersByRole, listLandlords, listTenants } = userStore;
    const { getListRoomsByRole,listRoomsByRole } = roomStore;
    const highest_role = Cookies.get("highest_role");
    const { t } = useI18n();
    const isDisabled = ref(false);
    const isShowBoxSearch = ref(false);
    const listStatuses = ref([
      {
        id: 0,
        value: t("contract.statuses.active"),
      },
      {
        id: 1,
        value: t("contract.statuses.terminated"),
      },
      {
        id: 2,
        value: t("contract.statuses.renewed"),
      },
    ]);
    const listTypes = ref([
      {
        id: 0,
        value: t("contract.types.monthly"),
      },
      {
        id: 1,
        value: t("contract.types.quater"),
      },
      {
        id: 2,
        value: t("contract.types.half-year"),
      },
      {
        id: 3,
        value: t("contract.types.year"),
      },
    ]);

    onMounted(() => {
      getListContracts(searchForms.value);
      getListUsersByRole();
      getListRoomsByRole();
    });

    const handleSearchForm = () => {
      isShowBoxSearch.value = !isShowBoxSearch.value;
    };

    const handleClear = () => {
      searchForms.value.filterValue = "";
      searchForms.value.searchValue = "";
    };

    const submitForm = () => {
      searchForms.value.pageNo = 0;
      isShowBoxSearch.value = false;
    };

    const handleLoadMore = () => {
      currentPage.value++;
      searchForms.value.pageNo++;
      getListContracts(searchForms.value);
    };

    return {
      searchForms,
      isShowBoxSearch,
      NUMBER_FORMAT,
      highest_role,
      ADMIN,
      LANDLORD,
      isDisabled,
      totalItems,
      listLandlords,
      listContracts,
      listStatuses,
      listTenants,
      listTypes,
      listRoomsByRole,
      handleSearchForm,
      handleClear,
      submitForm,
      handleLoadMore,
    };
  },
};
</script>

<style lang="scss" scoped>
.close-form {
  position: absolute;
  display: flex;
  justify-content: end;
  right: 16px;
  top: 10px;
  cursor: pointer;
  svg {
    height: 30px;
  }
}
</style>
