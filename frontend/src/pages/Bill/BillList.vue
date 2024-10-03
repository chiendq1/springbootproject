<template>
  <div class="bill bill-list">
    <div class="bill-header">
      <h3 class="page__ttl">{{ $t("bill.title") }}</h3>
      <div class="bill-btn-box bill-import-box">
        <el-row
          v-if="highest_role == ADMIN || highest_role == LANDLORD"
          class="mb-4"
        >
          <el-button class="btn btn-save" @click="handleCreateBill()"
            >{{ $t("bill.add_new") }}
          </el-button>
        </el-row>
      </div>
    </div>
    <div class="bill-body">
      <div class="bill-search">
        <div class="bill-search-box col-md-9 col-lg-9">
          <p class="bill-search__ttl">
            {{ $t("bill.keyword") }}
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
        <div class="btn-search-select col-md-3 col-lg-3 bill-box-btn-all">
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
            <el-form-item :label="$t('bill.date_month')">
              <el-date-picker
                style="width: 100%"
                v-model="searchForms.month"
                type="month"
                :placeholder="$t('bill.date_month')"
                :size="default"
                @change="handleChangeDate"
              />
            </el-form-item>
            <el-form-item :label="$t('bill.status')">
              <SingleOptionSelect
                v-model="searchForms.status"
                :listData="listStatuses.value"
                :placeholder="$t('bill.status_placeholder')"
                :isRemote="true"
                :disabled="isDisabled"
              />
            </el-form-item>
          </div>
          <div class="item">
            <el-form-item :label="$t('bill.room')">
              <SingleOptionSelect
                v-model="searchForms.roomId"
                :listData="listRoomsByRole.value"
                :placeholder="$t('bill.room_placeholder')"
                :isRemote="true"
                :disabled="isDisabled"
              />
            </el-form-item>
          </div>
        </div>
      </div>
    </div>

    <div class="bidding-body-table" style="margin-top: 16px; min-height: 400px">
      <BillTable
        :data="listBills.value"
        @download="handleDownloadPDF"
        @details="handleGetBillDetails"
      />
      <LoadMore
        :listData="listBills.value"
        :totalItems="totalItems.value"
        @loadMore="handleLoadMore"
      />
    </div>
  </div>
</template>

<script>
import IconSetting from "@/svg/IconSettingMain.vue";
import IconCircleClose from "@/svg/IconCircleClose.vue";
import SingleOptionSelect from "@/components/common/SingleOptionSelect.vue";
import LoadMore from "@/components/common/LoadMore.vue";
import ModalConfirm from "@/components/common/ModalConfirm.vue";
import Cookies from "js-cookie";
import PAGE_NAME from "@/constants/route-name.js";
import BillTable from "./item/BillTable.vue";
import { onMounted, onUnmounted, ref } from "vue";
import { ADMIN, LANDLORD } from "@/constants/roles.js";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";
import { useBillStore } from "@/store/bills.js";
import { useRoomStore } from "@/store/rooms.js";
import { mixinMethods } from "@/utils/variables";

export default {
  name: "UserList",
  components: {
    IconSetting,
    IconCircleClose,
    SingleOptionSelect,
    LoadMore,
    BillTable,
    ModalConfirm,
  },
  setup() {
    const searchForms = ref({
      searchValue: "",
      status: null,
      roomId: null,
      month: "",
      pageNo: 0,
    });
    const roomStore = useRoomStore();
    const router = useRouter();
    const billStore = useBillStore();
    const { getListRoomsByRole, listRoomsByRole } = roomStore;
    const {
      listBills,
      validation,
      currentPage,
      resetBillDetails,
      totalItems,
      listStatuses,
      getBillPdf,
      getBillDetails,
      getListBills,
    } = billStore;
    const highest_role = Cookies.get("highest_role");
    const { t } = useI18n();
    const isDisabled = ref(false);
    const isShowBoxSearch = ref(false);

    onMounted(() => {
      resetBillDetails();
      searchForms.value.pageNo = 0;
      currentPage.value = 0;
      getListBills();
      getListRoomsByRole();
    });

    onUnmounted(() => {});

    const handleSearchForm = () => {
      isShowBoxSearch.value = !isShowBoxSearch.value;
    };

    const handleClear = () => {
      searchForms.value.searchValue = "";
      searchForms.value.status = null;
      searchForms.value.roomId = null;
      searchForms.value.month = "";
    };

    const submitForm = () => {
      searchForms.value.pageNo = 0;
      currentPage.value = 0;
      isShowBoxSearch.value = false;
      getListBills(searchForms.value);
    };

    const handleLoadMore = () => {
      currentPage.value++;
      searchForms.value.pageNo++;
      getListBills(searchForms.value);
    };

    const handleDownloadPDF = (id) => {
      getBillPdf(id);
    };

    const handleCreateBill = () => {
      router.push({ name: PAGE_NAME.BILL.CREATE });
    };

    const handleChangeDate = (date) => {
      searchForms.value.month = mixinMethods.showDateTime(date);
    }

    const handleGetBillDetails = (id) => {
      getBillDetails(id);
    }

    return {
      searchForms,
      isShowBoxSearch,
      highest_role,
      ADMIN,
      LANDLORD,
      isDisabled,
      validation,
      listBills,
      totalItems,
      listStatuses,
      listRoomsByRole,
      handleSearchForm,
      handleClear,
      handleGetBillDetails,
      handleCreateBill,
      handleChangeDate,
      submitForm,
      handleLoadMore,
      handleDownloadPDF,
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
