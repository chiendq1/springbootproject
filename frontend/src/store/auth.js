import { defineStore } from "pinia";
import { reactive, ref } from "vue";
import Cookies from "js-cookie";
import { mixinMethods, $services } from "@/utils/variables";
import { COOKIE_EXPIRE_TIME } from "@/constants/application";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";
import { ADMIN } from "@/constants/roles.js";
import PAGE_NAME from "@/constants/route-name.js";

export const useAuthStore = defineStore(
  "auth",
  () => {
    const { t } = useI18n();
    const loggedIn = reactive({ value: false });
    const isShowModal = reactive({ value: false });
    const router = useRouter();
    const resetPasswordForm = reactive({
      value: {
        email: "",
        otp: "",
        newPassword: "",
        confirmPassword: "",
      },
    });
    const loadingButton = reactive({ value: false });
    const loadingSaveButton = reactive({ value: false });
    const validation = reactive({ value: {} });

    const handleLogin = async (params) => {
      mixinMethods.startLoading();
      await $services.AuthenticationAPI.login(
        params,
        (response) => {
          if (response.data) {
            const access_token = response.data.token;
            const refresh_token = response.data.refreshToken;
            const highestRole = response.data.user.highestRole;
            const userId = response.data.user.id;
            const userName = response.data.user.username;
            Cookies.set("user_id", userId, {
              expires: parseInt(COOKIE_EXPIRE_TIME),
            });
            Cookies.set("username", userName, {
              expires: parseInt(COOKIE_EXPIRE_TIME),
            });
            Cookies.set("highest_role", highestRole, {
              expires: parseInt(COOKIE_EXPIRE_TIME),
            });
            Cookies.set("access_token", access_token, {
              expires: parseInt(COOKIE_EXPIRE_TIME),
            });
            Cookies.set("refresh_token", refresh_token, {
              expires: parseInt(COOKIE_EXPIRE_TIME),
            });
            if(highestRole !== ADMIN) {
              router.push({name: PAGE_NAME.ROOM.LIST})
            } else {
              router.push({name: PAGE_NAME.USER.LIST})
            }
            loggedIn.value = true;
            validation.value = {};
          }

          mixinMethods.endLoading();
        },
        (error) => {
          validation.value = mixinMethods.handleErrorResponse(
            error.responseCode
          );
          mixinMethods.notifyError(t("response.message.login_fail"));
          mixinMethods.endLoading();
        }
      );
    };

    const handleLogout = () => {
      Cookies.remove("access_token");
      Cookies.remove("highest_role");
      loggedIn.value = false;
    };

    const getOTPCode = async (sentEmail) => {
      loadingButton.value = true;
      await $services.AuthenticationAPI.getOTP(
        {
          email: sentEmail,
          type: "reset_password_email",
          subject: "Reset Password OTP",
        },
        (response) => {
          if (response.success) {
            mixinMethods.notifySuccess(t("response.message.get_otp_success"));
          }
          validation.value = {};
          loadingButton.value = false;
        },
        (error) => {
          validation.value = mixinMethods.handleErrorResponse(
            error.responseCode
          );
          mixinMethods.notifyError(t("response.message.get_otp_failed"));
          loadingButton.value = false;
        }
      );
    };

    const resetNewPassword = async () => {
      loadingSaveButton.value = true;
      await $services.AuthenticationAPI.resetPassword(
        {
          email: resetPasswordForm.value.email,
          otpCode: resetPasswordForm.value.otp,
          newPassword: resetPasswordForm.value.confirmPassword,
        },
        (response) => {
          if (response.success) {
            mixinMethods.notifySuccess(
              t("response.message.reset_password_success")
            );
          }
          validation.value = {};
          isShowModal.value = false;
          loadingSaveButton.value = false;
        },
        (error) => {
          validation.value = mixinMethods.handleErrorResponse(
            error.responseCode
          );
          loadingSaveButton.value = false;
          mixinMethods.notifyError(t("response.message.reset_password_failed"));
        }
      );
    };

    return {
      loggedIn,
      validation,
      loadingButton,
      resetPasswordForm,
      loadingSaveButton,
      isShowModal,
      getOTPCode,
      handleLogin,
      handleLogout,
      resetNewPassword,
    };
  },
  {persist: true}
);
