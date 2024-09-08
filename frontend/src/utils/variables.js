import services from '@/plugins/services';
import PAGES from '@/utils/pages.js';
import { EN_LOCALE } from "@/constants/application";
import { ref } from 'vue';
import Cookies from 'js-cookie';
import mixins from '@/helpers/mixins';
import { ElNotification } from 'element-plus'

const defaultLocale = Cookies.get('CurrentLanguage') || EN_LOCALE;
const refDefaultLocale = ref(defaultLocale);
const globalLocale = {
  value: refDefaultLocale,
  update(locale) {
    refDefaultLocale.value = locale;
  }
};

export const mixinMethods = mixins.methods;
export const $notify = ElNotification;
export const $services = services;
export const $PAGES = PAGES;
export const $globalLocale = globalLocale;

export default {
  $services: services,
  $PAGES: PAGES,
  $globalLocale: globalLocale,
};
