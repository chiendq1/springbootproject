import { defineStore } from "pinia";
import { mixinMethods, $services } from "@/utils/variables";
import { useAuthStore } from "@/store/auth.js";
import { useI18n } from "vue-i18n";

export const useUserStore = defineStore("user", () => {
  const authStore = useAuthStore();
  const { t } = useI18n();
  const { userDetails } = authStore;

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
    await delay(500); // Add a delay of 500 milliseconds

    await $services.UserAPI.update(
      userDetails.value.id,
      userDetails.value,
      async (response) => {
        userDetails.value = response.data;
        await delay(300); // Optional: Delay before ending loading and showing notification
        await mixinMethods.notifySuccess(t("response.message.update_success"));
        await mixinMethods.endLoading();
      },
      () => {
        mixinMethods.notifyError(t("response.message.update_failed"));
        mixinMethods.endLoading();
      }
    );
  };

  return {
    userDetails,
    getUserProfile,
    updateUserProfile,
  };
});
