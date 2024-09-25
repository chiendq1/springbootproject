import { defineStore } from "pinia";
import Cookies from "js-cookie";
import { mixinMethods, $services } from "@/utils/variables";
import { reactive } from "vue";
import { EN_LOCALE } from "@/constants/application.js";
import { useI18n } from "vue-i18n";
import PAGE_NAME from "@/constants/route-name.js";
import { useRouter } from "vue-router";

export const useContractStore = defineStore("contract", () => {
  const currentLanguage = Cookies.get("CurrentLanguage");
  const { t } = useI18n();
  const validation = reactive({ value: {} });
  const listContracts = reactive({ value: [] });
  const totalItems = reactive({ value: 0 });
  const currentPage = reactive({ value: 0 });
  const isCreate = reactive({ value: false });
  const router = useRouter();

  const getListContracts = async (searchForm) => {
    mixinMethods.startLoading();
    await $services.ContractAPI.index(
      searchForm,
      (response) => {
        if (currentPage.value === 0) {
          listContracts.value = response.data.contracts;
        } else {
          listContracts.value = [...listContracts.value, ...response.data.contracts];
        }
        totalItems.value = response.data.totalItems;
        currentPage.value = response.data.currentPage;

        mixinMethods.endLoading();
      },
      () => {
        mixinMethods.endLoading();
      }
    );
  };

  return {
    validation,
    totalItems,
    currentPage,
    isCreate,
    listContracts,
    getListContracts,
  };
});
