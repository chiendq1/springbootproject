<template>
  <!-- Error Page -->
  <div class="error">
    <div class="container-fluid">
      <div class="col-xs-12 ground-color text-center">
        <div class="container-error-404">
          <div class="msg">Oh!<span class="triangle"></span></div>
        </div>
        <h2 class="h1">403 Forbidden</h2>
        <a
          class="home-link"
          href="#"
          style="color: #8d8d8d"
          @click="$router.push({name: PAGE_NAME.HOME})"
        >
          Hitch a ride back home.
        </a>
      </div>
    </div>
  </div>
  <!-- Error Page -->
</template>
<script>
import PAGE_NAME from "@/constants/route-name.js";

export default {
  setup() {
    return {
      PAGE_NAME
    }
  }
};
</script>

<style lang="scss" scoped>
* {
  font-family: "PT Sans Caption", sans-serif, "arial", "Times New Roman";
}
/* Error Page */
.error .clip .shadow {
  height: 180px; /*Contrall*/
}
.error .clip:nth-of-type(2) .shadow {
  width: 130px; /*Contrall play with javascript*/
}
.error .clip:nth-of-type(1) .shadow,
.error .clip:nth-of-type(3) .shadow {
  width: 250px; /*Contrall*/
}
.error .digit {
  width: 150px; /*Contrall*/
  height: 150px; /*Contrall*/
  line-height: 150px; /*Contrall*/
  font-size: 120px;
  font-weight: bold;
}
.error h2   /*Contrall*/ {
  font-size: 32px;
}
.error .msg /*Contrall*/ {
  width: 80px;
  line-height: 80px;
  font-size: 32px;
  display: block;
  margin: auto;
}
.error span.triangle    /*Contrall*/ {
  top: 70%;
  right: 0%;
  border-left: 20px solid #535353;
  border-top: 15px solid transparent;
  border-bottom: 15px solid transparent;
}

.error .container-error-404 {
  margin-top: 10%;
  position: relative;
  padding-top: 40px;
}
.error .container-error-404 .clip {
  display: inline-block;
  transform: skew(-45deg);
}
.error .clip .shadow {
  overflow: hidden;
}
.error .clip:nth-of-type(2) .shadow {
  overflow: hidden;
  position: relative;
  box-shadow: inset 20px 0px 20px -15px rgba(150, 150, 150, 0.8),
    20px 0px 20px -15px rgba(150, 150, 150, 0.8);
}

.error .clip:nth-of-type(3) .shadow:after,
.error .clip:nth-of-type(1) .shadow:after {
  content: "";
  position: absolute;
  right: -8px;
  bottom: 0px;
  z-index: 9999;
  height: 100%;
  width: 10px;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(173, 173, 173, 0.8),
    transparent
  );
  border-radius: 50%;
}
.error .clip:nth-of-type(3) .shadow:after {
  left: -8px;
}
.error .digit {
  position: relative;
  top: 8%;
  color: white;
  background: #07b3f9;
  border-radius: 50%;
  display: inline-block;
  transform: skew(45deg);
}
.error .clip:nth-of-type(2) .digit {
  left: -10%;
}
.error .clip:nth-of-type(1) .digit {
  right: -20%;
}
.error .clip:nth-of-type(3) .digit {
  left: -20%;
}
.error h2 {
  color: #a2a2a2;
  font-weight: bold;
  padding-bottom: 20px;
}
.error .msg {
  position: relative;
  z-index: 9999;
  display: block;
  background: #535353;
  color: #a2a2a2;
  border-radius: 50%;
  font-style: italic;
}
.error .triangle {
  position: absolute;
  z-index: 999;
  transform: rotate(45deg);
  content: "";
  width: 0;
  height: 0;
}
</style>
