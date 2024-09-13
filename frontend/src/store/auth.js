import { defineStore } from "pinia";
import { reactive } from "vue";
import Cookies from "js-cookie";
import { mixinMethods, $services } from "@/utils/variables";
import { COOKIE_EXPIRE_TIME } from "@/constants/application";
import { useI18n } from "vue-i18n";

export const useAuthStore = defineStore(
  "auth",
  () => {
    const { t } = useI18n();
    const loggedIn = reactive({ value: false });
    const userDetails = reactive({
      value: {
        id: "",
        username: "",
        email: "",
        fullName: "",
        phoneNumber: "",
        location: "",
        roles: [],
      },
    });

    const handleLogin = async (params) => {
      mixinMethods.startLoading();
      await $services.AuthenticationAPI.login(
        params,
        (response) => {
          if(response.data) {
            const access_token = response.data.token;
            const refresh_token = response.data.refreshToken;
            userDetails.value = response.data.user;
            Cookies.set("access_token", access_token, {
              expires: parseInt(COOKIE_EXPIRE_TIME),
            });
            Cookies.set("refresh_token", refresh_token, {
              expires: parseInt(COOKIE_EXPIRE_TIME),
            });
            loggedIn.value = true;
          }

          mixinMethods.endLoading();
        },
        () => {
          mixinMethods.notifyError(t("response.message.login_fail"));

          mixinMethods.endLoading();
        }
      );
    };

    const handleLogout = () => {
      Cookies.remove("access_token");
      loggedIn.value = false;
    };

    return {
      loggedIn,
      userDetails,
      handleLogin,
      handleLogout,
    };
  },
  { persist: true }
);
