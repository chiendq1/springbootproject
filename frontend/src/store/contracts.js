import { defineStore } from "pinia";
import { mixinMethods, $services, $globalLocale } from "@/utils/variables";
import { reactive } from "vue";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";

export const useContractStore = defineStore("contract", () => {
  const { t } = useI18n();
  const validation = reactive({ value: {} });
  const listContracts = reactive({ value: [] });
  const totalItems = reactive({ value: 0 });
  const currentPage = reactive({ value: 0 });
  const isCreate = reactive({ value: false });
  const isViewDetails = reactive({ value: false });
  const dateRangeModal = reactive({ value: [] });
  const isShowModalConfirm = reactive({value: false});
  const router = useRouter();
  const isShowModalSave = reactive({ value: false });
  const contractDetails = reactive({
    value: {
      id: 0,
      contractName: "",
      startDate: "",
      endDate: "",
      deposit: 0,
      type: 0,
      status: 0,
      roomId: "",
      tenants: [],
    },
  });
  const listTypes = reactive({
    value: [
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
    ],
  });

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

  const getContractDetails = async (id) => {
    mixinMethods.startLoading();
    await $services.ContractAPI.show(
      id,
      {},
      (response) => {
        isViewDetails.value = true;
        setContractDetails(response.data.contract);
        mixinMethods.endLoading();
      },
      () => {
        mixinMethods.notifyError(
          t("response.message.get_contract_details_failed")
        );
        mixinMethods.endLoading();
      }
    );
  };

  const getContractPdf = async (id) => {
    mixinMethods.startLoading();
    await $services.ContractAPI.getContractPdf(
      {
        contractId: id,
        language: $globalLocale.value._value,
      },
      (response) => {
        const blob = new Blob([response], { type: "application/pdf" });
        const url = window.URL.createObjectURL(blob);

        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `contract_${id}.pdf`);
        document.body.appendChild(link);
        link.click();
        link.remove();
        window.URL.revokeObjectURL(url);
        mixinMethods.endLoading();
      },
      (err) => {
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
        mixinMethods.notifySuccess(
          t("response.message.create_contract_success")
        );
        mixinMethods.endLoading();
      },
      (error) => {
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.notifyError(t("response.message.create_contract_failed"));
        mixinMethods.endLoading();
      }
    );
  };

  const updateContract = async () => {
    mixinMethods.startLoading();
    await $services.ContractAPI.update(
      contractDetails.value.id,
      {
        ...contractDetails.value
      },
      (response) => {
        isViewDetails.value = false;
        listContracts.value = listContracts.value.map(contract => {
          if (response.data.contract.id === contract.id) {
            return {
              ...contract, // Keep other contract properties unchanged
              status: response.data.contract.status, // Update the status
            };
          }
          return contract;
        });
        resetContractDetails();
        isShowModalSave.value = false;
        mixinMethods.notifySuccess(t("response.message.terminate_contract_success"));
        mixinMethods.endLoading();
      },
      () => {
        mixinMethods.notifyError(
          t("response.message.terminate_contract_failed")
        );
        mixinMethods.endLoading();
      }
    );
  }

  const resetContractDetails = async () => {
    contractDetails.value.contractName = "";
    contractDetails.value.id = 0;
    contractDetails.value.startDate = "";
    contractDetails.value.endDate = "";
    contractDetails.value.deposit = 0;
    contractDetails.value.type = 0;
    contractDetails.value.status = 0;
    contractDetails.value.roomId = "";
    contractDetails.value.tenants = [];
  };

  const setContractDetails = async (contractResponse) => {
    contractDetails.value.id = contractResponse.id;
    contractDetails.value.contractName = contractResponse.contractName;
    contractDetails.value.startDate = contractResponse.startDate;
    contractDetails.value.endDate = contractResponse.endDate;
    contractDetails.value.deposit = contractResponse.deposit;
    contractDetails.value.status = contractResponse.status;
    contractDetails.value.type = contractResponse.type;
    contractDetails.value.roomId = contractResponse.room.roomId;
    contractDetails.value.tenants = contractResponse.room.roomsTenants.map(
      (tenant) => tenant.id
    );
    dateRangeModal.value[0] = contractResponse.startDate;
    dateRangeModal.value[1] = contractResponse.endDate;
  };

  return {
    validation,
    totalItems,
    currentPage,
    isShowModalSave,
    isCreate,
    dateRangeModal,
    listContracts,
    isViewDetails,
    listTypes,
    isShowModalConfirm,
    contractDetails,
    getContractDetails,
    getListContracts,
    resetContractDetails,
    createNewContract,
    updateContract,
    getContractPdf,
  };
});
