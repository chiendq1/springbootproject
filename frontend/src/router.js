import { createWebHistory, createRouter } from "vue-router";
import PAGE_NAME from "@/constants/route-name.js";
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
import RoomDetails from "@/pages/Room/RoomDetails.vue";
import Contract from "@/pages/Contract/Index.vue";
import ContractList from "@/pages/Contract/ContractList.vue";
import Bill from "@/pages/Bill/Index.vue";
import BillList from "@/pages/Bill/BillList.vue";
import BillDetails from "@/pages/Bill/BillDetails.vue";
import UserList from "@/pages/User/UserList.vue";
import Forbidden from "@/pages/Forbidden.vue";

const routes = [
  {
    name: PAGE_NAME.LOGIN,
    path: PAGES.LOGIN,
    component: Login,
  },
  {
    name: PAGE_NAME.HOME,
    path: PAGES.HOME,
    component: Home,
    meta: {
      middleware: ["authentication"],
    },
  },
  {
    name: PAGE_NAME.PROFILE,
    path: PAGES.PROFILE,
    component: UserProfile,
    meta: {
      middleware: ["authentication"],
    },
  },
  {
    name: PAGE_NAME.FORBIDDEN,
    path: PAGES.FORBIDDEN,
    component: Forbidden,
    meta: {
      middleware: ["authentication"],
    },
  },
  {
    name: PAGE_NAME.USERMANAGEMENT,
    path: PAGES.USERMANAGEMENT,
    component: User,
    meta: {
      middleware: ["authentication", "admin-role"],
    },
    children: [
      {
        path: "",
        name: PAGE_NAME.USER.LIST,
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
        name: PAGE_NAME.ROOM.LIST,
        component: RoomList,
      },
      {
        path: PAGES.ROOM_DETAILS,
        name: PAGE_NAME.ROOM.DETAILS,
        component: RoomDetails,
      },
      {
        path: PAGES.ROOM_CREATE,
        name: PAGE_NAME.ROOM.CREATE,
        component: RoomDetails,
      },
    ],
  },
  {
    path: PAGES.CONTRACT,
    component: Contract,
    meta: {
      middleware: ["authentication", "manager-role"],
    },
    children: [
      {
        path: "",
        name: PAGE_NAME.CONTRACT.LIST,
        component: ContractList,
      },
    ],
  },
  {
    path: PAGES.BILL,
    component: Bill,
    meta: {
      middleware: ["authentication"],
    },
    children: [
      {
        path: "",
        component: BillList,
        name: PAGE_NAME.BILL.LIST,
      },
      {
        path: PAGES.BILL_CREATE,
        component: BillDetails,
        name: PAGE_NAME.BILL.CREATE,
      },
      {
        path: PAGES.BILL_DETAILS,
        component: BillDetails,
        name: PAGE_NAME.BILL.DETAILS,
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

  if (
    middleware &&
    middleware.includes("admin-role") &&
    highest_role !== ADMIN
  ) {
    return next(PAGES.FORBIDDEN);
  }

  if (middleware && middleware.includes("manager-role")) {
    if (highest_role !== ADMIN && highest_role !== LANDLORD)
      return next(PAGES.FORBIDDEN);
  }

  next();
});

export default router;
