<template>
  <div>
    <h1>{{ message }}</h1>
    <!-- No need for .value in the template -->
    <button @click="handleClick()">click here</button>
    {{$t('common.password')}}
  </div>
</template>

<script>
import { ref } from "vue";
import { $services } from "@/utils/variables";

export default {
  name: "Home",
  setup() {
    const message = ref("Home page");

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
