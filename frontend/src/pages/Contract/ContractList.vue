<template>
  <div class="contract contract-list">
    <div class="contract-header">
      <h3 class="page__ttl">{{ $t("contract.title") }}</h3>
      <div class="contract-btn-box contract-import-box">
        <el-row
          v-if="highest_role == ADMIN || highest_role == LANDLORD"
          class="mb-4"
        >
          <el-button class="btn btn-save" @click="handleOpenModal(true)"
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
                v-model="dateRange"
                type="daterange"
                range-separator="To"
                :start-placeholder="$t('common.start_date')"
                :end-placeholder="$t('common.end_date')"
                :size="default"
                @change="handleChangeDate"
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
                :listData="listTypes.value"
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
                v-model="searchForms.tenantId"
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
      <ContractTable
        :data="listContracts.value"
        @download="handleDownloadPDF"
        @details="handleGetContractDetails"
      />
      <LoadMore
        :listData="listContracts.value"
        :totalItems="totalItems.value"
        @loadMore="handleLoadMore"
      />
    </div>
    <ContractSaveModal
      :title="modalTitle"
      :show="isShowModalSave.value"
      :contractDetails="contractDetails.value"
      :validation="validation"
      :highestRole="highest_role"
      :listTypes="listTypes.value"
      :isViewDetails="isViewDetails.value"
      :rangeDate="dateRangeModal"
      :listRoomsByRole="listRoomsByRole.value"
      :listLandlord="listLandlords.value"
      @close="handleCloseModal"
      @submit="handleSaveContract"
      @extend="handleSaveContract"
      @terminate="handleTerminateContract"
    />
    <ModalConfirm
      :isShowModal="isShowModalConfirm.value"
      @close-modal="handleCloseModalConfirm"
      :isConfirmByText="true"
      :confirmText="TEXT_CONFIRM_TERMINATE"
      @confirmAction="handleConfirm"
      :message="$t('contract.modal_confirm.message')"
      :title="$t('contract.modal_confirm.title')"
    />
  </div>
</template>

<script>
import IconSetting from "@/svg/IconSettingMain.vue";
import IconCircleClose from "@/svg/IconCircleClose.vue";
import SingleOptionSelect from "@/components/common/SingleOptionSelect.vue";
import LoadMore from "@/components/common/LoadMore.vue";
import ModalConfirm from "@/components/common/ModalConfirm.vue";
import Cookies from "js-cookie";
import ContractTable from "./item/ContractTable.vue";
import ContractSaveModal from "./item/SaveModal.vue";
import { onMounted, onUnmounted, ref } from "vue";
import { NUMBER_FORMAT } from "@/constants/application.js";
import { TEXT_CONFIRM_TERMINATE } from "@/constants/application.js";
import { ADMIN, LANDLORD } from "@/constants/roles.js";
import { useI18n } from "vue-i18n";
import { useContractStore } from "@/store/contracts.js";
import { useUserStore } from "@/store/users.js";
import { useRoomStore } from "@/store/rooms.js";
import { mixinMethods } from "@/utils/variables";

export default {
  name: "UserList",
  components: {
    IconSetting,
    IconCircleClose,
    SingleOptionSelect,
    LoadMore,
    ContractTable,
    ModalConfirm,
    ContractSaveModal,
  },
  setup() {
    const searchForms = ref({
      searchValue: "",
      status: null,
      roomId: null,
      startDate: "",
      endDate: "",
      tenantId: null,
      type: null,
      landlordId: null,
      pageNo: 0,
    });
    const dateRange = ref([]);
    const contractStore = useContractStore();
    const userStore = useUserStore();
    const roomStore = useRoomStore();
    const {
      listContracts,
      getListContracts,
      isShowModalSave,
      listTypes,
      validation,
      isShowModalConfirm,
      isViewDetails,
      totalItems,
      contractDetails,
      dateRangeModal,
      currentPage,
      updateContract,
      getContractDetails,
      resetContractDetails,
      getContractPdf,
      createNewContract,
    } = contractStore;
    const {
      listFreeTenants,
      listLandlords,
      listTenants,
      getListUsersByRole,
      getListFreeTenants,
    } = userStore;
    const { getListRoomsByRole, listRoomsByRole } = roomStore;
    const modalTitle = ref("");
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

    onMounted(() => {
      getListContracts(searchForms.value);
      getListFreeTenants();
      getListUsersByRole();
      getListRoomsByRole();
    });

    onUnmounted(() => {
      listContracts.value = [];
      listLandlords.value = [];
      listTenants.value = [];
    });

    const handleSearchForm = () => {
      isShowBoxSearch.value = !isShowBoxSearch.value;
    };

    const handleClear = () => {
      searchForms.value.searchValue = "";
      searchForms.value.status = null;
      searchForms.value.roomId = null;
      searchForms.value.startDate = "";
      searchForms.value.endDate = "";
      searchForms.value.tenantId = null;
      searchForms.value.type = null;
      searchForms.value.landlordId = null;
      dateRange.value = [];
    };

    const submitForm = () => {
      searchForms.value.pageNo = 0;
      currentPage.value = 0;
      getListContracts(searchForms.value);
      isShowBoxSearch.value = false;
    };

    const handleLoadMore = () => {
      currentPage.value++;
      searchForms.value.pageNo++;
      getListContracts(searchForms.value);
    };

    const handleChangeDate = (date) => {
      searchForms.value.startDate = mixinMethods.showDateTime(date[0]);
      searchForms.value.endDate = mixinMethods.showDateTime(date[1]);
    };

    const handleDownloadPDF = (id) => {
      getContractPdf(id);
    };

    const handleOpenModal = (isCreate = false) => {
      if (isCreate) {
        modalTitle.value = t("contract.create.title");
        isViewDetails.value = false;
      } else {
        isViewDetails.value = true;
        modalTitle.value = t("contract.detail.title");
      }
      isShowModalSave.value = true;
    };

    const handleGetContractDetails = (id) => {
      getContractDetails(id);
      handleOpenModal();
    };

    const handleCloseModal = () => {
      dateRangeModal.value = [];
      validation.value = [];
      resetContractDetails();
      isViewDetails.value = false;
      isShowModalConfirm.value = false;
      isShowModalSave.value = false;
    };

    const handleSaveContract = () => {
      if (!isViewDetails.value) {
        contractDetails.value.deposit = mixinMethods.handleChangeNumber(
          contractDetails.value.deposit
        );
        dateRangeModal.value = [];
        createNewContract(contractDetails.value.id);
      } else {
        isViewDetails.value = false;
      }
    };

    const handleTerminateContract = () => {
      isShowModalConfirm.value = true;
    };

    const handleCloseModalConfirm = () => {
      isShowModalConfirm.value = false;
    };

    const handleConfirm = () => {
      isShowModalConfirm.value = false;
      updateContract();
    };

    return {
      searchForms,
      isShowBoxSearch,
      NUMBER_FORMAT,
      highest_role,
      ADMIN,
      LANDLORD,
      isDisabled,
      TEXT_CONFIRM_TERMINATE,
      isShowModalConfirm,
      validation,
      isViewDetails,
      totalItems,
      isShowModalSave,
      contractDetails,
      modalTitle,
      listLandlords,
      listContracts,
      listStatuses,
      listTenants,
      listFreeTenants,
      listTypes,
      dateRangeModal,
      dateRange,
      listRoomsByRole,
      handleSearchForm,
      handleClear,
      handleCloseModalConfirm,
      submitForm,
      handleLoadMore,
      handleTerminateContract,
      handleChangeDate,
      handleGetContractDetails,
      handleCloseModal,
      handleConfirm,
      handleOpenModal,
      getContractPdf,
      handleDownloadPDF,
      handleSaveContract,
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
