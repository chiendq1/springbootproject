import { defineStore } from "pinia";
import { mixinMethods, $services } from "@/utils/variables";
import { useAuthStore } from "@/store/auth.js";
import { useI18n } from "vue-i18n";
import { reactive } from "vue";

export const useUserStore = defineStore("user", () => {
  const authStore = useAuthStore();
  const { t } = useI18n();
  const { userDetails } = authStore;
  const validation = reactive({ value: {} });
  const isPasswordModalVisible = reactive({ value: false });
  const passwordForm = reactive({
    value: {
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
    },
  });

  const getUserProfile = async () => {
    mixinMethods.startLoading();
    await $services.UserAPI.show(
      userDetails.value.id,
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

  const delay = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

  const updateUserProfile = async () => {
    mixinMethods.startLoading();
    // await delay(500); // Add a delay of 500 milliseconds

    await $services.UserAPI.update(
      userDetails.value.id,
      userDetails.value,
      async (response) => {
        userDetails.value = response.data;
        validation.value = {};
        // await delay(300); // Optional: Delay before ending loading and showing notification
        await mixinMethods.notifySuccess(t("response.message.update_success"));
        await mixinMethods.endLoading();
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

  return {
    userDetails,
    validation,
    isPasswordModalVisible,
    passwordForm,
    getUserProfile,
    updateUserProfile,
    changeUserPassword,
  };
});
