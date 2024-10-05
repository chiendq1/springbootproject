import { createI18n } from "vue-i18n";
import en from "@/locales/en.json";
import ja from "@/locales/ja.json";
import { EN_LOCALE } from "@/constants/application";
import { computed } from "vue";
import { $globalLocale } from "../utils/variables";

export const i18n = createI18n({
  legacy: false,
  locale: computed(() => $globalLocale.value || EN_LOCALE),
  fallbackLocale: EN_LOCALE,
  messages: { en, ja },
  globalInjection: true,
});
