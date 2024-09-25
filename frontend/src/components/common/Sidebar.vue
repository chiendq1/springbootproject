<template>
  <div class="siderbar-block" v-if="isShowComponent">
    <div class="sidebar-logo">
      <a href="/"
        ><img
          src="@/assets/images/vmo_logo.svg"
          class="logo_zoom_in"
          alt="logo"
      /></a>
      <a href="/">
        <img
          src="@/assets/images/vmo_logo.svg"
          class="logo_zoom_out"
          alt="logo"
        />
      </a>
    </div>
    <div class="menu-box">
      <ul class="nav" v-if="listRouter.length">
        <li
          v-for="(route, index) in listRouter"
          :key="index"
          class="nav-list"
          style="border-bottom: 1px solid #d0d6da"
        >
          <div>
            <a
              href="#"
              @click.prevent="navigateChangeRoute(route)"
              :class="classActive(route)"
              class="js-sidenav-tooltip"
              style="display: flex; align-items: center"
            >
              <component
                :is="route.function_icon"
                class="icon_sidebar"
              ></component>
              <span class="sidebar-text sidebar-custom siderbar_name">{{
                route.function_name
              }}</span>
            </a>
          </div>
        </li>
      </ul>
      <p class="nav_others" v-if="listRouter.length">{{ $t("common.other") }}</p>
      <ul class="nav">
        <li
          v-for="(route, index) in listRouterOthers"
          :key="index"
          style="border-bottom: 1px solid #d0d6da"
        >
          <a
            href="#"
            @click.prevent="navigateChangeRoute(route)"
            :class="classActive(route)"
            class="js-sidenav-tooltip"
            style="display: flex; align-items: center"
          >
            <component
              :is="route.function_icon"
              class="icon_sidebar"
            ></component>
            <span class="sidebar-text sidebar-custom siderbar_name">{{
              route.function_name
            }}</span>
          </a>
        </li>
      </ul>
    </div>
    <p class="vmo-name">{{ $t("common.text_vmo") }}</p>
  </div>
</template>

<script>
import { ref } from "vue";
import IconCart from "@/svg/IconCart.vue";
import IconUser from "@/svg/IconUser.vue";
import IconUserGroup from "@/svg/IconUserGroup.vue";
import IconRoom from "@/svg/IconRoom.vue";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";
import PAGE_NAME from "@/constants/route-name.js";

export default {
  name: "Sidebar",
  components: {
    IconCart,
    IconRoom,
    IconUser,
    IconUserGroup,
  },
  setup() {
    const { t } = useI18n();
    const router = useRouter();
    const isShowComponent = ref(true);
    const menuOpen = ref("");
    const listRouter = ref([
      {
        function_name: t("side_bar.label.user"),
        function_page_name: PAGE_NAME.USER.LIST,
        function_icon: "IconUserGroup",
      },
      {
        function_name: t("side_bar.label.room"),
        function_page_name: PAGE_NAME.ROOM.LIST,
        function_icon: "IconRoom",
      },
    ]);
    const listRouterOthers = ref([
      {
        function_name: t("side_bar.label.profile"),
        function_page_name: PAGE_NAME.PROFILE,
        function_icon: "IconUser",
      },
    ]);
    const currentPath = ref("");

    const navigateChangeRoute = async (route) => {
      currentPath.value = route.function_page_name;
      await router.push({name: route.function_page_name});
    };

    const classActive = (route) => {
      return currentPath.value.includes(route.function_page_name) ? "active" : "";
    };

    return {
      isShowComponent,
      menuOpen,
      listRouter,
      currentPath,
      listRouterOthers,
      navigateChangeRoute,
      classActive,
    };
  },
};
</script>

<style scoped lang="scss">
@import url("https://fonts.googleapis.com/css2?family=Poppins&display=swap");
.nav_others {
  font-size: 12px;
  color: #7a8998;
  padding-left: 70px;
  font-family: "Poppins", sans-serif;
  margin: 23px 0 3px;
}

.active-expander {
  background-color: #dfe3fc;
  border-radius: 4px;

  span {
    color: #5a6acf !important;
  }
}

.menu-box {
  max-height: 90vh;
  overflow-y: auto;
  flex-grow: 1;
}

.vmo-name {
  color: #a6abc8;
  font-size: 14px;
  text-align: center;
}
</style>
