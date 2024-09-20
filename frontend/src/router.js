import { createWebHistory, createRouter } from "vue-router";
import { USER_ROUTE, ROOM_ROUTE } from "@/constants/route-name.js";
import PAGES from "@/utils/pages";
import { ADMIN, LANDLORD } from "@/constants/roles.js";
import Cookies from "js-cookie";

// import pages
import Login from "@/pages/Login.vue";
import Home from "@/pages/Home.vue";
import UserProfile from "@/pages/User/Profile.vue";
import User from "@/pages/User/Index.vue";
import Room from "@/pages/Room/Index.vue";
import RoomList from "@/pages/Room/RoomList.vue";
import UserList from "@/pages/User/UserList.vue";
import Forbidden from '@/pages/Forbidden.vue'

const routes = [
  {
    path: PAGES.LOGIN,
    component: Login,
  },
  {
    path: PAGES.HOME,
    component: Home,
    meta: {
      middleware: ["authentication"],
    },
  },
  {
    path: PAGES.PROFILE,
    component: UserProfile,
    meta: {
      middleware: ["authentication"],
    },
  },
  {
    path: PAGES.FORBIDDEN,
    component: Forbidden,
    meta: {
      middleware: ["authentication"],
    },
  },
  {
    path: PAGES.USERMANAGEMENT,
    component: User,
    meta: {
      middleware: ["authentication", "admin-role"],
    },
    children: [
      {
        path: "",
        name: USER_ROUTE.LIST,
        component: UserList,
      },
    ],
  },
  {
    path: PAGES.ROOM,
    component: Room,
    meta: {
      middleware: ["authentication", "manager-role"],
    },
    children: [
      {
        path: "",
        name: ROOM_ROUTE.LIST,
        component: RoomList,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  window.scrollTo(0, 0);

  const { middleware } = to.meta || {}; // Safely access meta.middleware
  const token = Cookies.get("access_token");
  const highest_role = Cookies.get("highest_role");

  // Check if middleware exists and includes 'authentication'
  if (middleware && middleware.includes("authentication") && !token) {
    return next(PAGES.LOGIN);
  }

  if (middleware && middleware.includes("admin-role") && highest_role !== ADMIN) {
    return next(PAGES.FORBIDDEN);
  }
  
  if (middleware && middleware.includes("manager-role")) {
    if(highest_role !== ADMIN && highest_role !== LANDLORD) return next(PAGES.FORBIDDEN);
  }

  next();
});

export default router;
