<template>
  <div class="slider-wrapper">
    <span>{{ label }}</span>
    <el-slider
      v-model="internalValue"
      :range="isRange"
      :min="min"
      :max="max"
      :step="step"
      :show-stops="showStops"
      :disabled="disabled"
    />
  </div>
</template>

<script>
import { ref, watch } from "vue";

export default {
  name: "CommonSlider",
  props: {
    modelValue: {
      type: [Number, Array],
      default: 0,
    },
    label: {
      type: String,
      default: "",
    },
    isRange: {
      type: Boolean,
      default: false,
    },
    min: {
      type: Number,
      default: 0,
    },
    max: {
      type: Number,
      default: 100,
    },
    step: {
      type: Number,
      default: 1,
    },
    showStops: {
      type: Boolean,
      default: false,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  setup(props, { emit }) {
    // Create a local reactive value to hold the slider value
    const internalValue = ref(props.modelValue);

    // Watch for changes in the prop value and update the internal value accordingly
    watch(
      () => props.modelValue,
      (newValue) => {
        internalValue.value = newValue;
      }
    );

    // Watch for changes in the internal value and emit updates to the parent component
    watch(internalValue, (newValue) => {
      emit("update:modelValue", newValue);
    });

    return {
      internalValue, // Bind this to the slider
    };
  },
};
</script>

<style scoped>
.slider-wrapper {
  max-width: 600px;
  margin: 20px auto;
}
</style>
