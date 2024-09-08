import { createWebHistory, createRouter } from "vue-router";
import PAGES from '@/utils/pages';
import Cookies from "js-cookie";

// import pages
import Login from '@/pages/Login.vue';
import Home from '@/pages/Home.vue';

const routes = [
  {
    path: PAGES.LOGIN,
    component: Login,
  },
  {
    path: PAGES.HOME,
    component: Home,
    meta: {
      middleware: [
        'authentication'
      ],
    }
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  window.scrollTo(0, 0);

  const { middleware } = to.meta || {};  // Safely access meta.middleware
  const token = Cookies.get("access_token");

  // Check if middleware exists and includes 'authentication'
  if (middleware && middleware.includes("authentication") && !token) {
    return next(PAGES.LOGIN);
  }

  next();
});

export default router;
