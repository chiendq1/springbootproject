<template>
  <Modal
    :show="show"
    :width="'60%'"
    :isShowFooter="false"
    @close="$emit('close')"
  >
    <template #body>
      <div class="modal-body">
        <el-form :model="userInfor" label-width="18%">
          <el-form-item :label="$t('user.user_name')">
            <el-input v-model="userInfor.username" />
            <p v-if="validate.value.username" class="error-feedback">
              {{
                $t(
                  validate.value.username.code,
                  validate.value.username.options
                )
              }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('user.user_full_name')">
            <el-input v-model="userInfor.fullName" />
            <p v-if="validate.value.fullName" class="error-feedback">
              {{
                $t(
                  validate.value.fullName.code,
                  validate.value.fullName.options
                )
              }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('user.email')">
            <el-input v-model="userInfor.email" />
            <p v-if="validate.value.email" class="error-feedback">
              {{
                $t(
                  validate.value.email.code,
                  validate.value.email.options
                )
              }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('user.phone')">
            <el-input v-model="userInfor.phoneNumber" />
            <p v-if="validate.value.phoneNumber" class="error-feedback">
              {{
                $t(
                  validate.value.phoneNumber.code,
                  validate.value.phoneNumber.options
                )
              }}
            </p>
          </el-form-item>

          <el-form-item :label="$t('user.location')">
            <el-input v-model="userInfor.location" />
          </el-form-item>

          <el-form-item :label="$t('user.role')">
            <el-select v-model="userInfor.highestRole">
              <el-option
                v-for="(role, index) in roles"
                :key="index"
                :label="role.key"
                :value="role.value"
              />
            </el-select>
            <p v-if="validate.value.highestRole" class="error-feedback">
              {{
                $t(
                  validate.value.highestRole.code,
                  validate.value.highestRole.options
                )
              }}
            </p>
          </el-form-item>
        </el-form>
      </div>

      <div class="modal-footer">
        <el-button class="btn btn-save" @click="handleSubmit">{{
          $t("common.save")
        }}</el-button>
        <el-button class="btn btn-refuse" @click="$emit('close')">{{ $t("common.cancel") }}</el-button>
      </div>
    </template>
  </Modal>
</template>

<script>
import IconCircleClose from "@/svg/IconCircleClose.vue";
import { ref } from "vue";
import { LANDLORD, TENANT } from "@/constants/roles.js";
import { useI18n } from "vue-i18n";
import Modal from "@/components/common/Modal.vue";
import { validate } from "vee-validate";

export default {
  name: "UserModal",
  components: { IconCircleClose, Modal },
  props: {
    show: Boolean,
    userInfor: {
      type: Object,
      default: {
        username: "",
        fullName: "",
        email: "",
        phoneNumber: "",
        location: "",
        highestRole: "",
      },
    },
    validate: {
      type: Object,
      default: {},
    }
  },
  setup(props, { emit }) {
    const { t } = useI18n();
    const roles = ref([
      { key: t("role.landlord"), value: LANDLORD },
      { key: t("role.tenant"), value: TENANT },
    ]);

    const handleSubmit = () => {
      emit("submit");
    };

    return {
      roles,
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
</style>
