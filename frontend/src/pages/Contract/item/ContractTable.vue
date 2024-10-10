<template>
  <el-table
    :data="data"
    max-height="400"
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
        <p v-html="$t('contract.table.header.name')"></p>
      </template>

      <template #default="scope">
        <span class="data-table">{{ scope.row.contractName }} </span>
      </template>
    </el-table-column>
    <el-table-column min-width="120">
      <template #header>
        <p v-html="$t('contract.table.header.start_date')"></p>
      </template>

      <template #default="scope">
        <span class="data-table"
          >{{ scope.row.startDate }}
        </span>
      </template>
    </el-table-column>
    <el-table-column min-width="120">
      <template #header>
        <p v-html="$t('contract.table.header.finish_date')"></p>
      </template>

      <template #default="scope">
        <span class="data-table"
          >{{ scope.row.endDate }}
        </span>
      </template>
    </el-table-column>
    <el-table-column min-width="120">
      <template #header>
        <p v-html="$t('contract.table.header.room')"></p>
      </template>

      <template #default="scope">
        <span class="data-table">{{ scope.row.room.roomCode }} </span>
      </template>
    </el-table-column>
    <el-table-column min-width="120">
      <template #header>
        <p v-html="$t('contract.table.header.landlord')"></p>
      </template>

      <template #default="scope">
        <span class="data-table">{{ scope.row.room.landlord.username }} </span>
      </template>
    </el-table-column>
    <el-table-column min-width="130">
      <template #header>
        <p v-html="$t('contract.table.header.type')"></p>
      </template>

      <template #default="scope">
        <span
          class="data-table contract-type-cate text-color"
          :class="`contract-${handleTypes(scope.row.type)}`"
          >{{ $t(`contract.types.${handleTypes(scope.row.type)}`) }}</span
        >
      </template>
    </el-table-column>
    <el-table-column min-width="130">
      <template #header>
        <p v-html="$t('contract.table.header.status')"></p>
      </template>

      <template #default="scope">
        <span
          class="data-table contract-type-cate text-color"
          :class="`contract-${handleStatus(scope.row.status)}`"
          >{{ $t(`contract.statuses.${handleStatus(scope.row.status)}`) }}</span
        >
      </template>
    </el-table-column>
    <el-table-column min-width="90">
      <template #header>
        <p v-html="$t('contract.table.header.action')"></p>
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
import { CONTRACT_STATUSES, CONTRACT_TYPES } from "@/constants/contract.js";

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
      return CONTRACT_STATUSES[statusId];
    };
    
    const handleTypes = (typeId) => {
      return CONTRACT_TYPES[typeId];
    };

    return {
      handleStatus,
      handleTypes,
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

.contract {
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

  &-renewed {
    background: #d4a816;
  }

  &-active {
    background: #15a726;
  }

  &-terminated {
    background: #d03333;
  }

  &-monthly {
    background: #4e1b7b;
  }

  &-quater {
    background: #136e87;
  }

  &-half-year {
    background: #4616d4;
  }
  
  &-year {
    background: #772044;
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
