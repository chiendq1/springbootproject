<template>
  <div class="utility utility-list">
    <div class="utility-header">
      <h3 class="page__ttl">{{ $t("utility.title") }}</h3>
      <div class="utility-btn-box utility-import-box">
        <el-row
          v-if="highest_role == ADMIN"
          class="mb-4"
        >
          <el-button class="btn btn-save" @click="handleAddNewUtility"
            >{{ $t("utility.add_new") }}
          </el-button>
        </el-row>
      </div>
    </div>
    <div class="utility-body">
      <div class="utility-search">
        <div class="utility-search-box col-md-9 col-lg-9">
          <p class="utility-search__ttl">
            {{ $t("utility.keyword") }}
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
        <div class="btn-search-select col-md-3 col-lg-3 utility-box-btn-all">
          <el-button class="btn btn-search" @click="submitForm()">
            {{ $t("common.search") }}</el-button
          >
          <el-button class="btn btn-clear" @click="handleClear()">
            {{ $t("common.clear") }}</el-button
          >
        </div>
        <div class="form-search" :class="{ active: isShowBoxSearch }">
        <div class="close-form">
          <IconCircleClose @click="isShowBoxSearch = false" />
        </div>
        <div class="form-search-box">
          <div class="item">
            <el-form-item :label="$t('utility.status_label')">
              <el-select v-model="searchForms.status">
                <el-option :label="$t('common.all')" value=""></el-option>
                <el-option
                  v-for="(util, index) in listStatuses"
                  :key="index"
                  :label="util.key"
                  :value="util.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
      </div>
      </div>
    </div>

    <div class="bidding-body-table" style="margin-top: 16px">
      <UtilitiesTable
        :listUtilities="listUtilities.value"
        :userRole="highest_role"
        @details="handleGetUtilityDetails"
        @delete="handleOpenModalConfirm"
      />
    </div>
    <UtilityModal
      v-if="isShowUtilityModal.value"
      :utilityDetails="utilityDetails.value"
      :show="isShowUtilityModal.value"
      :validate="validation"
      @close="handleCloseModal"
      @submit="handleSaveUtility"
    />
    <ModalConfirm
      :isShowModal="showModalConfirm.value"
      @close-modal="handleCloseModalConfirm"
      :isConfirmByText="true"
      :confirmText="TEXT_CONFIRM_INACTIVE"
      @confirmAction="handleConfirm"
      :message="$t('utility.modal_confirm.message_delete')"
      :title="$t('utility.modal_confirm.title_delete')"
    />
  </div>
</template>

<script>
import IconSetting from "@/svg/IconSettingMain.vue";
import IconCircleClose from "@/svg/IconCircleClose.vue";
import Cookies from "js-cookie";
import UtilitiesTable from "./item/UtilitiesTable.vue";
import SingleOptionSelect from "@/components/common/SingleOptionSelect.vue";
import UtilityModal from "./item/UtilityModal.vue";
import ModalConfirm from "@/components/common/ModalConfirm.vue";
import { useI18n } from "vue-i18n";
import { useUtilityStore } from "@/store/utility.js";
import { NUMBER_FORMAT } from "@/constants/application.js";
import { onMounted, ref } from "vue";
import { ADMIN } from "@/constants/roles.js";
import { INACTIVE_STATUS, ACTIVE_STATUS } from "@/constants/utility.js";
import { TEXT_CONFIRM_INACTIVE } from "@/constants/application.js";

export default {
  name: "UserList",
  components: {
    IconSetting,
    IconCircleClose,
    SingleOptionSelect,
    UtilitiesTable,
    UtilityModal,
    ModalConfirm,
  },
  setup() {
    const utilityStore = useUtilityStore();
    const isShowBoxSearch = ref(false);
    const { t } = useI18n();
    const {
      listUtilities,
      isShowUtilityModal,
      utilityDetails,
      validation,
      showModalConfirm,
      getListAllUtilities,
      getUtilityDetails,
      updateUtility,
      deactivateUtility,
      clearUtilityAttrs,
      createNewUtility,
    } = utilityStore;
    const searchForms = ref({
      searchValue: "",
      status: "",
    });
    const listStatuses = ref([
      {
        key: t("utility.status.active"),
        value: ACTIVE_STATUS,
      },
      {
        key: t("utility.status.inactive"),
        value: INACTIVE_STATUS,
      },
    ]);
    const isUpdate = ref(false);
    const updateId = ref(0);
    const highest_role = Cookies.get("highest_role");

    onMounted(() => {
      listUtilities.value = [];
      getListAllUtilities(searchForms.value, true);
    });

    const handleSearchForm = () => {
      isShowBoxSearch.value = !isShowBoxSearch.value;
    };

    const handleClear = () => {
      searchForms.value.searchValue = "";
      searchForms.value.status = "";
    };

    const submitForm = () => {
      isShowBoxSearch.value = false;
      getListAllUtilities(searchForms.value, true);
    };

    const handleAddNewUtility = () => {
      isShowUtilityModal.value = true;
    }

    const handleCloseModal = () => {
      clearUtilityAttrs()
      isUpdate.value = false;
      validation.value = {};
      isShowUtilityModal.value = false;
    };

    const handleGetUtilityDetails = async (utilityId) => {
      await getUtilityDetails(utilityId);
      isShowUtilityModal.value = true;
      updateId.value = utilityId;
      isUpdate.value = true;
    };

    const handleOpenModalConfirm = (utilityId) => {
      updateId.value = utilityId;
      showModalConfirm.value = true;
    };

    const handleCloseModalConfirm = () => {
      showModalConfirm.value = false;
    };

    const handleConfirm = () => {
      deactivateUtility(updateId.value);
    };

    const handleSaveUtility = () => {
      if (!isUpdate.value) {
        createNewUtility();
      } else {
        updateUtility(updateId.value);
      }
    };

    return {
      searchForms,
      NUMBER_FORMAT,
      highest_role,
      ADMIN,
      showModalConfirm,
      utilityDetails,
      listUtilities,
      isShowUtilityModal,
      isShowBoxSearch,
      listStatuses,
      TEXT_CONFIRM_INACTIVE,
      validation,
      showModalConfirm,
      handleCloseModal,
      handleSearchForm,
      handleGetUtilityDetails,
      handleClear,
      submitForm,
      handleAddNewUtility,
      handleCloseModalConfirm,
      handleOpenModalConfirm,
      handleSaveUtility,
      handleConfirm,
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
