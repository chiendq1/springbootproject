<template>
  <Modal :show="isShowModal" @close="close" class="modal-delete">
    <template #body>
      <div class="content text-center">
        <div class="modal__img">
          <IconWarning />
        </div>
        <h3 class="title">{{ title || $t("common.modal_title_delete") }}</h3>
        <p class="text">{{ message || $t("common.modal_text_delete") }}</p>
      </div>
    </template>
    <template #footer>
      <div class="button-wrap">
        <el-button
          color="#fff"
          :class="className || 'btn-modal-delete'"
          class="btn"
          plain
          @click="confirmDelete"
          >{{ actions || $t("common.delete") }}</el-button
        >
        <el-button :color="color" plain @click="close">{{
          $t("common.cancel")
        }}</el-button>
      </div>
    </template>
  </Modal>
</template>
<script>
import Modal from "@/components/common/Modal.vue";
import IconWarning from "@/svg/IconWarning.vue";
import { COLOR_BLUE_6 } from "@/constants/color";

export default {
  components: {
    Modal,
    IconWarning,
  },
  props: {
    isShowModal: {
      type: Boolean,
      default: () => false,
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
    actions: {
      type: String,
      default: "",
    },
    color: {
      type: String,
      default: COLOR_BLUE_6,
    },
    className: {
      type: String,
      default: "",
    },
  },

  setup(props, { emit }) {
    const close = (value) => {
      document.body.classList.remove("overflow-hidden");
      emit("closeModal", value);
    };

    const confirmDelete = () => {
      emit("deleteItem", props.id);
    };

    return {
      close,
      confirmDelete,
    };
  },
};
</script>

<style lang="scss">
.btn-modal-delete {
  border: 1px solid #b72a2a !important;
  background: #ec5252 !important;
  padding: 8px 30px !important;
  line-height: 4px !important;
  font-weight: 700;
  border-radius: 4px;
}

.modal-delete {
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
    .btn-modal-delete {
      height: 32px;
      max-width: 81px;
    }
  }

  .modal__img {
    cursor: pointer;
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

.modal-delete {
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
