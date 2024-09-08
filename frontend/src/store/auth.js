import { defineStore } from "pinia";
import { reactive, ref } from "vue";
import Cookies from "js-cookie";
import { mixinMethods, $services, $PAGES } from "@/utils/variables";
import { COOKIE_EXPIRE_TIME } from "@/constants/application";
import { useI18n } from "vue-i18n";

export const useAuthStore = defineStore("auth", () => {
  const { t } = useI18n();
  const loggedIn = ref(false);

  const handleLogin = async (params) => {
    mixinMethods.startLoading();
    await $services.AuthenticationAPI.login(
      params,
      (response) => {
        const access_token = response.data;
        Cookies.set("access_token", access_token, { expires: parseInt(COOKIE_EXPIRE_TIME) });
        loggedIn.value = true;

        mixinMethods.endLoading();
      },
      (error) => {
        mixinMethods.notifyError(t("E-CM-001"));

        mixinMethods.endLoading();
      }
    );
  };

  const handleLogout = async => {
    Cookies.remove('access_token');
    loggedIn.value = false;

    router.push($PAGES.HOME);
  }

  return {
    loggedIn,
    handleLogin,
    handleLogout,
  };
});
