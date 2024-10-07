<template>
  <Modal
    :show="show"
    :isShowHeader="true"
    :width="'60%'"
    :isShowFooter="false"
    @close="$emit('close')"
  >
    <template #header>
      <h5 class="header-title">
        {{ title }}
      </h5>
    </template>
    <template #body>
      <div class="modal-body">
        <el-form :model="contractDetails" label-width="18%">
          <el-form-item :label="$t('contract.create.contract_name')">
            <el-input
              v-model="contractDetails.contractName"
              :disabled="isViewDetails"
            />
            <p v-if="validation.value.contractName" class="error-feedback">
              {{
                $t(
                  validation.value.contractName.code,
                  validation.value.contractName.options
                )
              }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('contract.create.duration')">
            <div class="date-picker">
              <el-date-picker
                style="width: 100%"
                v-model="rangeDate.value"
                type="daterange"
                :disabled="isViewDetails"
                range-separator="To"
                :start-placeholder="$t('common.start_date')"
                :end-placeholder="$t('common.end_date')"
                :size="default"
                @change="handleChangeDate"
              />
            </div>
            <p
              v-if="validation.value.createContractRequest"
              class="error-feedback"
            >
              {{
                $t(
                  validation.value.createContractRequest.code,
                  validation.value.createContractRequest.options
                )
              }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('contract.create.deposit')">
            <el-input
              v-model="contractDetails.deposit"
              :formatter="(value) => mixinMethods.formatInputCurrency(value)"
              :disabled="isViewDetails"
            />
            <p v-if="validation.value.deposit" class="error-feedback">
              {{
                $t(
                  validation.value.deposit.code,
                  validation.value.deposit.options
                )
              }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('contract.create.type')">
            <SingleOptionSelect
              v-model="contractDetails.type"
              :listData="listTypes"
              :isDisabled="isViewDetails"
              :placeholder="$t('contract.type_placeholder')"
              :isRemote="true"
            />
            <p v-if="validation.value.type" class="error-feedback">
              {{
                $t(validation.value.type.code, validation.value.type.options)
              }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('contract.create.room')">
            <SingleOptionSelect
              v-model="contractDetails.roomId"
              :isDisabled="isViewDetails"
              :listData="listRoomsByRole"
              :placeholder="$t('contract.room_placeholder')"
              :isRemote="true"
            />
            <p v-if="validation.value.roomId" class="error-feedback">
              {{
                $t(
                  validation.value.roomId.code,
                  validation.value.roomId.options
                )
              }}
            </p>
          </el-form-item>

          <el-form-item v-if="!isViewDetails" :label="$t('contract.create.tenants')">
            <MultipleOptionSelect
              v-model="contractDetails.tenants"
              :list-data="listTenants"
              :isDisabled="isViewDetails"
              :showClearable="true"
              :placeholder="$t('contract.tenant_placeholder')"
              :isRemote="true"
            />
            <p v-if="validation.value.tenants" class="error-feedback">
              {{
                $t(
                  validation.value.tenants.code,
                  validation.value.tenants.options
                )
              }}
            </p>
          </el-form-item>
        </el-form>
      </div>

      <div class="modal-footer">
        <div v-if="!isViewDetails">
          <el-button
            class="btn btn-save"
            v-if="highestRole == ADMIN || highestRole == LANDLORD"
            @click="handleSubmit"
            >{{ $t("common.save") }}</el-button
          >
          <el-button class="btn btn-refuse" @click="$emit('close')">{{
            $t("common.cancel")
          }}</el-button>
        </div>
        <div v-if="isViewDetails && contractDetails.status != CONTRACT_STATUS_TERMINATED">
          <el-button
            class="btn btn-save"
            v-if="highestRole == ADMIN || highestRole == LANDLORD"
            @click="$emit('extend')"
            >{{ $t("common.extend") }}</el-button
          >
          <el-button
            class="btn btn-refuse"
            v-if="highestRole == ADMIN || highestRole == LANDLORD"
            @click="$emit('terminate')"
            >{{ $t("common.terminate") }}</el-button
          >
        </div>
      </div>
    </template>
  </Modal>
</template>

<script>
import { mixinMethods } from "@/utils/variables";
import Modal from "@/components/common/Modal.vue";
import SingleOptionSelect from "@/components/common/SingleOptionSelect.vue";
import MultipleOptionSelect from "@/components/common/MultipleOptionSelect.vue";
import { ADMIN, LANDLORD } from "@/constants/roles.js";
import { CONTRACT_STATUS_TERMINATED } from "@/constants/contract.js";

export default {
  name: "ContractSaveModal",
  components: {
    Modal,
    SingleOptionSelect,
    MultipleOptionSelect,
  },
  props: {
    show: Boolean,
    contractDetails: {
      type: Object,
      default: {
        contractName: "",
        startDate: "",
        endDate: "",
        deposit: 0,
        type: 0,
        roomId: 0,
        tenants: [],
        status: 0,
      },
    },
    validation: {
      type: Object,
      default: {},
    },
    listTypes: {
      type: Array,
      default: [],
    },
    listRoomsByRole: {
      type: Array,
      default: [],
    },
    listTenants: {
      type: Array,
      default: [],
    },
    title: {
      type: String,
      default: "",
    },
    highestRole: {
      type: String,
      default: "",
    },
    isViewDetails: {
      type: Boolean,
      default: false,
    },
    rangeDate: {
      type: Array,
      default: () => [],
    },
  },
  setup(props, { emit }) {
    const handleSubmit = () => {
      emit("submit");
    };

    const handleChangeDate = (date) => {
      props.contractDetails.startDate = mixinMethods.showDateTime(date[0]);
      props.contractDetails.endDate = mixinMethods.showDateTime(date[1]);
    };

    return {
      ADMIN,
      LANDLORD,
      CONTRACT_STATUS_TERMINATED,
      handleChangeDate,
      handleSubmit,
      mixinMethods,
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
.modal-container {
  width: 500px;
  background-color: white;
  border-radius: 4px;
  padding: 20px;
}
.date-picker {
  display: block;
  width: 100%;
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.modal-footer {
  padding: 0;
  display: flex;
  justify-content: flex-end;
}
.header-title {
  margin: 0;
}
</style>
