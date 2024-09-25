<template>
  <el-card class="box-card room-details-card">
    <div class="list-users">
      <div class="d-flex list-users-header">
        <h3>{{ $t("room.list_tenants") }}</h3>
        <div @click="$emit('addNew')" class="d-flex">
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
    }
  },
  setup(props, {emit}){
    const handleOpenModalConfirm = (id) => {
      emit('delete', id);
    }

    return {
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
