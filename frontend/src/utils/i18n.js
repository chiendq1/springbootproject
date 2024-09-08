import { $globalLocale } from "@/utils/variables";
import { createI18n } from "vue-i18n";
import en from "@/locales/en.json";
import ja from "@/locales/ja.json";
import { EN_LOCALE } from "@/constants/application";

export const i18n = createI18n({
  locale: $globalLocale.value,
  fallbackLocale: EN_LOCALE,
  messages: { en, ja },
  legacy: false,
});
