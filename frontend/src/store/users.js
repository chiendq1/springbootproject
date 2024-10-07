import { defineStore } from "pinia";
import { mixinMethods, $services } from "@/utils/variables";
import { useI18n } from "vue-i18n";
import { reactive } from "vue";
import { LANDLORD, TENANT } from "@/constants/roles.js";

export const useUserStore = defineStore("user", () => {
  const { t } = useI18n();
  const validation = reactive({ value: {} });
  const listUsers = reactive({ value: [] });
  const listLandlords = reactive({ value: [] });
  const listTenants = reactive({ value: [] });
  const listFreeTenants = reactive({ value: [] });
  const totalItems = reactive({ value: 0 });
  const currentPage = reactive({ value: 0 });
  const showModalConfirm = reactive({ value: false });
  const isShowUserModal = reactive({ value: false });
  const isPasswordModalVisible = reactive({ value: false });
  const userDetails = reactive({
    value: {
      id: "",
      username: "",
      email: "",
      fullName: "",
      phoneNumber: "",
      location: "",
      highestRole: "",
    },
  });
  const passwordForm = reactive({
    value: {
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
    },
  });

  const getListUsers = async (data) => {
    mixinMethods.startLoading();
    await $services.UserAPI.index(
      data,
      (response) => {
        if (currentPage.value === 0) {
          listUsers.value = response.data.users;
        } else {
          listUsers.value = [...listUsers.value, ...response.data.users];
        }

        totalItems.value = response.data.totalItems;
        currentPage.value = response.data.currentPage;

        mixinMethods.endLoading();
      },
      () => {
        mixinMethods.endLoading();
      }
    );
  };

  const getListUsersByRole = async () => {
    await $services.UserAPI.getListUsersByRole(
      {},
      (response) => {
        if (response.data.users) {
          response.data.users.map((user) => {
            if (user.highestRole === LANDLORD) {
              listLandlords.value.push({
                id: user.id,
                value: user.username,
              });
            }

            if (user.highestRole === TENANT) {
              listTenants.value.push({
                id: user.id,
                fullName: user.fullName,
                phoneNumber: user.phoneNumber,
                email: user.email,
                value: user.username,
              });
            }
          });
        }
      },
      (error) => {
        console.log(error);
      }
    );
  };

  const getListFreeTenants = async () => {
    await $services.UserAPI.getListFreeTenants(
      {},
      (response) => {
        if (response.data.users) {
          listFreeTenants.value = response.data.users.map((user) => {
            return {
              id: user.id,
              fullName: user.fullName,
              phoneNumber: user.phoneNumber,
              email: user.email,
              value: user.username,
            };
          });
        }
      },
      (error) => {
        console.log(error);
      }
    );
  };

  const getUserProfile = async (userId) => {
    mixinMethods.startLoading();
    await $services.UserAPI.show(
      userId,
      {},
      (response) => {
        userDetails.value = response.data;
        mixinMethods.endLoading();
      },
      () => {
        mixinMethods.endLoading();
      }
    );
  };

  const updateUserProfile = async (userId) => {
    mixinMethods.startLoading();

    await $services.UserAPI.update(
      userId,
      userDetails.value,
      (response) => {
        userDetails.value = response.data;
        validation.value = {};
        isShowUserModal.value = false;
        if (listUsers.value && listUsers.value.length > 0) {
          listUsers.value.forEach((user, index) => {
            if (user.id == userId) {
              listUsers.value[index] = userDetails.value;
            }
          });
        }
        mixinMethods.notifySuccess(t("response.message.update_success"));
        mixinMethods.endLoading();
      },
      (error) => {
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.notifyError(t("response.message.update_failed"));
        mixinMethods.endLoading();
      }
    );
  };

  const changeUserPassword = async () => {
    mixinMethods.startLoading();
    await $services.UserAPI.changePassword(
      userDetails.value.id,
      passwordForm.value,
      async (response) => {
        validation.value = {};
        passwordForm.value = {};
        isPasswordModalVisible.value = false;
        await mixinMethods.notifySuccess(
          t("response.message.change_password_success")
        );
        await mixinMethods.endLoading();
      },
      (error) => {
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.notifyError(t("response.message.change_password_failed"));
        mixinMethods.endLoading();
      }
    );
  };

  const clearUserDetailsAttr = () => {
    userDetails.value.id = "";
    userDetails.value.username = "";
    userDetails.value.email = "";
    userDetails.value.fullName = "";
    userDetails.value.phoneNumber = "";
    userDetails.value.location = "";
    userDetails.value.highestRole = "";
  };

  const createNewUser = async () => {
    mixinMethods.startLoading();

    await $services.UserAPI.store(
      userDetails.value,
      (response) => {
        listUsers.value.push(response.data);
        clearUserDetailsAttr();
        isShowUserModal.value = false;
        mixinMethods.notifySuccess(t("response.message.create_user_success"));
        mixinMethods.endLoading();
      },
      (error) => {
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.notifyError(t("response.message.create_user_failed"));
        mixinMethods.endLoading();
      }
    );
  };

  const deleteUser = async (userId) => {
    mixinMethods.startLoading();
    await $services.UserAPI.remove(
      userId,
      {},
      (response) => {
        validation.value = {};
        showModalConfirm.value = false;
        listUsers.value = listUsers.value.filter((user) => user.id !== userId);
        mixinMethods.notifySuccess(t("response.message.delete_user_success"));
        mixinMethods.endLoading();
      },
      (error) => {
        showModalConfirm.value = false;
        mixinMethods.notifyError(t("response.message.delete_user_failed"));
        mixinMethods.endLoading();
      }
    );
  };

  return {
    userDetails,
    validation,
    showModalConfirm,
    passwordForm,
    listUsers,
    isPasswordModalVisible,
    isShowUserModal,
    totalItems,
    currentPage,
    listLandlords,
    listTenants,
    listFreeTenants,
    deleteUser,
    createNewUser,
    clearUserDetailsAttr,
    getListUsers,
    getUserProfile,
    updateUserProfile,
    getListUsersByRole,
    getListFreeTenants,
    changeUserPassword,
  };
});
