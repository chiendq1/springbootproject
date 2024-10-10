<template>
  <el-card class="box-card room-details-card">
    <div class="list-users">
      <div class="d-flex list-users-header">
        <h3>{{ $t("room.list_tenants") }}</h3>
        <div v-if="(highest_role == LANDLORD || highest_role == ADMIN) && roomDetails.status == VACANT_STATUS" @click="$emit('addNew')" class="d-flex">
          <IconPlus />
        </div>
      </div>
      <UsersTable :data="data" :disabled="disabled" @delete="handleOpenModalConfirm"/>
    </div>
  </el-card>
</template>
<script>
import IconTrash from "@/svg/IconTrash.vue";
import IconPlus from "@/svg/IconPlus.vue";
import UsersTable from "./UsersTable.vue";
import { VACANT_STATUS } from "@/constants/room.js";
import { LANDLORD, ADMIN } from "@/constants/roles.js";
import Cookies from "js-cookie";

export default {
  components: {
    IconPlus,
    IconTrash,
    UsersTable,
  },
  props: {
    data: {
      type: Array,
      default: [],
    },
    disabled: {
      type: Boolean,
      default: true
    },
    roomDetails: {
      type: Object,
      default: {}
    }
  },
  setup(props, {emit}){
    const highest_role = Cookies.get("highest_role");
    const handleOpenModalConfirm = (id) => {
      emit('delete', id);
    }

    return {
      LANDLORD,
      ADMIN,
      highest_role,
      VACANT_STATUS,
      handleOpenModalConfirm
    }
  }
};
</script>
<style lang="scss" scoped>
.list-users-header {
  h3 {
    width: 90%;
    text-align: start;
  }
  div {
    align-items: center;
    svg {
      cursor: pointer;
    }
  }
}
</style>
