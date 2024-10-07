import { defineStore } from "pinia";
import { mixinMethods, $services, $globalLocale } from "@/utils/variables";
import { reactive } from "vue";
import { EN_LOCALE } from "@/constants/application.js";
import { BILL_STATUS_UNPAID } from "@/constants/bill.js";
import {
  CONTRACT_STATUS_ACTIVE,
  CONTRACT_TYPE_MONTHLY,
  CONTRACT_TYPE_QUATER,
  CONTRACT_TYPE_HALF_YEAR,
  CONTRACT_TYPE_YEARLY,
} from "@/constants/contract.js";
import { useI18n } from "vue-i18n";
import PAGE_NAME from "@/constants/route-name.js";
import { useRouter, useRoute } from "vue-router";
import { useRoomStore } from "@/store/rooms.js";

export const useBillStore = defineStore("bill", () => {
  const { t } = useI18n();
  const router = useRouter();
  const route = useRoute();
  const currentLanguage = localStorage.getItem('CurrentLanguage');
  const roomStore = useRoomStore();
  const { setRoomDetails, roomDetails } = roomStore;
  const validation = reactive({ value: {} });
  const listBills = reactive({ value: [] });
  const totalItems = reactive({ value: 0 });
  const currentPage = reactive({ value: 0 });
  const listStatuses = reactive({
    value: [
      {
        id: 0,
        value: t("bill.statuses.paid"),
      },
      {
        id: 1,
        value: t("bill.statuses.unpaid"),
      },
      {
        id: 2,
        value: t("bill.statuses.terminated"),
      },
    ],
  });
  const billDetails = reactive({
    value: {
      id: 0,
      billCode: "",
      date: new Date(),
      room: {
        roomId: "",
        roomCode: "",
        tenants: [],
      },
      landlord: {
        fullName: "",
      },
      status: BILL_STATUS_UNPAID,
      details: [],
      rentPrice: 0,
    },
  });

  const getListBills = async (searchForm) => {
    mixinMethods.startLoading();
    await $services.BillAPI.index(
      { ...searchForm },
      (response) => {
        if (currentPage.value === 0) {
          listBills.value = response.data.bills;
        } else {
          listBills.value = [...listBills.value, ...response.data.bills];
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

  const getBillDetails = async (id) => {
    mixinMethods.startLoading();
    await $services.BillAPI.show(
      id,
      (response) => {
        setRoomDetails(response.data.bill.room);
        setBillDetails(roomDetails, false, response.data.bill);
        router.push({ name: PAGE_NAME.BILL.DETAILS, params: { id: id } });
        mixinMethods.endLoading();
      },
      (error) => {
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.endLoading();
        mixinMethods.notifyError(t("response.message.get_bill_details_failed"));
      }
    );
  };

  const getBillPdf = async (id) => {
    mixinMethods.startLoading();
    await $services.BillAPI.getBillPdf(
      {
        billId: id,
        language: $globalLocale.value._value,
      },
      (response) => {
        const blob = new Blob([response], { type: "application/pdf" });
        const url = window.URL.createObjectURL(blob);

        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `bill_${id}.pdf`);
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

  const createNewBill = async () => {
    mixinMethods.startLoading();
    await $services.BillAPI.create(
      {
        billCode: billDetails.value.billCode,
        roomId: billDetails.value.room.roomId,
        details: billDetails.value.details,
        language: currentLanguage,
      },
      (response) => {
        validation.value = {};
        resetBillDetails();
        router.push({ name: PAGE_NAME.BILL.LIST });
        mixinMethods.notifySuccess(t("response.message.create_bill_success"));
        mixinMethods.endLoading();
      },
      (error) => {
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.endLoading();
        mixinMethods.notifyError(t("response.message.create_bill_failed"));
      }
    );
  };

  const updateBill = async (id) => {
    mixinMethods.startLoading();
    await $services.BillAPI.update(
      id,
      {
        status: billDetails.value.status
      },
      (response) => {
        validation.value = {};
        mixinMethods.notifySuccess(t("response.message.update_bill_success"));
        mixinMethods.endLoading();
      },
      (error) => {
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.endLoading();
        mixinMethods.notifyError(t("response.message.update_bill_failed"));
      }
    );
  };

  const resetBillDetails = async () => {
    billDetails.value.id = 0;
    billDetails.value.billCode = "";
    billDetails.value.date = new Date();
    billDetails.value.room.roomId = "";
    billDetails.value.room.roomCode = "";
    billDetails.value.room.tenants = [];
    billDetails.value.landlord.fullName = "";
    billDetails.value.status = BILL_STATUS_UNPAID;
    billDetails.value.details = [];
    billDetails.value.rentPrice = 0;
  };

  const setBillDetails = async (
    roomDetails,
    isCreate = false,
    billDetailsResponse = null
  ) => {
    billDetails.value.billCode = isCreate ? "" : billDetailsResponse.billCode;
    billDetails.value.room.roomCode = roomDetails.value.roomCode;
    billDetails.value.room.roomId = roomDetails.value.roomId;
    billDetails.value.status = isCreate
      ? BILL_STATUS_UNPAID
      : billDetailsResponse.paymentStatus;
    billDetails.value.room.tenants = roomDetails.value.tenants;
    billDetails.value.landlord.fullName = roomDetails.value.landlordName;
    let contracts = roomDetails.value.contracts.find(contract => contract.status == CONTRACT_STATUS_ACTIVE);
    billDetails.value.details = roomDetails.value.utilityDetails.map(
      (utility) => {
        let billAmount =
          billDetailsResponse?.billDetails.find(
            (bill) => bill.utility.id == utility.id
          ).amount ?? 0;
        let utilUnitPrice = contracts.contractDetails.find(contract => contract.utilityId == utility.id).unitPrice;
        return {
          utilityId: utility.id,
          name: currentLanguage == EN_LOCALE ? utility.enName : utility.jaName,
          amount: isCreate ? 0 : billAmount,
          unitPrice: utilUnitPrice,
          unit: utility.unit,
          price: isCreate ? 0 : billAmount * utility.unitPrice,
        };
      }
    );
    billDetails.value.rentPrice = calculateRentPrice(
      roomDetails.value.contracts,
      roomDetails.value.rentPrice,
      roomDetails.value.bills,
      billDetailsResponse
    );
  };

  const calculateRentPrice = (contracts, rentPrice, bills, billDetailsResponse) => {
    const currentDate = new Date();
    const currentContract = contracts.find(
      (contract) => contract.status === CONTRACT_STATUS_ACTIVE
    );
    const isExistBill = bills.some((bill) => {
      let billDate = new Date(bill.date);
      let currentBillDate = new Date(billDetailsResponse?.date);
      
      return (
        bill.id != billDetailsResponse?.id &&
        billDate.getFullYear() == currentBillDate.getFullYear() &&
        billDate.getMonth() == currentBillDate.getMonth() &&
        billDate.getDate() < currentBillDate.getDate()
      );
    });
    if (!isExistBill && currentContract) {
      const contractStartDate = new Date(currentContract.startDate);
      const monthsDifference = getMonthsDifference(
        contractStartDate,
        currentDate
      );
      switch (currentContract.type) {
        case CONTRACT_TYPE_MONTHLY:
          return rentPrice;

        case CONTRACT_TYPE_QUATER:
          if (monthsDifference % 3 === 0) {
            return rentPrice * 3;
          }
          break;

        case CONTRACT_TYPE_HALF_YEAR:
          if (monthsDifference % 6 === 0) {
            return rentPrice * 6;
          }
          break;

        case CONTRACT_TYPE_YEARLY:
          if (monthsDifference % 12 === 0) {
            return rentPrice * 12;
          }
          break;

        default:
          return 0;
      }
    }

    return 0;
  };

  const getMonthsDifference = (startDate, currentDate) => {
    return (
      (currentDate.getFullYear() - startDate.getFullYear()) * 12 +
      currentDate.getMonth() -
      startDate.getMonth()
    );
  };

  return {
    validation,
    totalItems,
    listBills,
    billDetails,
    listStatuses,
    currentPage,
    getListBills,
    getBillDetails,
    getBillPdf,
    createNewBill,
    updateBill,
    resetBillDetails,
    setBillDetails,
  };
});
