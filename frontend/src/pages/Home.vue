<template>
  <div>
    <h1>{{ message }}</h1>
    <!-- No need for .value in the template -->
    <button @click="handleClick()">click here</button>
    {{$t('common.password')}}
  </div>
</template>

<script>
import { ref, watch } from "vue";
import { $services } from "@/utils/variables";
import { useAuthStore } from "@/store/auth.js";
import PAGE_NAME from "@/constants/route-name.js";
import { useRouter } from "vue-router";

export default {
  name: "Home",
  setup() {
    const message = ref("Home page");
    const authStore = useAuthStore();
    const router = useRouter();
    const {
      loggedIn,
    } = authStore;

    watch(() => {
      if (loggedIn.value) {
        router.push({name: PAGE_NAME.HOME}); // Redirect to home if logged in
      }
    });

    const handleClick = () => {
      $services.UserAPI.index(
        {},
        (res) => {
          console.log(res.data);
        },
        (err) => {
          console.log(err);
        }
      );
    };

    // Return it inside an object
    return { message, handleClick };
  },
};
</script>

<style lang="scss"></style>
