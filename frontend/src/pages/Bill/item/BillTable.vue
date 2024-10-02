<template>
  <el-table
    :data="data"
    max-height="400"
    style="width: 100%"
    class="el-tbl-custom room-tbl"
  >
    <el-table-column
      type="index"
      min-width="35"
      :label="$t('common.no')"
      align="right"
    ></el-table-column>
    <el-table-column min-width="120">
      <template #header>
        <p v-html="$t('bill.table.header.code')"></p>
      </template>

      <template #default="scope">
        <span class="data-table">{{ scope.row.billCode }} </span>
      </template>
    </el-table-column>
    <el-table-column min-width="120">
      <template #header>
        <p v-html="$t('bill.table.header.room')"></p>
      </template>

      <template #default="scope">
        <span class="data-table">{{ scope.row.room.roomCode }} </span>
      </template>
    </el-table-column>
    <el-table-column min-width="120">
      <template #header>
        <p v-html="$t('bill.table.header.month')"></p>
      </template>
      <template #default="scope">
        <span class="data-table">{{ formatDate(scope.row.date) }} </span>
      </template>
    </el-table-column>
    <el-table-column min-width="120">
      <template #header>
        <p v-html="$t('bill.table.header.status')"></p>
      </template>

      <template #default="scope">
        <span
          class="data-table bill-type-cate text-color"
          :class="`bill-${handleStatus(scope.row.paymentStatus)}`"
          >{{
            $t(`bill.statuses.${handleStatus(scope.row.paymentStatus)}`)
          }}</span
        >
      </template>
    </el-table-column>
    <el-table-column min-width="90">
      <template #header>
        <p v-html="$t('bill.table.header.action')"></p>
      </template>
      <template #default="scope">
        <div>
          <button @click="$emit('details', scope.row.id)" class="btn-edit">
            <IconEdit />
          </button>
          <button @click="$emit('download', scope.row.id)" class="btn-edit">
            <IconDownload />
          </button>
        </div>
      </template>
    </el-table-column>
  </el-table>
</template>
<script>
import IconEdit from "@/svg/IconEdit.vue";
import IconDownload from "@/svg/IconDownload.vue";
import { BILL_STATUSES } from "@/constants/bill.js";
import { mixinMethods } from "@/utils/variables";
import { BILL_DATE_FORMAT } from "@/constants/application";

export default {
  components: {
    IconEdit,
    IconDownload,
  },
  props: {
    data: {
      type: Array,
      default: [],
    },
  },
  setup(props, { emit }) {
    const handleStatus = (statusId) => {
      return BILL_STATUSES[statusId];
    };

    const formatDate = (date) => {
      return mixinMethods.showDateTime(date, BILL_DATE_FORMAT);
    };
    return {
      formatDate,
      handleStatus,
    };
  },
};
</script>
<style lang="scss" scoped>
.list-users-header {
  h3 {
    width: 90%;
    text-align: start;
  }
  div {
    align-items: center;
    svg {
      cursor: pointer;
    }
  }
}

.bill {
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

  &-file {
    margin-left: 12px;
  }

  &-paid {
    background: #15a726;
  }

  &-unpaid {
    background: #d68f1f;
  }
  
  &-terminated {
    background: #d03333;
  }

  &-number {
    font-weight: 700;
  }

  &-type-cate {
    border-radius: 100px;
    display: block;
    padding: 2px 16px;
    line-height: 1.2;
    font-weight: 700;
    text-align: center;
  }
}
</style>
