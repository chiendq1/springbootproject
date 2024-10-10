<template>
  <el-card class="box-card room-details-card">
    <div class="d-flex room-details-header">
      <h3>{{ $t("room.room_details_title") }}</h3>
    </div>
    <el-form :model="roomDetails" label-width="120px">
      <el-form-item :label="$t('room.form.label_room_code')">
        <el-input
          v-model="roomDetails.roomCode"
          :placeholder="$t('room.form.placeholder_room_code')"
          :disabled="isDisabled"
        ></el-input>
        <p v-if="validation.value.roomCode" class="error-feedback">
          {{
            $t(
              validation.value.roomCode.code,
              validation.value.roomCode.options
            )
          }}
        </p>
      </el-form-item>
      <el-form-item :label="`${$t('room.form.label_room_area')} (m&#178;)`">
        <el-input
          v-model="roomDetails.area"
          type="number"
          :placeholder="$t('room.form.placeholder_room_area')"
          :disabled="isDisabled"
        ></el-input>
        <p v-if="validation.value.area" class="error-feedback">
          {{ $t(validation.value.area.code, validation.value.area.options) }}
        </p>
      </el-form-item>
      <el-form-item :label="$t('room.form.label_room_capacity')">
        <el-input
          v-model="roomDetails.capacity"
          type="number"
          :placeholder="$t('room.form.placeholder_room_capacity')"
          :disabled="isDisabled"
        ></el-input>
        <p v-if="validation.value.capacity" class="error-feedback">
          {{
            $t(
              validation.value.capacity.code,
              validation.value.capacity.options
            )
          }}
        </p>
      </el-form-item>
      <el-form-item :label="$t('room.form.label_room_rent_price')">
        <el-input
          v-model="roomDetails.rentPrice"
          :formatter="(value) => mixinMethods.formatInputCurrency(value)"
          :placeholder="$t('room.form.placeholder_room_rent_price')"
          :disabled="isDisabled"
        ></el-input>
        <p v-if="validation.value.rentPrice" class="error-feedback">
          {{
            $t(
              validation.value.rentPrice.code,
              validation.value.rentPrice.options
            )
          }}
        </p>
      </el-form-item>
      <el-form-item :label="$t('room.form.label_room_landlord')">
        <SingleOptionSelect
          v-model="roomDetails.landlordId"
          :listData="landlords"
          :placeholder="$t('room.form.room_landlord_placeholder')"
          :isRemote="false"
          :disabled="!isCreate"
        />
        <p v-if="validation.value.landlordId" class="error-feedback">
          {{
            $t(
              validation.value.landlordId.code,
              validation.value.landlordId.options
            )
          }}
        </p>
      </el-form-item>
      <el-form-item :label="$t('room.form.label_room_status')">
        <SingleOptionSelect
          v-model="roomDetails.status"
          :listData="listStatus"
          :placeholder="$t('room.status_placeholder')"
          :isRemote="true"
          :disabled="isDisabled || !isCreate"
        />
        <p v-if="validation.value.status" class="error-feedback">
          {{
            $t(validation.value.status.code, validation.value.status.options)
          }}
        </p>
      </el-form-item>
      <el-form-item :label="$t('room.form.label_room_utilities')">
        <MultipleOptionSelect
          v-model="roomDetails.utilities"
          :placeholder="$t('room.form.room_utilities_placeholder')"
          :list-data="listUtilities"
          :showClearable="true"
          :isRemote="true"
          :disabled="isDisabled"
        />
        <p v-if="validation.value.utilities" class="error-feedback">
          {{
            $t(
              validation.value.utilities.code,
              validation.value.utilities.options
            )
          }}
        </p>
      </el-form-item>
    </el-form>
    <div v-if="!isDisabled" class="room-details-buttons">
      <el-button class="btn btn-save" @click="$emit('save', true)">{{
        $t("common.save")
      }}</el-button>
      <el-button class="btn btn-refuse" @click="$emit('save', false)">{{
        $t("common.cancel")
      }}</el-button>
    </div>
  </el-card>
</template>
<script>
import SingleOptionSelect from "@/components/common/SingleOptionSelect.vue";
import MultipleOptionSelect from "@/components/common/MultipleOptionSelect.vue";
import { mixinMethods } from "@/utils/variables";

export default {
  components: {
    SingleOptionSelect,
    MultipleOptionSelect,
  },
  props: {
    roomDetails: {
      type: Object,
      default: {},
    },
    validation: {
      type: Object,
      default: {},
    },
    listUtilities: {
      type: Array,
      default: [],
    },
    listStatus: {
      type: Array,
      default: [],
    },
    landlords: {
      type: Array,
      default: [],
    },
    isDisabled: {
      type: Boolean,
      default:false
    },
    isCreate: {
      type: Boolean,
      default:false
    }
  },
  setup() {
    return {
      mixinMethods
    }
  }
};
</script>
<style lang="scss" scoped>
.room-details-header {
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

.room-details-buttons {
  width: 100%;
  margin: 24px 0;
  display: flex;
  justify-content: end;
}
</style>
