<template>
  <el-card class="box-card bill-details-card">
    <div class="d-flex bill-details-header">
      <h3>{{ $t("bill.list_details") }}</h3>
    </div>
    <el-table
      :data="data.details"
      :summary-method="getSummaries"
      show-summary
      style="width: 100%"
      class="el-tbl-custom bill-tbl"
    >
      <el-table-column min-width="120">
        <template #header>
          <p v-html="$t('bill.table.utilities.header.name')"></p>
        </template>
        <template #default="scope">
          <span class="data-table">{{ scope.row.name }} </span>
        </template>
      </el-table-column>

      <el-table-column min-width="150">
        <template #header>
          <p v-html="$t('bill.table.utilities.header.amount')"></p>
        </template>
        <template #default="scope">
          <el-input
            class="data-table"
            type="number"
            :min="0"
            v-model="scope.row.amount"
            :placeholder="$t('bill.form.bill_utility_amount_placeholder')"
            @change="handleChangeAmount(scope.row.amount, scope.row)"
            :disabled="!isCreate"
          ><template #append>{{ scope.row.unit }}</template></el-input>
        </template>
      </el-table-column>

      <el-table-column min-width="120">
        <template #header>
          <p v-html="$t('bill.table.utilities.header.unit_price')"></p>
        </template>
        <template #default="scope">
          <span class="data-table">{{ formatMoney(scope.row.unitPrice) }}</span>
        </template>
      </el-table-column>

      <el-table-column min-width="120">
        <template #header>
          <p v-html="$t('bill.table.utilities.header.price')"></p>
        </template>
        <template #default="scope">
          <span class="data-table">{{ formatMoney(scope.row.price) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- Display rent price and total price -->
    <div class="summary-info">
        <div>
            <h4>
              {{ $t("bill.rent_price") }}:
              {{ formatMoney(data.rentPrice) }}
            </h4>
            <h4>
              {{ $t("bill.total_price") }}:
              {{ formatMoney(totalPrice) }}
            </h4>
        </div>
    </div>
  </el-card>
</template>

<script>
import { mixinMethods } from "@/utils/variables";
import { useI18n } from "vue-i18n";
import { computed, ref } from "vue";

export default {
  components: {},
  props: {
    data: {
      type: Object, // Change to Object as it holds both `details` and `rentPrice`
      default: () => ({
        details: [],
        rentPrice: 0,
      }),
    },
    isCreate: {
      type: Boolean,
      default: false
    }
  },
  setup(props, { emit }) {
    const { t } = useI18n();
    let totalUtilityPrice = ref(0);
    const handleChangeAmount = (amount, utility) => {
      props.data.details.find((util) => {
        if (util.name == utility.name) {
          util.price = amount * util.unitPrice;
        }
      });
    };

    const formatMoney = (number) => {
      return mixinMethods.formatCurrency(number);
    };

    const getSummaries = ({ columns, data }) => {
      const summaries = [];

      columns.forEach((column, index) => {
        if (index === 0) {
          summaries[index] = t("bill.table.utilities.header.total_util_cost"); // Label for the first column
        } else if (index === columns.length - 1) {
          totalUtilityPrice.value = data.reduce((sum, row) => {
            return sum + (row.price || 0);
          }, 0);
          summaries[index] = formatMoney(totalUtilityPrice.value);
        } else {
          summaries[index] = "";
        }
      });

      return summaries;
    };

    const totalPrice = computed(() => {
      return totalUtilityPrice.value + props.data.rentPrice ?? 0;
    });

    return {
      mixinMethods,
      formatMoney,
      getSummaries,
      handleChangeAmount,
      totalPrice,
    };
  },
};
</script>

<style lang="scss" scoped>
.bill-details-header {
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

.summary-info {
  padding-bottom: 20px;
  padding-right: 60px;
  display: flex;
  justify-content: end;
}
</style>
