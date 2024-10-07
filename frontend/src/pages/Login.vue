<template>
  <div class="login">
    <div class="login-screen">
      <div class="login-item">
        <div class="login-icon">
          <span class="login-logo">
            <img src="@/assets/logo_login.png" alt="logo" />
          </span>
        </div>
        <div class="login-language">
          <button
            @click="changeLocale(JA_LOCALE)"
            :class="{
              'btn-language active': selectedLanguage.value === JA_LOCALE,
              'btn-language': selectedLanguage.value !== JA_LOCALE,
            }"
          >
            日本語
          </button>
          <button
            @click="changeLocale(EN_LOCALE)"
            :class="{
              'btn-language active': selectedLanguage.value === EN_LOCALE,
              'btn-language': selectedLanguage.value !== EN_LOCALE,
            }"
          >
            ENGLISH
          </button>
        </div>
        <div class="">
          <Form
            class="login-form"
            @submit="handleSubmit"
            :validation-schema="validate"
          >
            <div class="form-group">
              <p class="login_label">{{ $t("common.username") }}</p>
              <Field
                name="username"
                type="text"
                class="form-control login-field js--login-field--user-account"
                :placeholder="$t('common.username')"
                v-model="dataRequest.username"
                maxlength="50"
                id="login-name"
              />
              <ErrorMessage name="username" class="error-feedback" as="p" />
              <p v-if="validation.value.username" class="error-feedback">
                {{
                  $t(
                    validation.value.username.code,
                    validation.value.username.options
                  )
                }}
              </p>
            </div>
            <div class="form-group">
              <p class="login_label">{{ $t("common.password") }}</p>
              <Field
                name="password"
                type="password"
                class="form-control login-field js--login-field--password"
                :placeholder="$t('common.password')"
                v-model="dataRequest.password"
                maxlength="50"
                id="login-pass"
              />
              <ErrorMessage name="password" class="error-feedback" as="p" />
              <p v-if="validation.value.password" class="error-feedback">
                {{
                  $t(
                    validation.value.password.code,
                    validation.value.password.options
                  )
                }}
              </p>
            </div>
            <div class="form-group forgot-password">
              <a @click="openResetPassModal()" href="#"
                ><span>{{ $t("common.forgot_password") }}</span></a
              >
            </div>

            <button
              class="btn btn-primary btn-block js--btn-login"
              :disabled="disabledSubmit"
            >
              {{ $t("common.btn_login") }}
            </button>
          </Form>
        </div>
      </div>
    </div>
    <Modal
      :show="isShowModal.value"
      :width="'60%'"
      :isShowFooter="false"
      @close="handleCloseModal"
    >
      <template #body>
        <el-form :model="resetPasswordForm.value" label-width="18%">
          <el-form-item :label="$t('user.label_email')">
            <div class="input-button-group">
              <el-input
                v-model="resetPasswordForm.value.email"
                :placeholder="$t('user.placeholder_email')"
              ></el-input>
              <el-button
                :loading="loadingButton.value"
                class="btn btn-save"
                @click="getOtpCode()"
                >{{ $t("common.get_otp") }}</el-button
              >
            </div>
            <p v-if="validation.value.email" class="error-feedback">
              {{
                $t(validation.value.email.code, validation.value.email.options)
              }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('user.label_otp')">
            <el-input
              v-model="resetPasswordForm.value.otp"
              :placeholder="$t('user.placeholder_otp')"
            ></el-input>
            <p v-if="validation.value.otpCode" class="error-feedback">
              {{
                $t(
                  validation.value.otpCode.code,
                  validation.value.otpCode.options
                )
              }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('user.label_new_password')">
            <el-input
              type="password"
              v-model="resetPasswordForm.value.newPassword"
              :placeholder="$t('user.placeholder_new_password')"
            ></el-input>
            <p v-if="validation.value.newPassword" class="error-feedback">
              {{
                $t(
                  validation.value.newPassword.code,
                  validation.value.newPassword.options
                )
              }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('user.label_confirmed_password')">
            <el-input
              type="password"
              v-model="resetPasswordForm.value.confirmPassword"
              :placeholder="$t('user.placeholder_confirmed_password')"
            ></el-input>
            <p v-if="validation.value.confirmPassword" class="error-feedback">
              {{
                $t(
                  validation.value.confirmPassword.code,
                  validation.value.confirmPassword.options
                )
              }}
            </p>
          </el-form-item>

          <el-form-item class="confirm-button">
            <el-button
              :loading="loadingSaveButton.value"
              class="btn btn-save"
              @click="resetPassword"
              >{{ $t("common.save") }}</el-button
            >
          </el-form-item>
        </el-form>
      </template>
    </Modal>
  </div>
</template>

<script>
import { ref, computed, watch, onMounted, reactive } from "vue";
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";
import { JA_LOCALE, EN_LOCALE } from "@/constants/application";
import { useI18n } from "vue-i18n";
import { useAuthStore } from "@/store/auth.js";
import { $exchangeRate, $globalLocale } from "@/utils/variables";
import { i18n } from "@/utils/i18n";
import Modal from "@/components/common/Modal.vue";
import { $services } from "@/utils/variables";

export default {
  name: "Login",
  components: { Form, Field, ErrorMessage, Modal },
  setup() {
    const { t } = useI18n();
    const authStore = useAuthStore();
    const {
      loadingButton,
      loggedIn,
      loadingSaveButton,
      handleLogin,
      validation,
      getOTPCode,
      resetNewPassword,
      resetPasswordForm,
      isShowModal,
    } = authStore;
    const selectedLanguage = ref(localStorage.getItem('CurrentLanguage') || EN_LOCALE);
    const dataRequest = ref({
      username: "",
      password: "",
    });
    
    const validate = computed(() => {
      return yup.object().shape({
        username: yup
          .string()
          .trim()
          .required(t("E-CM-002", { item: t("common.username") })),
        password: yup
          .string()
          .trim()
          .required(t("E-CM-002", { item: t("common.password") })),
      });
    });

    const disabledSubmit = computed(() => {
      return (
        !dataRequest.value.username.trim() || !dataRequest.value.password.trim()
      );
    });

    onMounted(() => {
      handleCloseModal();
    });

    const changeLocale = (val) => {
      selectedLanguage.value = val;
      // Save the new language in cookies
      localStorage.setItem("CurrentLanguage", val);
      i18n.global.locale.value = val;
      $globalLocale.update(val);
      getExchangeRate(val);
    };

    const getExchangeRate = async (locale) => {
      await $services.CurrencyAPI.getExchangeRate(
        {
          language: locale,
        },
        (response) => {
          let exchangeRate = response.data.result.rate;
          localStorage.setItem("CurrentExchangeRate", exchangeRate);
          $exchangeRate.update(exchangeRate);
        },
        (error) => {}
      );
    };

    const openResetPassModal = () => {
      isShowModal.value = true;
    };

    const handleSubmit = (user) => {
      handleLogin(user);
    };

    const getOtpCode = () => {
      getOTPCode(resetPasswordForm.value.email);
    };

    const resetPassword = async () => {
      // Validate that passwords match and the form is complete
      if (
        resetPasswordForm.value.newPassword !==
        resetPasswordForm.value.confirmPassword
      ) {
        validation.value.confirmPassword = {};
        validation.value.confirmPassword.code = "E-CM-005";
        validation.value.confirmPassword.options = {};
        return;
      }
      await resetNewPassword();
    };

    const handleCloseModal = () => {
      isShowModal.value = false;
      validation.value = {};
      resetPasswordForm.value = {};
    }

    return {
      selectedLanguage,
      resetPasswordForm,
      JA_LOCALE,
      EN_LOCALE,
      dataRequest,
      validate,
      validation,
      disabledSubmit,
      changeLocale,
      handleSubmit,
      isShowModal,
      openResetPassModal,
      resetPassword,
      getOtpCode,
      loadingButton,
      loadingSaveButton,
      handleCloseModal
    };
  },
};
</script>

<style scoped lang="scss">
.js--btn-login {
  justify-content: center;
  width: 100%;
  margin: 30px 0 0 0 !important;
}

.login-logo {
  img {
    width: 60%;
  }
}

.forgot-password {
  display: flex;
  justify-content: end;
  a {
    span {
      color: #ec8448;
      font-size: 0.85em;
    }
  }
}

.confirm-button {
  margin-left: 80%;
}

.input-button-group {
  width: 100%;
  .el-input {
    width: 80%;
    margin-right: 10px; /* Space between input and button */
  }

  .el-button {
    width: 15%;
    white-space: nowrap; /* Ensure button text stays in one line */
  }
}
</style>
