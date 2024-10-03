<template>
  <Modal
    :show="show"
    :width="'60%'"
    :isShowFooter="false"
    @close="$emit('close')"
  >
    <template #header>
      <h4 class="header-title">
        {{ $t('utility.modal.header') }}
      </h4>
    </template>
    <template #body>
      <div class="modal-body">
        <el-form :model="utilityDetails" label-width="18%">
          <el-form-item :label="$t('utility.modal.name')">
            <el-input v-model="utilityDetails.name" />
            <p v-if="validate.value.name" class="error-feedback">
              {{ $t(validate.value.name.code, validate.value.name.options) }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('utility.modal.unit_price')">
            <el-input v-model="utilityDetails.unitPrice" />
            <p v-if="validate.value.unitPrice" class="error-feedback">
              {{
                $t(
                  validate.value.unitPrice.code,
                  validate.value.unitPrice.options
                )
              }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('utility.modal.unit')">
            <el-input v-model="utilityDetails.unit" />
            <p v-if="validate.value.unit" class="error-feedback">
              {{ $t(validate.value.unit.code, validate.value.unit.options) }}
            </p>
          </el-form-item>
        </el-form>
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
import { useI18n } from "vue-i18n";
import Modal from "@/components/common/Modal.vue";

export default {
  name: "UtilityModal",
  components: { IconCircleClose, Modal },
  props: {
    show: Boolean,
    utilityDetails: {
      type: Object,
      default: {
        id: 0,
        name: "",
        unitPrice: 0,
        unit: "",
      },
    },
    validate: {
      type: Object,
      default: {},
    },
  },
  setup(props, { emit }) {
    const { t } = useI18n();
    const handleSubmit = () => {
      emit("submit");
    };

    return {
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
.header-title {
  margin: 0;
}
</style>
