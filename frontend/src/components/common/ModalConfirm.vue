<template>
  <Modal :show="isShowModal" @close="close" class="modal-confirm">
    <template #body>
      <div class="content text-center">
        <div class="modal__img">
          <IconConfirm />
        </div>
        <h3 class="title">{{ title || $t("common.modal_title_confirm") }}</h3>
        <p class="text">{{ message || $t("common.modal_text_confirm") }}</p>
      </div>
      <div>
        <el-input
          v-if="isConfirmByText"
          :placeholder="placeholder || $t('common.confirm_placeholder')"
          v-model="inputConfirm"
          type="text"
          @keyup.enter="confirmAction"
        ></el-input>
      </div>
      <label class="error-text-confirm">{{ validation }}</label>
    </template>
    <template #footer>
      <div class="button-wrap">
        <el-button
          :color="confirmBtnColor"
          class="btn btn-modal-confirm"
          plain
          @click="confirmAction"
          >{{ $t("common.confirm") }}</el-button
        >
        <el-button
          class="btn btn-cancel"
          :color="cancelBtnColor"
          plain
          @click="close"
          >{{ $t("common.cancel") }}</el-button
        >
      </div>
    </template>
  </Modal>
</template>
<script>
import Modal from "@/components/common/Modal.vue";
import IconConfirm from "@/svg/IconConfirm.vue";
import { ref } from "vue";
import { useI18n } from "vue-i18n";

export default {
  components: {
    Modal,
    IconConfirm,
  },
  props: {
    isShowModal: {
      type: Boolean,
      default: () => false,
    },
    isConfirmByText: {
      type: Boolean,
      default: false,
    },
    confirmText: {
      type: String,
      default: "",
    },
    id: {
      type: [Number, String],
      default: null,
    },
    message: {
      type: String,
      default: "",
    },
    title: {
      type: String,
      default: "",
    },
    confirmBtnColor: {
      type: String,
      default: "#FFF",
    },
    cancelBtnColor: {
      type: String,
      default: "#3B5A9A",
    },
    placeholder: {
      type: String,
      default: "",
    },
  },
  setup(props, { emit }) {
    const { t } = useI18n();
    const inputConfirm = ref("");
    const validation = ref("");
    const close = (value) => {
      if (props.isConfirmByText) {
        inputConfirm.value = "";
        validation.value = "";
      }

      emit("closeModal", value);
    };

    const confirmAction = () => {
      if (props.isConfirmByText) {
        let inputConfirmText = inputConfirm.value.trim().toLowerCase();
        if (inputConfirmText !== props.confirmText) {
          validation.value = t("E-CT-8090");

          return;
        }

        emit("confirmAction", inputConfirmText);
        inputConfirm.value = "";
        validation.value = "";

        return;
      }
      emit("confirmAction", props.id);
    };

    return {
      inputConfirm,
      close,
      confirmAction,
      validation,
    };
  },
};
</script>

<style lang="scss">
.btn-modal-confirm {
  border: 1px solid #5a6acf !important;
  background: #5a6acf !important;
  padding: 8px 30px !important;
  line-height: 4px !important;
  font-weight: 700;
  border-radius: 4px;
  color: #fff;

  &:hover {
    color: aquamarine;
  }
}

.error-text-confirm {
  display: block;
  color: red;
  text-align: left;
  font-size: 12px;
  margin-top: 5px;
}

.modal-confirm {
  .modal-header {
    border: none;
  }

  .modal-container {
    max-width: 350px;
    border-radius: 12px !important;
  }

  .modal-body {
    overflow-y: unset;
    padding: 0 25px !important;
  }

  .modal-footer {
    justify-content: center;
  }
}
</style>

<style lang="scss" scoped>
.title {
  font-size: 18px;
  font-weight: 700;
  margin: -15px 0 10px 0;
  word-break: break-word;
  letter-spacing: 0.5px;
}

.text {
  margin: 0;
  color: #212529;
  font-weight: 400;
}

.content svg {
  width: 100%;
}

.modal-confirm {
  .modal-container {
    max-width: 350px;
  }
}

.button-wrap {
  display: flex;
  justify-content: end;
}

.modal__img {
  background: #fff;
  width: max-content;
  padding: 12px;
  border-radius: 50%;
  position: absolute;
  top: -43px;
  left: 50%;
  transform: translateX(-50%);
}

.text-center {
  text-align: center;
}
</style>
