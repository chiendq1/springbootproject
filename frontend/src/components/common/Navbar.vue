<template>
  <div class="container-fluid nav-bar">
    <div class="row">
      <div class="col-lg-12 nav-bar-width">
        <div class="logo_nav">
          <a href="/">
            <img
              src="@/assets/logo_login.png"
              class="logo_zoom_in"
              alt="logo"
            />
          </a>
        </div>
        <el-menu class="with-sidebar nav-bar-list" mode="horizontal">
          <el-sub-menu index="1" mode="vertical">
            <template #title>
              <img
                src="@/assets/images/avatar/avatar_001.png"
                alt="logo"
                class="avatar__img"
              />
              {{ username }}
            </template>
            <el-menu-item @click="logout()" index="1-1">
              {{ $t("common.logout") }}
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              {{
                selectedLanguage === EN_LOCALE ? "ENGLISH" : "日本語"
              }}</template
            >
            <el-menu-item @click="changeLocale(EN_LOCALE)" index="3-1">ENGLISH</el-menu-item>
            <el-menu-item @click="changeLocale(JA_LOCALE)" index="3-2">日本語</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import Cookies from "js-cookie";
import vClickOutside from "click-outside-vue3";
import { JA_LOCALE, EN_LOCALE } from "@/constants/application";
import { useAuthStore } from "@/store/auth.js";
import { useRouter } from "vue-router";
import { i18n } from "@/utils/i18n";
import PAGE_NAME from "@/constants/route-name.js";
import { $services } from "@/utils/variables";
import { $exchangeRate, $globalLocale } from "../../utils/variables";

export default {
  name: "Navbar",
  directives: {
    clickOutside: vClickOutside.directive,
  },
  setup() {
    const selectedLanguage = ref(localStorage.getItem('CurrentLanguage') || EN_LOCALE);
    const authStore = useAuthStore();
    const { handleLogout } = authStore;
    const router = useRouter();
    const username = ref(Cookies.get("username"));
    const role = ref(Cookies.get("highest_role"));

    const changeLocale = (val) => {
      selectedLanguage.value = val;
      localStorage.setItem('CurrentLanguage', val);
      $globalLocale.update(val);
      i18n.global.locale.value = val; 
      getExchangeRate(val);
    };

    const getExchangeRate = async (locale) => {
      await $services.CurrencyAPI.getExchangeRate(
        {
          language: locale,
        },
        (response) => {
          location.reload();
          let exchangeRate = response.data.result.rate;
          localStorage.setItem('CurrentExchangeRate', exchangeRate);
          $exchangeRate.update(exchangeRate);
        },
        (error) => {}
      );
    };

    const logout = async () => {
      await handleLogout();
      router.push({ name: PAGE_NAME.LOGIN });
    };

    onMounted(() => {
      const storedLanguage = localStorage.getItem("CurrentLanguage");
      if (storedLanguage) {
        selectedLanguage.value = storedLanguage;
        $globalLocale.update(storedLanguage);
        i18n.global.locale.value = storedLanguage;
      }
    });

    return {
      selectedLanguage,
      JA_LOCALE,
      EN_LOCALE,
      username,
      role,
      $globalLocale,
      $exchangeRate,
      changeLocale,
      logout,
    };
  },
};
</script>
