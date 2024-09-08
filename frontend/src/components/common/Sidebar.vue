<template>
  <div class="siderbar-block" v-if="isShowComponent">
    <div class="sidebar-logo">
      <a href="/">
        <img src="@/assets/logo.png" class="logo_zoom_in" alt="logo" />
      </a>
      <a href="/">
        <img
          src="@/assets/img_logo_zoom_in.png"
          class="logo_zoom_out"
          alt="logo"
        />
      </a>
    </div>
    <div class="menu-box">
      <ul class="nav" v-if="listRouter.length">
        <li
          style="border-bottom: 1px solid #d0d6da"
          v-for="(route, index) in listRouter"
          :key="index"
          class="nav-list"
        >
          <div
            v-if="
              route.functions &&
              route.functions.length > 0 &&
              route.function_group_code != 'DASHBOARD'
            "
          >
            <span
              class="expander-trigger cursor-pointer"
              @click="menuOpen = menuOpen == index ? '' : index"
              style="display: flex; align-items: center"
            >
              <component
                class="icon_sidebar"
                :is="`${route.function_group_icon}`"
              ></component>
              <span
                class="sidebar-text sidebar-custom siderbar_name"
                :class="classActive(route)"
              >
                {{ $t(route.function_group_name) }}
                <span
                  class="expander expander-trigger expander-children p-0"
                  :class="menuOpen == index && 'open'"
                  style="top: 12px; transform: translateY(-50%)"
                  @click="menuOpen = menuOpen == index ? '' : index"
                >
                  <span
                    class="caret"
                    style="
                      left: 50%;
                      top: 50%;
                      transform: translate(-50%, -50%);
                    "
                    ><img src="@/assets/images/ico_arrow.png" alt="logo"
                  /></span>
                </span>
              </span>
            </span>
            <ul v-show="menuOpen == index" class="expander-target sidebar_box">
              <li
                v-for="(child, i) in route.functions"
                class="js-sidenav-tooltip sidenav-children"
                :key="i"
                @click="(e) => navigateToRoute(e, child, route.functions)"
                :class="currentPath == child.function_url && 'active'"
              >
                <router-link
                  :class="classActive(child)"
                  class="sidebar-text sidebar-custom-child sidebar-child"
                  style="display: flex; align-items: center; padding-left: 15px"
                  :to="'/' + child.function_url"
                >
                  <i
                    class="prx"
                    :class="child.function_icon"
                    style="padding-left: 10px"
                  ></i>
                  <span
                    class="sidebar-text sidebar-custom siderbar_name siderbar_name--custom"
                    >{{ $t(child.function_name) }}
                  </span>
                </router-link>
              </li>
            </ul>
          </div>
          <div v-else-if="route.function_group_code == 'DASHBOARD'">
            <a
              :to="'/' + route.functions[0].function_url"
              @click="(e) => navigateToRoute(e, route.functions[0])"
              :class="classActive(route)"
              class="js-sidenav-tooltip"
              style="display: flex; align-items: center"
            >
              <component
                class="icon_sidebar"
                :is="`${route.function_group_icon}`"
              ></component>
              <span
                class="sidebar-text sidebar-custom siderbar_name"
                :class="classActive(route)"
              >
                {{ $t(route.function_group_name) }}
              </span>
            </a>
          </div>
          <div v-else>
            <a
              href="#"
              @click="(e) => navigateChangeRoute(e, route)"
              :class="classActive(route)"
              class="js-sidenav-tooltip"
              style="display: flex; align-items: center"
            >
              <component
                class="icon_sidebar"
                :is="`${route.function_group_icon}`"
              ></component>
              <span
                class="sidebar-text sidebar-custom siderbar_name"
                :class="classActive(route)"
              >
                {{ $t(route.function_group_name) }}
              </span>
            </a>
          </div>
        </li>
      </ul>
      <p class="nav_others" v-if="listRouter.length">{{ $t("OTHERS") }}</p>
      <ul class="nav">
        <li
          style="border-bottom: 1px solid #d0d6da"
          v-for="(route, index) in listRouterOthers"
          :key="index"
        >
          <div>
            <router-link
              :to="route.function_url"
              :class="classActive(route)"
              class="js-sidenav-tooltip"
              style="display: flex; align-items: center"
            >
              <component
                class="icon_sidebar"
                :is="`${route.function_icon}`"
              ></component>
              <span
                class="sidebar-text sidebar-custom siderbar_name"
                :class="classActive(route)"
              >
                {{ $t(route.function_name) }}
              </span>
            </router-link>
          </div>
        </li>
      </ul>
    </div>
    <p class="vietis-name">{{ $t("common.text_vietis") }}</p>
  </div>
</template>

<script>
import IconCart from "@/svg/IconCart.vue";
import IconUser from "@/svg/IconUser.vue";
import IconChart from "@/svg/IconChart.vue";
import IconList from "@/svg/IconList.vue";
import IconSetting from "@/svg/IconSetting.vue";
import IconBidding from "@/svg/IconBidding.vue";
import IconContract from "@/svg/IconContract.vue";
import IconReport from "@/svg/IconReport.vue";
import IconUserGroup from "@/svg/IconUserGroup.vue";
import { COLOR_BLUE_1, COLOR_GRAY_1 } from "@/constants/color";
import PAGES from "@/utils/pages";
import { $globalMarket } from "@/utils/variables";

export default {
  name: "Sidebar",
  components: {
    IconCart,
    IconUser,
    IconChart,
    IconList,
    IconSetting,
    IconBidding,
    IconContract,
    IconReport,
    IconUserGroup,
  },
  data() {
    return {
      isShowComponent: false,
      menuOpen: "",
      listRouter: [],
      listRouterOthers: [
        {
          function_name: "Setting",
          function_code: "SETTING",
          function_url: "/setting",
          function_icon: "IconSetting",
        },
        {
          function_name: "Profile",
          function_code: "PROFILE",
          function_url: "/profile",
          function_icon: "IconUser",
        },
      ],
    };
  },
  computed: {
    currentPath() {
      return this.$route.path;
    },
  },
  mounted() {
    if (this.$store.state.auth.status.loggedIn && $globalMarket.value.value) {
      this.getFunctionByUser();
    }
  },
  watch: {
    "$globalMarket.value": {
      handler: async function () {
        this.isShowComponent = false;
        await this.getFunctionByUser();
      },
      deep: true,
    },
  },
  methods: {
    async getFunctionByUser() {
      this.startLoading();
      await this.$services.function.getFunctionByUser(
        { market: this.$globalMarket.value.value },
        (res) => {
          this.listRouter = res;
          if (this.listRouter.length > 0) {
            if (
              this.listRouter[0].children &&
              this.listRouter[0].children.length > 0
            ) {
              this.menuOpen = "-1";
            }
            // Sort by order_no
            this.listRouter.sort((a, b) => {
              return a.order_no - b.order_no;
            });
          }
          this.isShowComponent = true;
          this.endLoading();
        },
        (err) => {
          this.endLoading();
          this.notifyError(this.$t(err.error));
        }
      );
    },
    classActive(route) {
      // Dashboard path is '/'
      if (
        this.$route.path == PAGES.DASHBOARD &&
        route.functions &&
        route.functions[0]?.function_url == PAGES.DASHBOARD.replace("/", "")
      )
        return "active";

      if (
        route.function_url != "/" &&
        this.currentPath.indexOf(route.function_url) !== -1
      )
        return "active";

      return "";
    },
    navigateToRoute(e, route, functions = []) {
      this.$router.push({ path: "/" + route.function_url, force: true });
      document
        .querySelectorAll(".expander-trigger")
        .forEach((e) => e.classList.remove("active-expander"));
      document
        .querySelectorAll(".nav")
        .forEach((e) =>
          e
            .querySelectorAll("svg path")
            .forEach((el) => el.setAttribute("fill", COLOR_GRAY_1))
        );
      const target = e.target;
      const tagName = target.tagName.toLowerCase();
      const navList = target.closest(".nav-list");
      if (
        functions.length &&
        functions.map((item) => item.function_url).includes(route.function_url)
      ) {
        const expander = navList.querySelector(".expander-trigger");
        expander.classList.add("active-expander");
        expander.querySelector("svg path")?.setAttribute("fill", COLOR_BLUE_1);
      } else {
        this.checkTargetClickRouter(target, tagName);
      }
      const element = document.querySelector("#remove_class");
      element.classList.remove("active");
    },

    navigateChangeRoute(e, route) {
      document
        .querySelectorAll(".nav")
        .forEach((e) =>
          e
            .querySelectorAll("svg path")
            .forEach((el) => el.setAttribute("fill", COLOR_GRAY_1))
        );
      if (route.function_url == "/dashboard")
        window.location.href = route.function_url;
      else this.$router.push({ path: route.function_url, force: true });
      const target = e.target;
      const tagName = target.tagName.toLowerCase();
      this.checkTargetClickRouter(target, tagName);
      this.menuOpen = ""; // Đóng submenu (nếu cần)
      const element = document.querySelector("#remove_class");
      element.classList.remove("active");
    },

    checkTargetClickRouter(target, tagName) {
      switch (tagName) {
        case "a":
          return target
            .querySelector("svg path")
            ?.setAttribute("fill", COLOR_BLUE_1);
        case "span":
          return target
            .closest("a")
            .querySelector("svg path")
            ?.setAttribute("fill", COLOR_BLUE_1);
        case "svg":
          return target
            .querySelector("path")
            ?.setAttribute("fill", COLOR_BLUE_1);

        default:
          return target.setAttribute("fill", COLOR_BLUE_1);
      }
    },
  },
};
</script>

<style lang="scss" scope>
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

.vietis-name {
  color: #a6abc8;
  font-size: 14px;
  text-align: center;
}
</style>
