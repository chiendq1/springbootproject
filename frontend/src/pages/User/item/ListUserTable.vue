<template>
  <div>
    <div class="user_table">
      <el-table
        :data="listUsers"
        style="width: 100%"
        class="el-tbl-custom user-tbl"
      >
        <el-table-column
          type="index"
          min-width="53"
          :label="$t('common.no')"
          align="right"
        ></el-table-column>

        <el-table-column min-width="150">
          <template #header>
            <p v-html="$t('user.table.header.username')"></p>
          </template>

          <template #default="scope">
            <span class="data-table">{{ scope.row.username }} </span>
          </template>
        </el-table-column>

        <el-table-column min-width="155">
          <template #header>
            <p v-html="$t('user.table.header.fullname')"></p>
          </template>
          <template #default="scope">
            <span class="data-table data-table--highlight"
              >{{ scope.row.fullName }}
            </span>
          </template>
        </el-table-column>

        <el-table-column min-width="220">
          <template #header>
            <p v-html="$t('user.table.header.email')"></p>
          </template>

          <template #default="scope">
            <span class="data-table">{{ scope.row.email }} </span>
          </template>
        </el-table-column>

        <el-table-column min-width="120">
          <template #header>
            <p v-html="$t('user.table.header.phone')"></p>
          </template>
          <template #default="scope">
            <span class="data-table">{{ scope.row.phoneNumber }}</span>
          </template>
        </el-table-column>

        <el-table-column min-width="120">
          <template #header>
            <p v-html="$t('user.table.header.location')"></p>
          </template>
          <template #default="scope">
            <span class="data-table">{{ scope.row.location ?? "-" }}</span>
          </template>
        </el-table-column>

        <el-table-column min-width="140">
          <template #header>
            <p v-html="$t('user.table.header.role')"></p>
          </template>

          <template #default="scope">
            <span
              class="data-table user-type-cate text-color"
              :class="`user-${scope.row.highestRole.toLowerCase()}`"
              >{{ scope.row.highestRole }}</span
            >
          </template>
        </el-table-column>

        <el-table-column v-if="userRole == ADMIN" min-width="110">
          <template #header>
            <p v-html="$t('user.table.header.action')"></p>
          </template>
          <template #default="scope">
            <div>
              <button @click="$emit('details', scope.row.id)" class="btn-edit">
                <IconEdit />
              </button>
              <button
                @click="$emit('delete', scope.row.id)"
                class="btn-edit"
              >
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
import { ADMIN } from "@/constants/roles.js";

export default {
  name: "UsersTable",
  components: {
    IconEdit,
    IconTrash,
  },
  props: {
    listUsers: {
      type: Array,
      default: () => [],
    },
    userRole: {
      type: String,
      default: ""
    }
  },
  setup() {
    return {
      ADMIN
    };
  },
};
</script>

<style lang="scss" scoped>
.user_table {
  margin-top: 16px;
  padding-bottom: 20px;
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
.user {
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

  &-admin {
    background: #36812a;
  }

  &-landlord {
    background: #8e3c9b;
  }

  &-tenant {
    background: #2d47a3;
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
