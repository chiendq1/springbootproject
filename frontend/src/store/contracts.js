import { defineStore } from "pinia";
import Cookies from "js-cookie";
import { mixinMethods, $services, $globalLocale } from "@/utils/variables";
import { reactive } from "vue";
import { EN_LOCALE } from "@/constants/application.js";
import { useI18n } from "vue-i18n";
import PAGE_NAME from "@/constants/route-name.js";
import { useRouter } from "vue-router";

export const useContractStore = defineStore("contract", () => {
  const { t } = useI18n();
  const validation = reactive({ value: {} });
  const listContracts = reactive({ value: [] });
  const totalItems = reactive({ value: 0 });
  const currentPage = reactive({ value: 0 });
  const isCreate = reactive({ value: false });
  const router = useRouter();
  const isShowModalSave = reactive({value: false});
  const contractDetails = reactive({value: {
    contractName: "",
    startDate: "",
    endDate: "",
    deposit: 0,
    type: 0,
    roomId: "",
    tenants: [],
  }});
  const listTypes = reactive({value: [
    {
      id: 0,
      value: t("contract.types.monthly"),
    },
    {
      id: 1,
      value: t("contract.types.quater"),
    },
    {
      id: 2,
      value: t("contract.types.half-year"),
    },
    {
      id: 3,
      value: t("contract.types.year"),
    },
  ]});

  const getListContracts = async (searchForm) => {
    mixinMethods.startLoading();
    await $services.ContractAPI.index(
      { ...searchForm },
      (response) => {
        if (currentPage.value === 0) {
          listContracts.value = response.data.contracts;
        } else {
          listContracts.value = [
            ...listContracts.value,
            ...response.data.contracts,
          ];
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

  const getContractPdf = async (id) => {
    mixinMethods.startLoading();
    await $services.ContractAPI.getContractPdf(
      {
        contractId: id,
        language: $globalLocale.value._value
      }, (response) => {
        const blob = new Blob([response], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(blob);
        
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `contract_${id}.pdf`);
        document.body.appendChild(link);
        link.click();
        link.remove();
        window.URL.revokeObjectURL(url);
        mixinMethods.endLoading();
      }, (err) => {
        mixinMethods.endLoading();
      }
    );
      
  };

  const createNewContract = async () => {
    mixinMethods.startLoading();
    await $services.ContractAPI.create(
      { ...contractDetails.value },
      (response) => {
        listContracts.value.push(response.data.contract);
        isShowModalSave.value = false;
        validation.value = [];
        resetContractDetails();
        mixinMethods.notifySuccess(t("response.message.create_contract_success"));
        mixinMethods.endLoading();
      },
      (error) => {
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.notifyError(t("response.message.create_contract_failed"));
        mixinMethods.endLoading();
      }
    );
  }

  const resetContractDetails = async () => {
    contractDetails.value.contractName = "";
    contractDetails.value.startDate = "";
    contractDetails.value.endDate = "";
    contractDetails.value.deposit = 0;
    contractDetails.value.type = 0;
    contractDetails.value.roomId = "";
    contractDetails.value.tenants = [];
  }

  return {
    validation,
    totalItems,
    currentPage,
    isShowModalSave,
    isCreate,
    listContracts,
    listTypes,
    contractDetails,
    getListContracts,
    resetContractDetails,
    createNewContract,
    getContractPdf,
  };
});
