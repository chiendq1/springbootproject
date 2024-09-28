<template>
  <Transition name="modal">
    <div
      v-if="show"
      class="modal-mask"
      :id="target"
      :style="modalContainerStyle"
    >
      <div class="modal-container" v-click-outside="handleClickOutside">
        <div class="modal-header" v-if="isShowHeader">
          <slot name="header"></slot>
          <IconCircleClose @click="$emit('close', true)" />
        </div>
        <div class="modal-body">
          <slot name="body"></slot>
        </div>

        <div class="modal-footer" v-if="isShowFooter">
          <slot name="footer"></slot>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script>
import { computed } from "vue";
import IconCircleClose from "@/svg/IconCircleClose.vue";
import vClickOutside from "click-outside-vue3";

export default {
  components: { IconCircleClose },
  props: {
    show: {
      type: Boolean,
      default: false,
    },
    modalTitle: {
      type: String,
      default: "Modal Title",
    },
    width: {
      type: [Number, String],
      default: "fit-content",
    },
    height: {
      type: [Number, String],
      default: "fit-content",
    },
    target: {
      type: String,
      default: "body",
    },
    form: {
      type: Object,
      default: () => {},
    },
    canClickOutside: {
      type: Boolean,
      default: true,
    },
    isShowHeader: {
      type: Boolean,
      default: true,
    },
    isShowFooter: {
      type: Boolean,
      default: true,
    },
  },

  directives: {
    clickOutside: vClickOutside.directive,
  },

  setup(props, { emit }) {
    const modalContainerStyle = computed(() => {
      return {
        "--width": props.width,
        "--height": props.height,
      };
    });

    const handleClickOutside = async (event) => {
      if (
        event.target.getAttribute("id") === props.target &&
        props.canClickOutside
      ) {
        emit("close", true);
      }
    };

    return {
      modalContainerStyle,
      handleClickOutside,
    };
  },
};
</script>

<style lang="scss" scoped>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  transition: opacity 0.3s ease;
}

.modal-container {
  width: var(--width);
  margin: auto;
  max-height: 90vh;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
  transition: all 0.3s ease;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: grid;
  grid-template-rows: 1fr;
}

.modal-header {
  border-bottom: 1px solid #ccc;
  width: 100%;
  top: 0;
  left: 0;
  display: flex;

  svg {
    cursor: pointer;
    position: absolute;
    top: 5%;
    right: 1%;
    z-index: 10;
  }
}

.modal-header h5 {
  margin: 0;
  font-weight: 600;
  font-size: 24px;
  line-height: 32px;
  margin-bottom: 0;
}

.modal-body {
  overflow-y: hidden;
  height: 100%;
  padding: 16px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
}

.modal-default-button {
  float: right;
}

.modal-enter-from {
  opacity: 0;
}

.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-container,
.modal-leave-to .modal-container {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}
</style>
