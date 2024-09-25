<template>
  <div class="user user-list">
    <div class="user-header">
      <h3 class="page__ttl">{{ $t("user.title") }}</h3>
      <div class="user-btn-box user-import-box">
        <el-row
          v-if="highest_role == ADMIN || highest_role == LANDLORD"
          class="mb-4"
        >
          <el-button class="btn btn-save" @click="showModalUser"
            >{{ $t("user.add_new") }}
          </el-button>
        </el-row>
      </div>
    </div>
    <div class="user-body">
      <div class="user-search">
        <div class="user-search-box col-md-9 col-lg-9">
          <p class="user-search__ttl">
            {{ $t("user.keyword") }}
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
        <div class="btn-search-select col-md-3 col-lg-3 user-box-btn-all">
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
            <el-form-item :label="$t('user.role')">
              <el-select v-model="searchForms.filterValue">
                <el-option :label="$t('common.all')" value=""></el-option>
                <el-option
                  v-for="(role, index) in listRoles"
                  :key="index"
                  :label="role.key"
                  :value="role.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
      </div>
    </div>

    <div class="bidding-body-table" style="margin-top: 16px">
      <UsersTable
        :listUsers="listUsers.value"
        :userRole="highest_role"
        @details="getUserDetails"
        @delete="handleOpenModalConfirm"
      />
      <LoadMore :listData="listUsers.value" :totalItems="totalItems.value" @loadMore="handleLoadMore" />
    </div>
    <UserModal
      v-if="isShowUserModal.value"
      :userInfor="userDetails.value"
      :show="isShowUserModal.value"
      :validate="validation"
      @close="handleCloseModal"
      @submit="handleSaveUser"
    />
    <ModalConfirm
      :isShowModal="showModalConfirm.value"
      @close-modal="handleCloseModalConfirm"
      :isConfirmByText="true"
      :confirmText="TEXT_CONFIRM_DELETE"
      @confirmAction="handleConfirm"
      :message="$t('user.modal_confirm.message_delete')"
      :title="$t('user.modal_confirm.title_delete')"
    />
  </div>
</template>

<script>
import IconSetting from "@/svg/IconSettingMain.vue";
import IconCircleClose from "@/svg/IconCircleClose.vue";
import { NUMBER_FORMAT } from "@/constants/application.js";
import UsersTable from "./item/ListUserTable.vue";
import SingleOptionSelect from "@/components/common/SingleOptionSelect.vue";
import { onMounted, ref } from "vue";
import { ADMIN, LANDLORD, TENANT } from "@/constants/roles.js";
import Cookies from "js-cookie";
import { useI18n } from "vue-i18n";
import { useUserStore } from "@/store/users.js";
import UserModal from "./item/UserModal.vue";
import ModalConfirm from "@/components/common/ModalConfirm.vue";
import LoadMore from "@/components/common/LoadMore.vue";
import { TEXT_CONFIRM_DELETE } from "@/constants/application.js";

export default {
  name: "UserList",
  components: {
    IconSetting,
    IconCircleClose,
    SingleOptionSelect,
    UsersTable,
    UserModal,
    ModalConfirm,
    LoadMore,
  },
  setup() {
    const userStore = useUserStore();
    const {
      listUsers,
      isShowUserModal,
      validation,
      getListUsers,
      userDetails,
      totalItems,
      currentPage,
      getUserProfile,
      clearUserDetailsAttr,
      createNewUser,
      updateUserProfile,
      deleteUser,
      showModalConfirm,
    } = userStore;
    const searchForms = ref({
      searchValue: "",
      filterValue: "",
      pageNo: 0,
    });
    const isShowBoxSearch = ref(false);
    const isUpdate = ref(false);
    const { t } = useI18n();
    const updateId = ref(0);
    const highest_role = Cookies.get("highest_role");
    const listRoles = ref([
      {
        key: t("role.landlord"),
        value: LANDLORD,
      },
      {
        key: t("role.tenant"),
        value: TENANT,
      },
    ]);

    onMounted(() => {
      listUsers.value = [];
      getListUsers(searchForms.value);
      clearUserDetailsAttr();
    });

    const handleSearchForm = () => {
      isShowBoxSearch.value = !isShowBoxSearch.value;
    };

    const handleClear = () => {
      searchForms.value.filterValue = "";
      searchForms.value.searchValue = "";
    };

    const submitForm = () => {
      isShowBoxSearch.value = false;
      getListUsers(searchForms.value);
    };

    const showModalUser = () => {
      isShowUserModal.value = true;
    };

    const handleCloseModal = () => {
      clearUserDetailsAttr();
      isUpdate.value = false;
      validation.value = {};
      isShowUserModal.value = false;
    };

    const getUserDetails = async (userId) => {
      isUpdate.value = true;
      updateId.value = userId;
      await getUserProfile(userId);
      showModalUser();
    };

    const handleOpenModalConfirm = (userId) => {
      updateId.value = userId;
      showModalConfirm.value = true;
    };

    const handleCloseModalConfirm = () => {
      showModalConfirm.value = false;
    };

    const handleConfirm = () => {
      deleteUser(updateId.value);
    };

    const handleSaveUser = () => {
      if (!isUpdate.value) {
        createNewUser();
      } else {
        updateUserProfile(updateId.value);
      }
    };

    const handleLoadMore = () => {
      currentPage.value++;
      searchForms.value.pageNo++;
      getListUsers(searchForms.value);
    };

    return {
      searchForms,
      isShowBoxSearch,
      NUMBER_FORMAT,
      highest_role,
      ADMIN,
      listRoles,
      listUsers,
      isShowUserModal,
      userDetails,
      TEXT_CONFIRM_DELETE,
      validation,
      totalItems,
      showModalConfirm,
      handleCloseModal,
      getUserDetails,
      handleSearchForm,
      handleClear,
      submitForm,
      showModalUser,
      handleCloseModalConfirm,
      handleOpenModalConfirm,
      handleSaveUser,
      handleConfirm,
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
