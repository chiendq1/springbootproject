<template>
  <div>
    <div class="utility_table">
      <el-table
        :data="listUtilities"
        style="width: 100%"
        class="el-tbl-custom utility-tbl"
      >
        <el-table-column
          type="index"
          min-width="40"
          :label="$t('common.no')"
          align="right"
        ></el-table-column>

        <el-table-column min-width="150">
          <template #header>
            <p v-html="$t('utility.table.header.name')"></p>
          </template>

          <template #default="scope">
            <span class="data-table">{{ scope.row.value }} </span>
          </template>
        </el-table-column>

        <el-table-column min-width="155">
          <template #header>
            <p v-html="$t('utility.table.header.unit_price')"></p>
          </template>
          <template #default="scope">
            <span class="data-table data-table--highlight"
              >{{ formatMoney(scope.row.unitPrice) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column min-width="100">
          <template #header>
            <p v-html="$t('utility.table.header.unit')"></p>
          </template>

          <template #default="scope">
            <span class="data-table">{{ scope.row.unit }} </span>
          </template>
        </el-table-column>

        <el-table-column min-width="140">
          <template #header>
            <p v-html="$t('utility.table.header.status')"></p>
          </template>
          <template #default="scope">
            <span
              class="data-table utility-type-cate text-color"
              :class="`utility-${handleStatus(scope.row.status)}`"
              >{{ $t(`utility.status.${handleStatus(scope.row.status)}`) }}</span
            >
          </template>
        </el-table-column>

        <el-table-column min-width="110">
          <template #header>
            <p v-html="$t('utility.table.header.action')"></p>
          </template>
          <template #default="scope">
            <div v-if="!scope.row.status">
              <button @click="$emit('details', scope.row.id)" class="btn-edit">
                <IconEdit />
              </button>
              <button
                @click="$emit('delete', scope.row.id)"
                class="btn-edit"
              >
                <IconInactive />
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
import IconInactive from "@/svg/IconInactive.vue";
import { ADMIN } from "@/constants/roles.js";
import { UTILITY_STATUSES } from "@/constants/utility.js";
import { mixinMethods } from "@/utils/variables";

export default {
  name: "UtilitysTable",
  components: {
    IconEdit,
    IconInactive,
  },
  props: {
    listUtilities: {
      type: Array,
      default: () => [],
    }
  },
  setup() {
    const formatMoney = (number) => {
      return mixinMethods.formatCurrency(number);
    };

    const handleStatus = (statusId) => {
      return UTILITY_STATUSES[statusId];
    };
    return {
      ADMIN,
      handleStatus,
      formatMoney
    };
  },
};
</script>

<style lang="scss" scoped>
.utility_table {
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
.utility {
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

  &-active {
    background: #36812a;
  }

  &-inactive {
    background: #d41111;
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
