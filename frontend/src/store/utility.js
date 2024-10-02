import { defineStore } from "pinia";
import { mixinMethods, $services } from "@/utils/variables";
import { reactive } from "vue";
import Cookies from "js-cookie";
import { EN_LOCALE } from "@/constants/application.js";

export const useUtilityStore = defineStore("utility", () => {
  const listUtilities = reactive({ value: [] });
  const currentLanguage = Cookies.get("CurrentLanguage");

  const getListAllUtilities = async () => {
    await $services.UtilityAPI.index(
      {},
      (response) => {
        listUtilities.value = response.data.utilities.map((utility) => {
          return {
            id: utility.id,
            unitPrice: utility.unitPrice,
            value:
              currentLanguage == EN_LOCALE ? utility.enName : utility.jaName,
          };
        });
      },
      () => {}
    );
  };

  return {
    listUtilities,
    getListAllUtilities,
  };
});
