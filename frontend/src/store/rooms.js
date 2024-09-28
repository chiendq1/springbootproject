import { defineStore } from "pinia";
import Cookies from "js-cookie";
import { mixinMethods, $services } from "@/utils/variables";
import { reactive } from "vue";
import { EN_LOCALE } from "@/constants/application.js";
import { ROOM_STATUSES } from "@/constants/room.js";
import { useI18n } from "vue-i18n";
import { useUserStore } from "@/store/users.js";
import PAGE_NAME from "@/constants/route-name.js";
import { useRouter } from "vue-router";

export const useRoomStore = defineStore("room", () => {
  const userStore = useUserStore();
  const { listLandlords } = userStore;
  const currentLanguage = Cookies.get("CurrentLanguage");
  const { t } = useI18n();
  const validation = reactive({ value: {} });
  const listRooms = reactive({ value: [] });
  const listRoomsByRole = reactive({ value: [] });
  const totalItems = reactive({ value: 0 });
  const currentPage = reactive({ value: 0 });
  const isOpenDataUsersModal = reactive({ value: false });
  const showModalConfirm = reactive({ value: false });
  const roomUsers = reactive({ value: [] });
  const allowUpdate = reactive({ value: false });
  const isShowModalConfirm = reactive({ value: false });
  const isCreate = reactive({ value: false });
  const router = useRouter();
  const roomDetails = reactive({
    value: {
      landlordId: "",
      roomCode: "",
      area: 0,
      capacity: 0,
      rentPrice: 0,
      status: "",
      utilities: [],
    },
  });
  const utilitiesConsumer = reactive({ value: [] });

  const getListRooms = async (searchForm) => {
    mixinMethods.startLoading();
    await $services.RoomAPI.index(
      searchForm,
      (response) => {
        if (currentPage.value === 0) {
          listRooms.value = response.data.rooms;
        } else {
          listRooms.value = [...listRooms.value, ...response.data.rooms];
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

  
  const getRoomDetails = async (id) => {
    mixinMethods.startLoading();
    await $services.RoomAPI.show(
      id,
      {},
      (response) => {
        const currentDate = new Date();
        setRoomDetails(response.data.roomDetails);
        setRoomUsers(response.data);
        response.data.roomDetails.bills.map((bill) => {
          const billDate = new Date(bill.date).getMonth();
          if (currentDate.getMonth() === billDate) {
            bill.billDetails.map((details) => {
              utilitiesConsumer.value.push({
                name:
                currentLanguage == EN_LOCALE
                ? details.utility.enName
                : details.utility.jaName,
                amount: details.amount,
                price: mixinMethods.formatInputCurrency(
                  details.utility.unitPrice * details.amount
                ),
              });
            });
          }
        });
        allowUpdate.value = ROOM_STATUSES[roomDetails.value.status] == "rented";
        
        mixinMethods.endLoading();
      },
      (error) => {
        mixinMethods.endLoading();
      }
    );
  };

  const getListRoomsByRole = async () => {
    await $services.RoomAPI.getListRoomsByRole(
      {},
      (response) => {
        listRoomsByRole.value = response.data.rooms.map((room) => {
          return {
            id: room.roomId,
            value: room.roomCode,
            status: room.status
          }
        })
      },() => {

      }
    );
  }
  
  const createNewRoom = async () => {
    mixinMethods.startLoading();
    await $services.RoomAPI.create(
      {
        ...roomDetails.value,
        listTenants: roomUsers.value.map((user) => user.id),
      },
      (response) => {
        router.push({ name: PAGE_NAME.ROOM.LIST });
        mixinMethods.notifySuccess(t("response.message.create_room_success"));
        mixinMethods.endLoading();
      },
      (error) => {
        mixinMethods.endLoading();
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.notifyError(t(validation.value.errors.code ?? "response.message.create_room_failed"));
      }
    );
  };

  const udpateRoomDetails = async (id) => {
    mixinMethods.startLoading();
    await $services.RoomAPI.update(
      id,
      roomDetails.value,
      (response) => {
        setRoomDetails(response.data.roomDetails);
        allowUpdate.value = ROOM_STATUSES[roomDetails.value.status] == "rented";
        validation.value = {};
        mixinMethods.notifySuccess(t("response.message.update_success"));
        mixinMethods.endLoading();
      },
      (error) => {
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.notifyError(t("response.message.update_failed"));
        mixinMethods.endLoading();
      }
    );
  };

  const addRoomTenants = async (id, listTenants) => {
    mixinMethods.startLoading();
    await $services.RoomAPI.addTenants(
      id,
      { listTenants: listTenants },
      (response) => {
        setRoomUsers(response.data);
        isOpenDataUsersModal.value = false;
        mixinMethods.notifySuccess(t("response.message.add_tenant_success"));
        mixinMethods.endLoading();
      },
      (error) => {
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.notifyError(t(validation.value.errors.code));
        mixinMethods.endLoading();
      }
    );
  };

  const deleteRoomTenant = async (id, listTenants) => {
    mixinMethods.startLoading();
    await $services.RoomAPI.deleteTenant(
      id,
      { listTenants: listTenants },
      (response) => {
        setRoomUsers(response.data);
        isShowModalConfirm.value = false;
        mixinMethods.notifySuccess(t("response.message.delete_tenant_success"));
        mixinMethods.endLoading();
      },
      (error) => {
        validation.value = mixinMethods.handleErrorResponse(error.responseCode);
        mixinMethods.notifyError(t("response.message.delete_tenant_failed"));
        mixinMethods.endLoading();
      }
    );
  };

  const deleteRoom = async (id) => {
    mixinMethods.startLoading();
    await $services.RoomAPI.deleteRoom(
      id,
      {},
      (response) => {
        listRooms.value = listRooms.value.filter(room => room.roomId != id)
        mixinMethods.notifySuccess(t("response.message.delete_room_success"));
        mixinMethods.endLoading();
      },
      (error) => {
        mixinMethods.notifyError(t("response.message.delete_room_failed"));
        mixinMethods.endLoading();
      }
    );
  };

  const setRoomDetails = (data) => {
    roomDetails.value.landlordId = data.landlord.id;
    roomDetails.value.roomCode = data.roomCode;
    roomDetails.value.area = data.area;
    roomDetails.value.capacity = data.capacity;
    roomDetails.value.rentPrice = data.rentPrice;
    roomDetails.value.status = data.status;
    roomDetails.value.utilities = [
      ...data.utilities.map((utility) => utility.id),
    ];
  };

  const resetData = () => {
    roomUsers.value = [];
    roomDetails.value.roomCode = "";
    roomDetails.value.area = "";
    roomDetails.value.capacity = "";
    roomDetails.value.rentPrice = "";
    roomDetails.value.status = "";
    roomDetails.value.utilities = [];
    utilitiesConsumer.value = [];
  };

  const setRoomUsers = (data) => {
    roomUsers.value = data.roomDetails.roomsTenants.map((tenant) => {
      return {
        id: tenant.id,
        username: tenant.username,
        fullName: tenant.fullName,
        email: tenant.email,
        phoneNumber: tenant.phoneNumber,
      };
    });
  };

  return {
    validation,
    listRooms,
    totalItems,
    currentPage,
    showModalConfirm,
    roomUsers,
    roomDetails,
    utilitiesConsumer,
    allowUpdate,
    isOpenDataUsersModal,
    isShowModalConfirm,
    isCreate,
    listLandlords,
    listRoomsByRole,
    deleteRoom,
    deleteRoomTenant,
    addRoomTenants,
    createNewRoom,
    udpateRoomDetails,
    resetData,
    getListRooms,
    getRoomDetails,
    getListRoomsByRole,
  };
});
