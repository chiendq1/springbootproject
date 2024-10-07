<template>
  <el-table :data="data" max-height="400"	 style="width: 100%" class="el-tbl-custom room-tbl">
    <el-table-column min-width="120">
      <template #header>
        <p v-html="$t('user.table.header.username')"></p>
      </template>

      <template #default="scope">
        <span class="data-table">{{ scope.row.fullName }} </span>
      </template>
    </el-table-column>
    <el-table-column min-width="130">
      <template #header>
        <p v-html="$t('user.table.header.phone')"></p>
      </template>

      <template #default="scope">
        <span class="data-table">{{ scope.row.phoneNumber }} </span>
      </template>
    </el-table-column>
    <el-table-column min-width="190">
      <template #header>
        <p v-html="$t('user.table.header.email')"></p>
      </template>

      <template #default="scope">
        <span class="data-table">{{ scope.row.email }} </span>
      </template>
    </el-table-column>
    <el-table-column min-width="60">
      <template #header>
        <p v-html="$t('room.table.header.action')"></p>
      </template>
      <template #default="scope">
        <div>
          <button
            @click="$emit('delete', scope.row.id)"
            class="btn-edit"
            v-if="!isPicker && disabled"
          >
            <IconTrash />
          </button>
          <el-checkbox
            v-if="isPicker"
            @change="onPickUser(scope.row.id)"
          >
          </el-checkbox>
        </div>
      </template>
    </el-table-column>
  </el-table>
</template>
<script>
import IconTrash from "@/svg/IconTrash.vue";
import { ref } from "vue";

export default {
  components: {
    IconTrash,
  },
  props: {
    data: {
      type: Array,
      default: [],
    },
    disabled: {
      type: Boolean,
      default: true,
    },
    isPicker: {
      type: Boolean,
      default: false,
    },
  },
  setup(props, { emit }) {
    const selectedUsers = ref([]);
    const onPickUser = (id) => {
      const index = selectedUsers.value.indexOf(id);

      if (index === -1) {
        // Add user if not in the selected list
        selectedUsers.value.push(id);
      } else {
        // Remove user if already in the selected list
        selectedUsers.value.splice(index, 1);
      }
      emit("pick", selectedUsers.value);
    };

    return {
      selectedUsers,
      onPickUser,
    };
  },
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
