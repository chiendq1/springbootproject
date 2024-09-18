<template>
  <div class="container-fluid nav-bar">
    <div class="row">
      <div class="col-lg-12 nav-bar-width">
        <div class="logo_nav">
          <a href="/">
            <img src="@/assets/logo_login.png" class="logo_zoom_in" alt="logo" />
          </a>
        </div>
        <el-menu class="with-sidebar nav-bar-list" mode="horizontal">
          <el-sub-menu index="1" mode="verticle">
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
              {{ selectedLanguage === EN_LOCALE ? "ENGLISH" : "日本語" }}</template
            >
            <el-menu-item @click="changeLocale(EN_LOCALE)" index="3-1"
              >ENGLISH
            </el-menu-item>
            <el-menu-item @click="changeLocale(JA_LOCALE)" index="3-2"
              >日本語
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import Cookies from "js-cookie";
import vClickOutside from "click-outside-vue3";
import { JA_LOCALE, EN_LOCALE } from "@/constants/application";
import { COOKIE_EXPIRE_TIME } from "@/constants/application";
import { useAuthStore } from "@/store/auth.js";
import { useRouter } from "vue-router";
import { $PAGES } from "@/utils/variables";
import { i18n } from "@/utils/i18n";

export default {
  name: "Navbar",
  directives: {
    clickOutside: vClickOutside.directive,
  },
  setup() {
    const selectedLanguage = ref(Cookies.get("CurrentLanguage") || EN_LOCALE);
    const authStore = useAuthStore();
    const { handleLogout } = authStore;
    const router = useRouter();
    const username = ref(Cookies.get("username"));
    const role = ref(Cookies.get("highest_role"));

    const changeLocale = (val) => {
      selectedLanguage.value = val;
      Cookies.set("CurrentLanguage", val, {
        expires: parseInt(COOKIE_EXPIRE_TIME),
      });
      i18n.global.locale.value = val;
      $globalLocale.update(val);
    };

    const logout = async () => {
      await handleLogout();

      router.push($PAGES.LOGIN);
    }

    return {
      selectedLanguage,
      JA_LOCALE,
      EN_LOCALE,
      username,
      role,
      changeLocale,
      logout,
    };
  },
};
</script>
