<template>
    <div>
      <h1>{{ message }}</h1> <!-- No need for .value in the template -->
    </div>
  </template>
  
  <script>
  import { ref } from "vue";
  
  export default {
    name: "Home",
    setup() {
      const message = ref("Home page");
  
      // Return it inside an object
      return { message };
    },
  };
  </script>
  
  <style lang="scss">
  </style>
  