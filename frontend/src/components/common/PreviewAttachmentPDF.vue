<template>
  <Modal
    :show="isShowModal"
    class="modal-preview-pdf"
    @close="close"
  >
    <template #body>
      <div v-if="attachment" class="preview-content">
        <iframe
          :src="attachment"
          frameborder="0"
        >
        </iframe>
      </div>
    </template>
  </Modal>
</template>

<script>
import Modal from "@/components/common/Modal.vue";

export default {
  components: {
    Modal,
  },
  props: {
    isShowModal: {
      type: Boolean,
      default: () => false,
    },
    attachment: {
      type: String,
      default: "",
    },
  },
  setup(_, { emit }) {
    const close = () => {
      emit("closeModal", true);
    };

    return {
      close,
    };
  },
};
</script>

<style lang="scss">
  .modal-preview-pdf {
    .modal-container {
      width: 80%;
      .modal-body {
        width: 100%;
  
        .preview-content {
          width: 100%;
  
          iframe {
            width: 100%;
            min-height: 80vh;
            border: none;
          }
        }
      }
    }
  }
</style>
