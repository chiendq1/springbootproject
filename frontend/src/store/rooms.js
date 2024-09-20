import { defineStore } from "pinia";
import { mixinMethods, $services } from "@/utils/variables";
import { useI18n } from "vue-i18n";
import { reactive } from "vue";

export const useRoomStore = defineStore("user", () => {
  const { t } = useI18n();
  const validation = reactive({ value: {} });
  const listRooms = reactive({ value: [] });
  const totalItems = reactive({ value: 0 });
  const currentPage = reactive({ value: 0 });
  const showModalConfirm = reactive({ value: false });

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

  return {
    validation,
    listRooms,
    totalItems,
    currentPage,
    showModalConfirm,
    getListRooms,
  };
});
