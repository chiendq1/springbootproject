<template>
  <div class="room room-list">
    <div class="room-header">
      <h3 class="page__ttl">{{ $t("room.title") }}</h3>
      <div class="room-btn-box room-import-box">
        <el-row
          v-if="highest_role == ADMIN || highest_role == LANDLORD"
          class="mb-4"
        >
          <el-button class="btn btn-save" @click="handleAddNewRoom"
            >{{ $t("room.add_new") }}
          </el-button>
        </el-row>
      </div>
    </div>
    <div class="room-body">
      <div class="room-search">
        <div class="room-search-box col-md-9 col-lg-9">
          <p class="room-search__ttl">
            {{ $t("room.keyword") }}
          </p>
          <div class="mb-0 ruleform">
            <el-input
              :placeholder="$t('common.input_keyword')"
              @keyup.enter="submitForm"
              v-model="searchForms.searchValue"
              prop="searchValue"
            >
              <template #append>
                <span @click="handleSearchForm" class="btn-setting">
                  <IconSetting />
                </span>
              </template>
            </el-input>
          </div>
        </div>
        <div class="btn-search-select col-md-3 col-lg-3 room-box-btn-all">
          <el-button class="btn btn-search" @click="submitForm()">
            {{ $t("common.search") }}</el-button
          >
          <el-button class="btn btn-clear" @click="handleClear()">
            {{ $t("common.clear") }}</el-button
          >
        </div>
      </div>
      <div class="form-search" :class="{ active: isShowBoxSearch }">
        <div class="close-form">
          <IconCircleClose @click="isShowBoxSearch = false" />
        </div>
        <div class="form-search-box">
          <div class="item">
            <CommonSlider
              :modelValue="searchForms.priceRange"
              :label="$t('room.price_range')"
              :isRange="true"
              :max="MAX_PRICE"
              @update:modelValue="
                (newRange) => handleChangeRange(newRange, 'priceRange')
              "
            />
            <CommonSlider
              :modelValue="searchForms.capacity"
              :label="$t('room.capacity')"
              :isRange="true"
              :max="MAX_CAPACITY"
              @update:modelValue="
                (newRange) => handleChangeRange(newRange, 'capacity')
              "
            />
          </div>
          <div class="item">
            <CommonSlider
              :modelValue="searchForms.area"
              :label="$t('room.area')"
              :isRange="true"
              :max="MAX_AREA"
              @update:modelValue="
                (newRange) => handleChangeRange(newRange, 'area')
              "
            />
            <span>{{ $t("common.status") }}</span>
            <MultipleOptionSelect
              v-model="searchForms.status"
              :list-data="listStatus"
              :showClearable="true"
              :placeholder="$t('room.status_placeholder')"
              :isRemote="true"
            />
          </div>
        </div>
      </div>
    </div>

    <div class="bidding-body-table" style="margin-top: 16px; min-height: 250px">
      <RoomTable :listRooms="listRooms" @details="handleGetRoomDetails" @delete="handleOpenModalConfirm"/>
      <LoadMore
        :listData="listRooms.value"
        :totalItems="totalItems.value"
        @loadMore="handleLoadMore"
      />
    </div>
    <ModalConfirm
      :isShowModal="isShowModalConfirm.value"
      @close-modal="handleCloseModal"
      :isConfirmByText="true"
      :confirmText="TEXT_CONFIRM_DELETE"
      @confirmAction="handleConfirm"
      :message="$t('room.modal_confirm.message_delete_room')"
      :title="$t('room.modal_confirm.title_delete_room')"
    />
  </div>
</template>

<script>
import IconSetting from "@/svg/IconSettingMain.vue";
import ModalConfirm from "@/components/common/ModalConfirm.vue";
import IconCircleClose from "@/svg/IconCircleClose.vue";
import SingleOptionSelect from "@/components/common/SingleOptionSelect.vue";
import MultipleOptionSelect from "@/components/common/MultipleOptionSelect.vue";
import CommonSlider from "@/components/common/SliderRange.vue";
import RoomTable from "./item/ListRoomsTable.vue";
import { onMounted, onUnmounted, ref } from "vue";
import { ADMIN, LANDLORD } from "@/constants/roles.js";
import Cookies from "js-cookie";
import { useI18n } from "vue-i18n";
import {
  TEXT_CONFIRM_DELETE,
  MAX_PRICE,
  MAX_CAPACITY,
  MAX_AREA,
} from "@/constants/application.js";
import { useRoomStore } from "@/store/rooms.js";
import { useRouter } from "vue-router";
import PAGE_NAME from "@/constants/route-name.js";
import LoadMore from "@/components/common/LoadMore.vue";

export default {
  name: "RoomList",
  components: {
    IconSetting,
    IconCircleClose,
    CommonSlider,
    RoomTable,
    MultipleOptionSelect,
    SingleOptionSelect,
    LoadMore,
    ModalConfirm,
  },
  setup() {
    const roomStore = useRoomStore();
    const router = useRouter();
    const { t } = useI18n();
    const {
      listRooms,
      totalItems,
      currentPage,
      isShowModalConfirm,
      isCreate,
      deleteRoom,
      getListRooms,
    } = roomStore;
    const searchForms = ref({
      priceRange: [0, 0],
      capacity: [0, 0],
      area: [0, 0],
      status: [],
      searchValue: "",
      pageNo: 0,
    });
    const deleteId = ref(0);
    const listStatus = ref([
      {
        id: 0,
        value: t("room.status.vacant"),
      },
      {
        id: 1,
        value: t("room.status.rented"),
      },
      {
        id: 2,
        value: t("room.status.under_repair"),
      },
    ]);
    const isShowBoxSearch = ref(false);
    const highest_role = Cookies.get("highest_role");

    onMounted(async () => {
      await getListRooms(searchForms.value);
    });

    onUnmounted(() => {
      listRooms.value = [];
    })

    const handleSearchForm = () => {
      isShowBoxSearch.value = !isShowBoxSearch.value;
    };

    const handleClear = () => {
      searchForms.value.status = [];
      searchForms.value.priceRange = [0, 0];
      searchForms.value.capacity = [0, 0];
      searchForms.value.area = [0, 0];
      searchForms.value.searchValue = "";
    };

    const submitForm = () => {
      isShowBoxSearch.value = false;
      listRooms.value = [];
      searchForms.value.pageNo = 0;
      getListRooms(searchForms.value);
    };

    const handleChangeRange = (newRange, field) => {
      searchForms.value[field] = newRange;
    };

    const handleGetRoomDetails = (roomId) => {
      router.push({
        name: PAGE_NAME.ROOM.DETAILS,
        params: { id: roomId },
      });
    };

    const handleAddNewRoom = () => {
      isCreate.value = true;
      router.push({
        name: PAGE_NAME.ROOM.CREATE,
      });
    }

    const handleCloseModal = () => {
      isShowModalConfirm.value = false;
    }

    const handleOpenModalConfirm = (id) => {
      deleteId.value = id;
      isShowModalConfirm.value = true;
    }

    const handleConfirm = () => {
      deleteRoom(deleteId.value);
      handleCloseModal();
    }

    const handleLoadMore = () => {
      currentPage.value++;
      searchForms.value.pageNo++;
      getListRooms(searchForms.value);
    };

    return {
      searchForms,
      isShowBoxSearch,
      highest_role,
      ADMIN,
      LANDLORD,
      TEXT_CONFIRM_DELETE,
      MAX_PRICE,
      MAX_CAPACITY,
      MAX_AREA,
      listStatus,
      listRooms,
      totalItems,
      isShowModalConfirm,
      handleSearchForm,
      handleCloseModal,
      handleConfirm,
      handleChangeRange,
      handleClear,
      submitForm,
      handleOpenModalConfirm,
      handleGetRoomDetails,
      handleLoadMore,
      handleAddNewRoom,
    };
  },
};
</script>

<style lang="scss" scoped>
.close-form {
  position: absolute;
  display: flex;
  justify-content: end;
  right: 16px;
  top: 10px;
  cursor: pointer;
  svg {
    height: 30px;
  }
}
</style>
