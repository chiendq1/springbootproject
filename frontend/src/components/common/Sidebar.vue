<template>
  <div class="siderbar-block" v-if="isShowComponent">
    <div class="sidebar-logo">
      <a href="/"><img src="@/assets/logo.png" class="logo_zoom_in" alt="logo" /></a>
      <a href="/"><img src="@/assets/img_logo_zoom_in.png" class="logo_zoom_out" alt="logo" /></a>
    </div>
    <div class="menu-box">
      <ul class="nav" v-if="listRouter.length">
        <li
          v-for="(route, index) in listRouter"
          :key="index"
          class="nav-list"
          style="border-bottom: 1px solid #d0d6da"
        >
          <div v-if="route.functions && route.functions.length > 0 && route.function_group_code != 'DASHBOARD'">
            <span
              class="expander-trigger cursor-pointer"
              @click="toggleMenu(index)"
              style="display: flex; align-items: center"
            >
              <component :is="route.function_group_icon" class="icon_sidebar"></component>
              <span class="sidebar-text sidebar-custom siderbar_name" :class="classActive(route)">
                {{ $t(route.function_group_name) }}
                <span
                  class="expander expander-trigger expander-children p-0"
                  :class="{ open: menuOpen === index }"
                  @click="toggleMenu(index)"
                  style="top: 12px; transform: translateY(-50%)"
                >
                  <span
                    class="caret"
                    style="left: 50%; top: 50%; transform: translate(-50%, -50%)"
                  ><img src="@/assets/images/ico_arrow.png" alt="logo" /></span>
                </span>
              </span>
            </span>
            <ul v-show="menuOpen === index" class="expander-target sidebar_box">
              <li
                v-for="(child, i) in route.functions"
                :key="i"
                class="js-sidenav-tooltip sidenav-children"
                :class="{ active: currentPath === child.function_url }"
                @click="navigateToRoute(child)"
              >
                <router-link
                  :to="'/' + child.function_url"
                  :class="classActive(child)"
                  class="sidebar-text sidebar-custom-child sidebar-child"
                  style="display: flex; align-items: center; padding-left: 15px"
                >
                  <i :class="child.function_icon" class="prx" style="padding-left: 10px"></i>
                  <span class="sidebar-text sidebar-custom siderbar_name siderbar_name--custom">
                    {{ $t(child.function_name) }}
                  </span>
                </router-link>
              </li>
            </ul>
          </div>
          <div v-else-if="route.function_group_code === 'DASHBOARD'">
            <router-link
              :to="'/' + route.functions[0].function_url"
              @click="navigateToRoute(route.functions[0])"
              :class="classActive(route)"
              class="js-sidenav-tooltip"
              style="display: flex; align-items: center"
            >
              <component :is="route.function_group_icon" class="icon_sidebar"></component>
              <span class="sidebar-text sidebar-custom siderbar_name">{{ $t(route.function_group_name) }}</span>
            </router-link>
          </div>
          <div v-else>
            <a
              href="#"
              @click.prevent="navigateChangeRoute(route)"
              :class="classActive(route)"
              class="js-sidenav-tooltip"
              style="display: flex; align-items: center"
            >
              <component :is="route.function_group_icon" class="icon_sidebar"></component>
              <span class="sidebar-text sidebar-custom siderbar_name">{{ $t(route.function_group_name) }}</span>
            </a>
          </div>
        </li>
      </ul>
      <p class="nav_others" v-if="listRouter.length">{{ $t('OTHERS') }}</p>
      <ul class="nav">
        <li v-for="(route, index) in listRouterOthers" :key="index" style="border-bottom: 1px solid #d0d6da">
          <router-link :to="route.function_url" :class="classActive(route)" class="js-sidenav-tooltip" style="display: flex; align-items: center">
            <component :is="route.function_icon" class="icon_sidebar"></component>
            <span class="sidebar-text sidebar-custom siderbar_name">{{ $t(route.function_name) }}</span>
          </router-link>
        </li>
      </ul>
    </div>
    <p class="vietis-name">{{ $t('common.text_vietis') }}</p>
  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue';
import IconCart from '@/svg/IconCart.vue';
import IconUser from '@/svg/IconUser.vue';
import IconUserGroup from '@/svg/IconUserGroup.vue';
import IconRooms from '@/svg/IconRooms.vue'; // Newly added
import { COLOR_BLUE_1, COLOR_GRAY_1 } from '@/constants/color';
import PAGES from '@/utils/pages';
import { $globalMarket } from '@/utils/variables';

export default {
  name: 'Sidebar',
  components: {
    IconCart,
    IconUser,
    IconUserGroup,
    IconRooms, // Added Room icon
  },
  setup() {
    const isShowComponent = ref(false);
    const menuOpen = ref('');
    const listRouter = ref([]);
    const listRouterOthers = ref([
      { function_name: 'Setting', function_code: 'SETTING', function_url: '/setting', function_icon: 'IconUserGroup' },
      { function_name: 'Profile', function_code: 'PROFILE', function_url: '/profile', function_icon: 'IconUser' },
      { function_name: 'Rooms', function_code: 'ROOMS', function_url: '/rooms', function_icon: 'IconRooms' }, // Added Room entry
    ]);
    const currentPath = ref('');

    const getFunctionByUser = async () => {
      // Sample logic to fetch function routes
      listRouter.value = await fetchFunctionByUser(); // Assume `fetchFunctionByUser()` is your API
      listRouter.value.sort((a, b) => a.order_no - b.order_no);
      isShowComponent.value = true;
    };

    const toggleMenu = (index) => {
      menuOpen.value = menuOpen.value === index ? '' : index;
    };

    const navigateToRoute = (route) => {
      // Logic for navigating to route
    };

    const navigateChangeRoute = (route) => {
      // Logic to handle changes in routes
    };

    const classActive = (route) => {
      return currentPath.value.includes(route.function_url) ? 'active' : '';
    };

    onMounted(() => {
      getFunctionByUser();
    });

    watch($globalMarket, async () => {
      isShowComponent.value = false;
      await getFunctionByUser();
    });

    return {
      isShowComponent,
      menuOpen,
      listRouter,
      listRouterOthers,
      currentPath,
      toggleMenu,
      navigateToRoute,
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

.vietis-name {
  color: #a6abc8;
  font-size: 14px;
  text-align: center;
}
</style>
