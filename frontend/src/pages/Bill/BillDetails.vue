<template>
  <div class="bill-details-page">
    <div class="bill-header">
      <div class="bill-header-item">
        <span class="btn-back" @click="handleBack"><IconBackMain /></span>
        <h3 class="page__ttl">{{ $t("bill.title") }}</h3>
      </div>
      <div class="bill-header-item">
        <el-button
          class="btn btn-save"
          v-if="highest_role == ADMIN || highest_role == LANDLORD"
          @click="handleSubmit"
          >{{ $t("common.save") }}</el-button
        >
        <el-button v-if="isCreate" class="btn btn-refuse" @click="handleCancel">{{
          $t("common.cancel")
        }}</el-button>
      </div>
    </div>
    <div class="bill-details-infor">
      <div class="bill-details-card-container">
        <el-row :gutter="20" class="bill-details-collapse">
          <el-col :span="10">
            <BillRoomInforCard
              :data="billDetails.value"
              :validation="validation"
              :isCreate="isCreate"
              :listRoomsByRole="listRoomsByRole.value"
              :listStatuses="listStatuses.value"
              :listRoomTenants="listTenants.value"
              @changeRoom="handleChangeRoom"
            />
          </el-col>
          <el-col :span="14">
            <BillFeeCard :isCreate="isCreate" :data="billDetails.value" />
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import IconBackMain from "@/svg/IconBackMain.vue";
import PAGE_NAME from "@/constants/route-name.js";
import BillRoomInforCard from "./item/BillRoomInforCard.vue";
import BillFeeCard from "./item/BillFeeCard.vue";
import Cookies from "js-cookie";
import { onMounted, onUnmounted, reactive, ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRoute, useRouter } from "vue-router";
import { useBillStore } from "@/store/bills.js";
import { useUserStore } from "@/store/users.js";
import { useRoomStore } from "@/store/rooms.js";

export default {
  name: "BillSave",
  components: {
    IconBackMain,
    BillRoomInforCard,
    BillFeeCard,
  },
  setup() {
    const { t } = useI18n();
    const route = useRoute();
    const router = useRouter();
    const userStore = useUserStore();
    const roomStore = useRoomStore();
    const billStore = useBillStore();
    const isCreate = ref(false);
    const highest_role = Cookies.get("highest_role");
    const { listTenants, getListUsersByRole } = userStore;
    const { getListRoomsByRole, listRoomsByRole, getRoomDetails, roomDetails } =
      roomStore;
    const {
      validation,
      listStatuses,
      billDetails,
      setBillDetails,
      resetBillDetails,
      updateBill,
      getBillDetails,
      createNewBill,
    } = billStore;

    onMounted(() => {
      isCreate.value = !route.params.id;
      if (route.params.id) getBillDetails(route.params.id);
      getListRoomsByRole(true);
      getListUsersByRole();
    });

    onUnmounted(() => {});

    const handleBack = () => {
      router.push({ name: PAGE_NAME.BILL.LIST });
    };

    const handleChangeRoom = async (roomId) => {
      await getRoomDetails(roomId);
      await setBillDetails(roomDetails, true);
    };

    const handleSubmit = () => {
      if(isCreate.value) createNewBill();
      else updateBill(route.params.id);
    };

    const handleCancel = () => {
      resetBillDetails();
    };

    return {
      listTenants,
      highest_role,
      validation,
      billDetails,
      listRoomsByRole,
      listStatuses,
      isCreate,
      handleBack,
      handleCancel,
      handleSubmit,
      handleChangeRoom,
    };
  },
};
</script>

<style scoped lang="scss">
.bill-details-page {
  padding: 20px;
  .bill-header {
    display: flex;
    padding: 32px 0;
    &-item {
      display: flex;
    }
  }
  .bill-details-infor {
    display: flex;
    .bill-details-card-container {
      width: 100%;
      position: sticky;
    }
    .bill-details-collapse {
      width: 100%;
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
