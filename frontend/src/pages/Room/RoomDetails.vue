<template>
  <div class="room-details-page">
    <div class="room-header">
      <span class="btn-back" @click="handleBack"><IconBackMain /></span>
      <h3 class="page__ttl">{{ $t("room.title") }}</h3>
    </div>
    <div class="room-details-infor">
      <div class="room-details-card-container">
        <RoomDetailsCard
          :isDisabled="allowUpdate.value"
          :roomDetails="roomDetails.value"
          :validation="validation"
          :listUtilities="listUtilities.value"
          :landlords="listLandlords.value"
          :listStatus="listStatus"
          :isCreate="isCreate.value"
          @save="onSubmit"
        />
      </div>
      <el-row :gutter="20" class="room-details-collapse">
        <el-col :span="24">
          <el-collapse v-model="activeCollapseItems">
            <el-collapse-item name="1">
              <template #title>
                <h3>{{ $t("room.room_tenants_title") }}</h3>
              </template>
              <ListUsersCard
                :data="roomUsers.value"
                @addNew="handleGetListNewTenants"
                @delete="handleOpenModalConfirm"
              />
            </el-collapse-item>
            <el-collapse-item name="2" v-if="!isCreate.value">
              <template #title>
                <h3>{{ $t("room.room_utilities_title") }}</h3>
              </template>
              <RoomUtilities :data="utilitiesConsumer.value" />
            </el-collapse-item>
          </el-collapse>
        </el-col>
      </el-row>
    </div>
    <AddUserModal
      @close="handleCloseModal"
      :dataUsers="listTenants.value"
      :show="isOpenDataUsersModal.value"
      @submit="handleAddTenants"
    />
    <ModalConfirm
      :isShowModal="isShowModalConfirm.value"
      @close-modal="handleCloseModal"
      :isConfirmByText="true"
      :confirmText="TEXT_CONFIRM_DELETE"
      @confirmAction="handleConfirm"
      :message="$t('room.modal_confirm.message_delete')"
      :title="$t('room.modal_confirm.title_delete')"
    />
  </div>
</template>

<script>
import Modal from "@/components/common/Modal.vue";
import ModalConfirm from "@/components/common/ModalConfirm.vue";
import { onMounted, onUnmounted, ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRoute, useRouter } from "vue-router";
import ListUsersCard from "./item/ListUsersCard.vue";
import RoomDetailsCard from "./item/RoomDetailsCard.vue";
import RoomUtilities from "./item/RoomUtilities.vue";
import AddUserModal from "./item/AddUserModal.vue";
import { useRoomStore } from "@/store/rooms.js";
import { useUserStore } from "@/store/users.js";
import { useUtilityStore } from "@/store/utility.js";
import { TEXT_CONFIRM_DELETE } from "@/constants/application.js";
import { ACTIVE_STATUS } from "@/constants/utility.js";
import IconBackMain from "@/svg/IconBackMain.vue";
import PAGE_NAME from "@/constants/route-name.js";

export default {
  name: "RoomDetails",
  components: {
    Modal,
    ListUsersCard,
    RoomDetailsCard,
    RoomUtilities,
    AddUserModal,
    ModalConfirm,
    IconBackMain,
  },
  setup() {
    const { t } = useI18n();
    const route = useRoute();
    const router = useRouter();
    const roomStore = useRoomStore();
    const userStore = useUserStore();
    const utilityStore = useUtilityStore();
    const activeCollapseItems = ref(["1", "2"]);
    const deleteId = ref(0);
    const { listLandlords, listTenants, getListUsersByRole } = userStore;
    const {
      getRoomDetails,
      roomUsers,
      roomDetails,
      utilitiesConsumer,
      resetData,
      isCreate,
      validation,
      allowUpdate,
      isOpenDataUsersModal,
      isShowModalConfirm,
      addRoomTenants,
      udpateRoomDetails,
      deleteRoomTenant,
      createNewRoom,
    } = roomStore;
    const { listUtilities, getListAllUtilities } = utilityStore;
    const listStatus = ref([
      {
        id: 0,
        value: t("room.status.vacant"),
      },
      {
        id: 1,
        value: t("room.status.rented"),
      },
      {
        id: 2,
        value: t("room.status.under_repair"),
      },
    ]);

    onMounted(() => {
      getListAllUtilities({status: ACTIVE_STATUS});
      getListUsersByRole();
      if (!route.params.id) {
        isCreate.value = true;
      }
      if (!isCreate.value) getRoomDetails(route.params.id);      
    });

    onUnmounted(() => {
      isCreate.value = false;
      listLandlords.value = [];
      resetData();
    });

    const handleBack = () => {
      router.push({ name: PAGE_NAME.ROOM.LIST });
    };

    const onSubmit = (isUpdate) => {
      if (isCreate.value) {
        createNewRoom();
        return;
      }
      if (isUpdate) udpateRoomDetails(route.params.id);
      else getRoomDetails(route.params.id);
    };

    const handleOpenModalConfirm = (id) => {
      if (isCreate.value) {
        let user = roomUsers.value.find((user) => user.id == id);
        listTenants.value.push({
          id: user.id,
          fullName: user.fullName,
          phoneNumber: user.phoneNumber,
          email: user.email,
          value: user.username,
        });
        roomUsers.value = roomUsers.value.filter((user) => user.id != id);
        return;
      }
      deleteId.value = id;
      isShowModalConfirm.value = true;
    };

    const handleCloseModal = () => {
      validation.value = {};
      isShowModalConfirm.value = false;
      isOpenDataUsersModal.value = false;
    };
    const handleGetListNewTenants = () => {
      isOpenDataUsersModal.value = true;
    };

    const handleAddTenants = (listAddTenants) => {
      if (isCreate.value) {
        roomUsers.value.push.apply(
          roomUsers.value,
          listTenants.value.filter((user) => listAddTenants.includes(user.id))
        );
        listTenants.value = listTenants.value.filter(
          (user) => !listAddTenants.includes(user.id)
        );
        isOpenDataUsersModal.value = false;
        return;
      }
      addRoomTenants(route.params.id, listAddTenants);
    };

    const handleConfirm = () => {
      deleteRoomTenant(route.params.id, [deleteId.value]);
    };

    return {
      listStatus,
      listUtilities,
      roomDetails,
      validation,
      roomUsers,
      listTenants,
      listLandlords,
      allowUpdate,
      isOpenDataUsersModal,
      utilitiesConsumer,
      activeCollapseItems,
      isCreate,
      isShowModalConfirm,
      TEXT_CONFIRM_DELETE,
      handleBack,
      handleCloseModal,
      handleConfirm,
      handleOpenModalConfirm,
      onSubmit,
      handleGetListNewTenants,
      handleAddTenants,
    };
  },
};
</script>

<style scoped lang="scss">
.room-details-page {
  padding: 20px;
  .room-header {
    display: flex;
    justify-content: left;
  }
  .room-details-infor {
    display: flex;
    .room-details-card-container {
      width: 40%;
      position: sticky;
    }
    .room-details-collapse {
      width: 60%;
      margin-left: 24px !important;
    }
  }
}

.box-card {
  height: 90%;
}

.room-details-card {
  height: auto;
}

.list-users {
  text-align: center;
  margin-bottom: 20px;
}

.user-actions {
  display: flex;
  justify-content: space-around;

  .btn {
    width: 90%;
  }
}

.info-card {
  padding: 20px;
}

h3 {
  margin: auto 12px;
}
</style>
