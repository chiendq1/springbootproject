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
            </div>

            <p
              class="error-feedback"
              v-text="errors_message.login && $t(errors_message.login)"
            ></p>

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
  </div>
</template>

<script>
import { ref, computed, watch, onMounted } from "vue";
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";
import Cookies from "js-cookie";
import { JA_LOCALE, EN_LOCALE } from "@/constants/application";
import { useI18n } from "vue-i18n";
import { useAuthStore } from "@/store/auth.js";
import { useRouter } from "vue-router";
import { $PAGES, $globalLocale } from "@/utils/variables";
import { i18n } from "@/utils/i18n";
import { COOKIE_EXPIRE_TIME } from "@/constants/application";

export default {
  name: "Login",
  components: { Form, Field, ErrorMessage },
  setup() {
    const { t } = useI18n();
    const router = useRouter();
    const authStore = useAuthStore();
    const { loggedIn, handleLogin } = authStore;
    const selectedLanguage = ref(Cookies.get("CurrentLanguage") || EN_LOCALE);
    const showPasscode = ref(false);
    const errors_message = ref({
      login: "",
    });
    const dataRequest = ref({
      username: "",
      password: "",
    });
    const validate = computed(() => {
      return yup.object().shape({
        username: yup
          .string()
          .trim()
          .required(t("E-CM-100", { item: t("common.username") })),
        password: yup
          .string()
          .trim()
          .required(t("E-CM-100", { item: t("common.password") })),
      });
    });

    const disabledSubmit = computed(() => {
      return (
        !dataRequest.value.username.trim() || !dataRequest.value.password.trim()
      );
    });

    onMounted(() => {
      if (loggedIn.value) router.push($PAGES.HOME);
    });

    watch(() => authStore.loggedIn, (newVal) => {
      if (newVal) {
        router.push($PAGES.HOME); // Redirect to home if logged in
      }
    });

    const changeLocale = (val) => {
      selectedLanguage.value = val;
      // Save the new language in cookies
      Cookies.set("CurrentLanguage", val, {
        expires: parseInt(COOKIE_EXPIRE_TIME),
      });
      i18n.global.locale.value = val;
      $globalLocale.update(val);
    };

    const handleSubmit = (user) => {
      errors_message.value.login = "";
      handleLogin(user);
    };

    return {
      selectedLanguage,
      JA_LOCALE,
      EN_LOCALE,
      showPasscode,
      errors_message,
      dataRequest,
      validate,
      disabledSubmit,
      changeLocale,
      handleSubmit,
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
</style>
