import { defineStore } from "pinia";
import { mixinMethods, $services } from "@/utils/variables";
import { reactive } from "vue";
import { EN_LOCALE } from "@/constants/application.js";
import { useI18n } from "vue-i18n";

export const useUtilityStore = defineStore("utility", () => {
  const currentLanguage = localStorage.getItem("CurrentLanguage");
  const { t } = useI18n();
  const isShowUtilityModal = reactive({ value: false });
  const showModalConfirm = reactive({ value: false });
  const listUtilities = reactive({ value: [] });
  const validation = reactive({ value: {} });
  const utilityDetails = reactive({
    value: {
      id: 0,
      name: "",
      unitPrice: 0,
      unit: "",
      status: 0,
    },
  });

  const getListAllUtilities = async (searchForm, isLoading = false) => {
    if (isLoading) mixinMethods.startLoading();
    await $services.UtilityAPI.index(
      searchForm,
      (response) => {
        listUtilities.value = response.data.utilities.map((utility) => {
          return {
            id: utility.id,
            unitPrice: utility.unitPrice,
            value:
              currentLanguage == EN_LOCALE ? utility.enName : utility.jaName,
            unit: utility.unit,
            status: utility.status,
          };
        });
        if (isLoading) mixinMethods.endLoading();
      },
      () => {
        if (isLoading) mixinMethods.endLoading();
      }
    );
  };

  const getUtilityDetails = async (id) => {
    mixinMethods.startLoading();
    await $services.UtilityAPI.show(
      id,
      {},
      (response) => {
        setUtilityDetails(response.data.utility);
        mixinMethods.endLoading();
      },
      () => {
        mixinMethods.endLoading();
      }
    );
  };

  const createNewUtility = async () => {
    mixinMethods.startLoading();
    await $services.UtilityAPI.create(
      {
        name: utilityDetails.value.name,
        unitPrice: utilityDetails.value.unitPrice,
        unit: utilityDetails.value.unit,
      },
      (response) => {
        listUtilities.value.push({
          value: EN_LOCALE
            ? response.data.utility.enName
            : response.data.utility.jaName,
          unit: response.data.utility.unit,
          id: response.data.utility.id,
          status: response.data.utility.status,
          unitPrice: response.data.utility.unitPrice,
        });
        isShowUtilityModal.value = false;
        mixinMethods.notifySuccess(
          t("response.message.create_utility_success")
        );
        mixinMethods.endLoading();
      },
      (error) => {
        mixinMethods.notifyError(t("response.message.create_utility_failed"));
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.endLoading();
      }
    );
  };

  const updateUtility = async (id) => {
    mixinMethods.startLoading();
    await $services.UtilityAPI.update(
      id,
      {
        name: utilityDetails.value.name,
        unitPrice: utilityDetails.value.unitPrice,
        unit: utilityDetails.value.unit,
      },
      (response) => {
        listUtilities.value.forEach((utility, index) => {
          if (utility.id == id) {
            listUtilities.value[index] = {
              id: response.data.utility.id,
              unitPrice: response.data.utility.unitPrice,
              value:
                currentLanguage == EN_LOCALE
                  ? response.data.utility.enName
                  : response.data.utility.jaName,
              unit: response.data.utility.unit,
              status: response.data.utility.status,
            };
          }
        });
        clearUtilityAttrs();
        isShowUtilityModal.value = false;
        mixinMethods.notifySuccess(
          t("response.message.update_utility_success")
        );
        mixinMethods.endLoading();
      },
      (error) => {
        mixinMethods.notifyError(t("response.message.update_utility_failed"));
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.endLoading();
      }
    );
  };

  const deactivateUtility = async (id) => {
    mixinMethods.startLoading();
    await $services.UtilityAPI.deactivate(
      id,
      {},
      (response) => {
        listUtilities.value.forEach((utility, index) => {
          if (utility.id == id) {
            listUtilities.value[index].status = response.data.utility.status;
          }
        });
        showModalConfirm.value = false;
        mixinMethods.notifySuccess(
          t("response.message.update_utility_success")
        );
        mixinMethods.endLoading();
      },
      (error) => {
        mixinMethods.notifyError(t("response.message.update_utility_failed"));
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.endLoading();
      }
    );
  };

  const setUtilityDetails = (utility) => {
    utilityDetails.value.name =
      currentLanguage == EN_LOCALE ? utility.enName : utility.jaName;
    utilityDetails.value.id = utility.id;
    utilityDetails.value.unitPrice = utility.unitPrice;
    utilityDetails.value.unit = utility.unit;
  };

  const clearUtilityAttrs = () => {
    utilityDetails.value.name = "";
    utilityDetails.value.id = 0;
    utilityDetails.value.unitPrice = 0;
    utilityDetails.value.unit = "";
  };

  return {
    listUtilities,
    utilityDetails,
    validation,
    showModalConfirm,
    isShowUtilityModal,
    createNewUtility,
    updateUtility,
    getListAllUtilities,
    clearUtilityAttrs,
    deactivateUtility,
    getUtilityDetails,
  };
});
