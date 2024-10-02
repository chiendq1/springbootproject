<template>
  <el-card class="box-card bill-details-card">
    <div class="d-flex bill-details-header">
      <h3>{{ $t("bill.bill_details_title") }}</h3>
    </div>
    <el-form class="bill-details-body" :model="billDetails" label-width="120px">
      <el-form-item :label="$t('bill.form.label_bill_code')">
        <el-input
          v-model="billDetails.billCode"
          :placeholder="$t('bill.form.placeholder_bill_code')"
          :disabled="!isCreate"
        ></el-input>
        <p v-if="validation.value.billCode" class="error-feedback">
          {{
            $t(
              validation.value.billCode.code,
              validation.value.billCode.options
            )
          }}
        </p>
      </el-form-item>
      <el-form-item :label="`${$t('bill.form.label_room_code')}`">
        <SingleOptionSelect
          v-model="billDetails.room.roomId"
          :listData="listRoomsByRole"
          :placeholder="$t('bill.room_placeholder')"
          :isRemote="false"
          @change="$emit('changeRoom', billDetails.room.roomId)"
          :disabled="!isCreate"
        />
        <p v-if="validation.value.roomId" class="error-feedback">
          {{
            $t(validation.value.roomId.code, validation.value.roomId.options)
          }}
        </p>
      </el-form-item>
      <el-form-item :label="`${$t('bill.form.label_bill_date')}`">
        <el-date-picker
          style="width: 100%"
          v-model="billDetails.date"
          type="month"
          :placeholder="$t('bill.form.bill_date_placeholder')"
          :size="default"
          :disabled="true"
        />
        <p v-if="validation.value.date" class="error-feedback">
          {{ $t(validation.value.date.code, validation.value.date.options) }}
        </p>
      </el-form-item>
      <el-form-item :label="$t('bill.form.label_room_tenants')">
        <MultipleOptionSelect
          v-model="billDetails.room.tenants"
          :placeholder="$t('bill.form.room_tenants_placeholder')"
          :list-data="listRoomTenants"
          :showClearable="true"
          :isRemote="true"
          :disabled="true"
        />
      </el-form-item>
      <el-form-item :label="$t('bill.form.label_room_landlord')">
        <el-input
          v-model="billDetails.landlord.fullName"
          :placeholder="$t('bill.form.room_landlord_placeholder')"
          :disabled="true"
        ></el-input>
      </el-form-item>
      <el-form-item :label="$t('bill.form.label_bill_status')">
        <SingleOptionSelect
          v-model="billDetails.status"
          :listData="listStatuses"
          :placeholder="$t('bill.form.bill_status_placeholder')"
          :isRemote="false"
          :disabled="billDetails.status == BILL_STATUS_PAID"
        />
        <p v-if="validation.value.status" class="error-feedback">
          {{
            $t(validation.value.status.code, validation.value.status.options)
          }}
        </p>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
import SingleOptionSelect from "@/components/common/SingleOptionSelect.vue";
import MultipleOptionSelect from "@/components/common/MultipleOptionSelect.vue";
import { BILL_STATUS_PAID } from "@/constants/bill.js";

export default {
  components: {
    SingleOptionSelect,
    MultipleOptionSelect,
  },
  props: {
    billDetails: {
      type: Object,
      default: {},
    },
    validation: {
      type: Object,
      default: {},
    },
    listRoomTenants: {
      type: Array,
      default: [],
    },
    listStatuses: {
      type: Array,
      default: [],
    },
    listRoomsByRole: {
      type: Array,
      default: [],
    },
    isCreate: {
      type: Boolean,
      default: true,
    },
  },
  setup(props, { emit }) {
    return {
      BILL_STATUS_PAID
    };
  },
};
</script>
<style lang="scss" scoped>
.bill-details {
  &-header {
    h2 {
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
  &-body {
    padding-bottom: 40px;
  }
}

.bill-details-buttons {
  width: 100%;
  margin: 24px 0;
  display: flex;
  justify-content: end;
}
</style>
