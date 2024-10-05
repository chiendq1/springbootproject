import services from '@/plugins/services';
import PAGES from '@/utils/pages.js';
import { EN_LOCALE, DEFAULT_EXCHANGE_RATE } from "@/constants/application";
import { ref } from 'vue';
import mixins from '@/helpers/mixins';
import { ElNotification } from 'element-plus'

const defaultLocale = localStorage.getItem('CurrentLanguage') || EN_LOCALE;
const defaultExchangeRate = localStorage.getItem('CurrentExchangeRate') || DEFAULT_EXCHANGE_RATE;
const refDefaultLocale = ref(defaultLocale);
const refExchangeRate = ref(defaultExchangeRate);
const globalExhangeRate = {
  value: refExchangeRate,
  update(exchangeRate) {
    refExchangeRate.value = exchangeRate;
  }
};
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
export const $exchangeRate = globalExhangeRate;

export default {
  $services: services,
  $PAGES: PAGES,
  $globalLocale: globalLocale,
  $exchangeRate: globalExhangeRate,
};
