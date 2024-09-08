<template>
  <div class="container-fluid nav-bar">
    <div class="row">
      <div class="col-lg-12 nav-bar-width">
        <div class="logo_nav">
          <a href="/">
            <img src="@/assets/logo.png" class="logo_zoom_in" alt="logo" />
          </a>
        </div>
        <el-menu class="with-sidebar nav-bar-list" mode="horizontal">
          <div class="flex-grow" />
          <el-sub-menu index="1">
            <template #title>
              <img
                src="@/assets/images/avatar/avatar_001.png"
                alt="logo"
                class="avatar__img"
              />
              {{ username }}
            </template>
            <el-menu-item @click="logout" index="1-1">
              {{ $t("common.logout") }}
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu v-if="markets.length" index="2">
            <template #title>
              {{ markets.find((m) => m.market_id == market).market_name }}
            </template>
            <el-menu-item
              v-for="market in markets"
              :key="market.market_id"
              @click="changeMarket(market.market_id)"
              index="2-2"
            >
              {{ $t(`${market.market_name}`) }}
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="3">
            <template #title>
              {{ language === EN_LOCALE ? "ENGLISH" : "日本語" }}</template
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
import Cookies from "js-cookie";
import vClickOutside from "click-outside-vue3";
import { JA_LOCALE, EN_LOCALE } from "@/constants/application";

export default {
  name: "Navbar",
  directives: {
    clickOutside: vClickOutside.directive,
  },
  data() {
    return {
      JA_LOCALE,
      EN_LOCALE,
      market: this.$globalMarket.value,
      language: Cookies.get("CurrentLanguage") || EN_LOCALE,
    };
  },
  computed: {
    username() {
      return this.$store.state.auth?.user?.name || "";
    },
    markets() {
      return this.$store.state.auth?.user?.markets || [];
    },
  },
  created() {
    if (this.$store.state.auth.status.loggedIn) {
      this.getCurrentUser();
    }
  },
  methods: {
    async getCurrentUser() {
      await this.$services.authentication.current(
        (res) => {
          this.$store.dispatch("auth/initCurrentData", res);
          this.$globalMarket.update(res.markets?.[0]?.market_id);
          this.$marketList.update(res.markets);
          this.$globalCurrcency.update(
            res.markets?.[0]?.currency.currency_code
          );
        },
        () => {
          this.logout();
        }
      );
    },
    logout() {
      this.$services.authentication.logout(
        {},
        () => {
          this.$store.dispatch("auth/logout").then(() => {
            this.removeAccessToken();
            this.$router.push(this.PAGES.LOGIN);
          });
        },
        () => {}
      );
    },
    changeMarket(marketValue) {
      if (this.market != marketValue) {
        this.market = this.markets.find((m) => m.market_id == marketValue);
        let currencyCode = this.markets.find((m) => m.market_id == marketValue).currency.currency_code;
        // Set global market for another components listening to change
        // If any component need to refresh data by market, create a watcher in that component
        //   watch: {
        //     "$globalMarket.value": {
        //        handler: function () {},
        //        deep: true,
        //      },
        //   },
        // Using: this.$globalMarket.value.value to get market changed value
        this.$globalMarket.update(marketValue);
        this.$globalCurrcency.update(currencyCode);
      }
    },
    changeLocale(locale) {
      if (this.language != locale) {
        this.startLoading();
        this.language = locale;
        this.$i18n.locale = locale;
        Cookies.set("CurrentLanguage", locale, {
          expires: 365,
        });

        // Set global locale for another components listening to change
        // If any component need to refresh data by locale, create a watcher in that component
        //   watch: {
        //     "$globalLocale.value": {
        //        handler: function () {},
        //        deep: true,
        //      },
        //   },
        // Using: this.$globalLocale.value.value to get locale changed value
        this.$globalLocale.update(locale);
        this.endLoading();
      }
    },
  },
};
</script>
