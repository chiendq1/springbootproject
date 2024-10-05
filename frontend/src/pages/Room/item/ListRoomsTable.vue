<template>
  <div>
    <div class="room_table">
      <el-table
        :data="listRooms.value"
        style="width: 100%"
        class="el-tbl-custom room-tbl"
      >
        <el-table-column
          type="index"
          min-width="53"
          :label="$t('common.no')"
          align="right"
        ></el-table-column>

        <el-table-column min-width="150">
          <template #header>
            <p v-html="$t('room.table.header.room_code')"></p>
          </template>

          <template #default="scope">
            <span class="data-table">{{ scope.row.roomCode }} </span>
          </template>
        </el-table-column>

        <el-table-column min-width="155">
          <template #header>
            <p v-html="$t('room.table.header.area')"></p>
          </template>
          <template #default="scope">
            <span class="data-table data-table--highlight"
              >{{ scope.row.area }}
            </span>
          </template>
        </el-table-column>

        <el-table-column min-width="80">
          <template #header>
            <p v-html="$t('room.table.header.capacity')"></p>
          </template>

          <template #default="scope">
            <span class="data-table">{{ scope.row.capacity }} </span>
          </template>
        </el-table-column>

        <el-table-column min-width="100">
          <template #header>
            <p v-html="$t('room.table.header.price')"></p>
          </template>

          <template #default="scope">
            <span class="data-table">{{ formatMoney(scope.row.rentPrice) }} </span>
          </template>
        </el-table-column>

        <el-table-column min-width="140">
          <template #header>
            <p v-html="$t('room.table.header.status')"></p>
          </template>
          <template #default="scope">
            <span
              class="data-table room-type-cate text-color"
              :class="`room-${handleStatus(scope.row.status)}`"
              >{{ $t(`room.status.${handleStatus(scope.row.status)}`) }}</span
            >
          </template>
        </el-table-column>

        <el-table-column min-width="110">
          <template #header>
            <p v-html="$t('room.table.header.action')"></p>
          </template>
          <template #default="scope">
            <div>
              <button @click="$emit('details', scope.row.roomId)" class="btn-edit">
                <IconEdit />
              </button>
              <button @click="$emit('delete', scope.row.roomId)" class="btn-edit">
                <IconTrash />
              </button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import IconEdit from "@/svg/IconEdit.vue";
import IconTrash from "@/svg/IconTrash.vue";
import { ROOM_STATUSES } from "@/constants/room.js";
import { mixinMethods } from "@/utils/variables";

export default {
  name: "RoomsTable",
  components: {
    IconEdit,
    IconTrash,
  },
  props: {
    listRooms: {
      type: Array,
      default: () => [],
    }
  },
  setup() {
    const handleStatus = (statusId) => {
      return ROOM_STATUSES[statusId];
    };

    const formatMoney = (number) => {
      return mixinMethods.formatCurrency(number);
    };
    return {
      handleStatus,
      formatMoney,
    };
  },
};
</script>

<style lang="scss" scoped>
.user_table {
  margin-top: 16px;
  max-height: calc(77vh - 70px);
  overflow-y: auto;
}

.data-table-set {
  color: #787878;
}

.btn {
  &-payment {
    color: #fff;
    border: 1px solid #785199;
    background: #8e69ae;
    border-radius: 4px;
    width: 76px;
    cursor: pointer;
  }

  &-cr {
    color: #fff;
    cursor: pointer;
    border: 1px solid #382665;
    background: #4e3a7f;
    border-radius: 4px;
    width: 76px;
  }
}
.room {
  &-status {
    background: #ccc;
    color: #fff !important;
    font-size: 12px;
    border-radius: 100px;
    display: block;
    padding: 2px 16px;
    line-height: 1.2;
    font-weight: 700;
    text-align: center;
  }

  &-rented {
    background: #aa1191;
  }

  &-vacant {
    background: #15a726;
  }

  &-under_repair {
    background: #e7d31d;
  }

  &-type-cate {
    border-radius: 100px;
    display: block;
    padding: 2px 16px;
    line-height: 1.2;
    font-weight: 700;
    text-align: center;
  }

  &-number {
    font-weight: 700;
  }
}
</style>

<style lang="scss">
.text-color {
  color: #dbdee0;
}

.data-table {
  word-break: break-word;
}
</style>
