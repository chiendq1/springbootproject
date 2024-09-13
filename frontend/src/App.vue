<template>
  <div v-if="loggedIn.value" class="main">
    <div class="container-fluid" style="height: 100vh">
      <div class="row" style="height: 100%">
        <div
          id="remove_block"
          :class="{
            'col10-width-set px-0': true,
            'set-width__block': isMenu,
          }"
          style="height: 100%"
        >
          <div class="main-block">
            <div
              id="remove_class"
              :class="{
                'col2-width-set': true,
                'set-width__menu': isMenu,
                'is-sidebar-hover': isHovered && isWidth1025,
              }"
            >
              <div class="btn-menu">
                <button class="btn-close" @click="handleOpenClose">X</button>
                <button
                  @click="handleMenu"
                  class="btn-menu-item"
                  :class="{ active: isMenu }"
                >
                  <img src="@/assets/images/icon/ico_menu.svg" alt="" />
                </button>
              </div>
              <nav
                class="sidebar sidebar-inverse sidebar-fixed-top-bs"
                id="sidebar-general"
                @mouseover="addHoverClass"
                @mouseleave="removeHoverClass"
              >
                <!-- <Sidebar /> -->
                 <div>
                 </div>
              </nav>
              <div @click="handleOpenClose" class="bg-menu"></div>
            </div>
            <Navbar />
            <div class="btn-box">
              <button class="btn-open" @click="handleOpen">
                <img src="@/assets/images/icon/ico_menu.svg" alt="" />
              </button>
            </div>
            <div class="main-layout">
              <router-view />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div v-else>
    <router-view />
  </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount } from "vue";
import Navbar from "@/components/common/Navbar.vue";
// import Sidebar from "@/components/common/Sidebar";
import { useAuthStore } from "@/store/auth.js";
import { useRouter } from "vue-router";
import { $PAGES } from "@/utils/variables";

export default {
  components: { Navbar },
  setup() {
    const isMenu = ref(false);
    const isHovered = ref(false);
    const isWidth1025 = ref(window.innerWidth > 1025);
    const authStore = useAuthStore();
    const { loggedIn } = authStore;
    const router = useRouter();

    const handleMenu = () => {
      isMenu.value = !isMenu.value;
    };

    const handleResize = () => {
      isWidth1025.value = window.innerWidth > 1025;
    };

    const handleOpen = () => {
      const element = document.querySelector("#sidebar-general");
      const parent = document.querySelector("#remove_class");
      if (element) {
        document.body.classList.add("hidden");
        parent.classList.add("active");
      }
    };

    const handleOpenClose = () => {
      const element = document.querySelector("#sidebar-general");
      const parent = document.querySelector("#remove_class");
      if (element) {
        parent.classList.remove("active");
        document.body.classList.remove("hidden");
      }
    };

    const addHoverClass = () => {
      isHovered.value = true;
    };

    const removeHoverClass = () => {
      isHovered.value = false;
    };

    onMounted(() => {
      if(!loggedIn.value) {
        router.push($PAGES.LOGIN);
      }
      handleResize();
      window.addEventListener("resize", handleResize);
    });

    onBeforeUnmount(() => {
      window.removeEventListener("resize", handleResize);
    });

    return {
      isMenu,
      isHovered,
      isWidth1025,
      loggedIn,
      handleMenu,
      handleResize,
      handleOpen,
      handleOpenClose,
      addHoverClass,
      removeHoverClass,
    };
  },
};
</script>

<style lang="scss">
@import "@/assets/styles/app.scss";

.main-layout {
  max-height: 91vh;
  overflow-x: hidden;
  overflow-y: auto;
  padding-top: 2px;
}
a {
  color: $link-color;
  text-decoration: none;
  &:hover,
  &:focus {
    color: $link-hover-color;
    text-decoration: none;
    outline: none !important;
  }
  &:focus {
    outline: none !important;
    outline-offset: -2px;
  }
}
.el-select__tags,
.el-tag__content {
  height: 100% !important;
}
</style>
