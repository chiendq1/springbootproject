import { useAuthStore } from "@/store/auth.js";

export const authMiddleware = (to, from, next) => {
  const authStore = useAuthStore();
  const {
    loggedIn
  } = authStore;

  next();
};
