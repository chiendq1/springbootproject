<template>
  <el-card class="box-card room-details-card">
    <div class="d-flex room-details-header">
      <h3>{{ $t("room.list_consumption") }}</h3>
    </div>
    <el-table :data="data" style="width: 100%" class="el-tbl-custom room-tbl">
      <el-table-column min-width="120">
        <template #header>
          <p v-html="$t('room.table.utilities.header.name')"></p>
        </template>

        <template #default="scope">
          <span class="data-table">{{ scope.row.name }} </span>
        </template>
      </el-table-column>
      <el-table-column min-width="150">
        <template #header>
          <p v-html="$t('room.table.utilities.header.amount')"></p>
        </template>

        <template #default="scope">
          <el-input
            class="data-table"
            type="number"
            :min="0"
            style="width: 50%;"
            :disabled="true"
            v-model="scope.row.amount"
            :placeholder="$t('bill.form.bill_utility_amount_placeholder')"
            ><template #append>{{ scope.row.unit }}</template></el-input
          >
        </template>
      </el-table-column>
      <el-table-column min-width="60">
        <template #header>
          <p v-html="$t('room.table.utilities.header.price')"></p>
        </template>
        <template #default="scope">
          <span class="data-table">{{ scope.row.price }} </span>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script>
import SingleOptionSelect from "@/components/common/SingleOptionSelect.vue";
import MultipleOptionSelect from "@/components/common/MultipleOptionSelect.vue";

export default {
  components: {
    SingleOptionSelect,
    MultipleOptionSelect,
  },
  props: {
    data: {
      type: Array,
      default: [],
    },
  },
};
</script>
<style lang="scss" scoped>
.room-details-header {
  h2 {
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
</style>
