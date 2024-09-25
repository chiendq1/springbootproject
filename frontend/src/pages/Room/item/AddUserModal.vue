<template>
  <Modal
    :show="show"
    :width="'50%'"
    :height="400"
    :isShowFooter="false"
    @close="$emit('close')"
  >
    <template #header>
      <h4 class="modal-title">{{ $t("room.room_add_tenant") }}</h4>
    </template>
    <template #body>
      <div class="modal-body">
        <UsersTable
          :data="dataUsers"
          :selectedUsers="selectedUsers"
          :disabled="disabled"
          @pick="handleUserPick"
        />
      </div>
      <div class="modal-footer">
        <el-button class="btn btn-save" @click="handleSubmit">{{
          $t("common.save")
        }}</el-button>
        <el-button class="btn btn-refuse" @click="$emit('close')">{{
          $t("common.cancel")
        }}</el-button>
      </div>
    </template>
  </Modal>
</template>

<script>
import IconCircleClose from "@/svg/IconCircleClose.vue";
import { onUnmounted, ref } from "vue";
import { LANDLORD, TENANT } from "@/constants/roles.js";
import { useI18n } from "vue-i18n";
import Modal from "@/components/common/Modal.vue";
import UsersTable from "./UsersTable.vue";
import { validate } from "vee-validate";

export default {
  name: "AddUserModal",
  components: { IconCircleClose, Modal, UsersTable },
  props: {
    show: {
      type: Boolean,
      default: false,
    },
    dataUsers: {
      type: Array,
      default: [],
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  setup(props, { emit }) {
    const { t } = useI18n();
    const selectedUsers = ref([]);
    const roles = ref([
      { key: t("role.landlord"), value: LANDLORD },
      { key: t("role.tenant"), value: TENANT },
    ]);

    const handleUserPick = (listPickedUsers) => {
      selectedUsers.value = listPickedUsers;
    };

    const handleSubmit = () => {
      emit("submit", selectedUsers.value);
    };

    return {
      roles,
      selectedUsers,
      handleUserPick,
      handleSubmit,
    };
  },
};
</script>

<style scoped>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}
.modal-title {
  margin: 0;
}
.modal-container {
  width: 500px;
  background-color: white;
  border-radius: 4px;
  padding: 20px;
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.modal-footer {
  display: flex;
  justify-content: flex-end;
}
</style>
