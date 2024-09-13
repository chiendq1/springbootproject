<template>
  <div class="user-profile-page">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card profile-card">
          <div class="avatar">
            <img
              src="@/assets/logo_login.png"
              alt="Profile Image"
              class="avatar__img"
            />
          </div>
          <div class="user-info">
            <h3>{{ userDetails.value.username }}</h3>
            <p>
              {{ $t("user.role") }}: {{ userDetails.value.roles[0].roleName }}
            </p>
          </div>
          <div class="user-actions">
            <el-button
              class="btn btn-primary"
              round
              @click="openChangePasswordModal"
            >
              {{ $t("common.change_password") }}
            </el-button>
          </div>
        </el-card>
      </el-col>

      <!-- Right Side: User Information Form -->
      <el-col :span="18">
        <el-card class="box-card info-card">
          <h3>{{ $t("user.infor") }}</h3>
          <el-form :model="userDetails.value" label-width="120px">
            <!-- Form Fields -->
            <el-form-item :label="$t('user.label_fullname')">
              <el-input
                v-model="userDetails.value.fullName"
                :placeholder="$t('user.placeholder_fullname')"
              ></el-input>
            </el-form-item>

            <el-form-item :label="$t('user.label_email')">
              <el-input
                v-model="userDetails.value.email"
                :placeholder="$t('user.placeholder_email')"
              ></el-input>
            </el-form-item>

            <el-form-item :label="$t('user.label_phone')">
              <el-input
                v-model="userDetails.value.phoneNumber"
                :placeholder="$t('user.placeholder_phone')"
              ></el-input>
            </el-form-item>

            <el-form-item :label="$t('user.label_location')">
              <el-input
                v-model="userDetails.value.location"
                :placeholder="$t('user.placeholder_location')"
              ></el-input>
            </el-form-item>

            <el-form-item>
              <el-button class="btn btn-save" @click="onSubmit">{{
                $t("common.save")
              }}</el-button>
              <el-button class="btn btn-refuse" @click="onSubmit(false)">{{
                $t("common.cancel")
              }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- Modal for Change Password -->
    <Modal
      :show="isPasswordModalVisible"
      :width="'50%'"
      :isShowFooter=false
      @close="isPasswordModalVisible = false"
      modal-title="Change Password"
    >
      <template #body>
        <el-form :model="passwordForm" label-width="25%">
          <el-form-item :label="$t('user.label_old_password')">
            <el-input
              type="password"
              v-model="passwordForm.oldPassword"
              :placeholder="$t('user.placeholder_old_password')"
            ></el-input>
          </el-form-item>

          <el-form-item :label="$t('user.label_new_password')">
            <el-input
              type="password"
              v-model="passwordForm.newPassword"
              :placeholder="$t('user.placeholder_new_password')"
            ></el-input>
          </el-form-item>

          <el-form-item :label="$t('user.label_confirmed_password')">
            <el-input
              type="password"
              v-model="passwordForm.confirmPassword"
              :placeholder="$t('user.placeholder_confirmed_password')"
            ></el-input>
          </el-form-item>

          <el-form-item class="confirm-button">
            <el-button class="btn btn-save" @click="changePassword">{{
                $t("common.save")
              }}</el-button>
          </el-form-item>
        </el-form>
      </template>
    </Modal>
  </div>
</template>

<script>
import { useAuthStore } from "@/store/auth.js";
import { useUserStore } from "@/store/users.js";
import Modal from "@/components/common/Modal.vue";
import { ref } from "vue";

export default {
  name: "UserProfile",
  components: {
    Modal,
  },
  setup() {
    const authStore = useAuthStore();
    const userStore = useUserStore();
    const { userDetails } = authStore;
    const { updateUserProfile, getUserProfile } = userStore;

    // State to control the password modal visibility
    const isPasswordModalVisible = ref(false);

    // Password form model
    const passwordForm = ref({
      oldPassword: "",
      newPassword: "",
      confirmPassword: "",
    });

    // Function to handle opening the modal
    const openChangePasswordModal = () => {
      isPasswordModalVisible.value = true;
    };

    // Function to handle password change submission
    const changePassword = () => {
      if (
        passwordForm.value.newPassword !== passwordForm.value.confirmPassword
      ) {
        // Show an error notification about password mismatch
        console.error("Passwords do not match");
        return;
      }
      // Call an API or perform action to change the password
      console.log("Changing password", passwordForm.value);
      isPasswordModalVisible.value = false; // Close modal after submission
    };

    // Function to handle form submission
    const onSubmit = (isUpdate = true) => {
      if (isUpdate) {
        updateUserProfile();
      } else {
        getUserProfile();
      }
    };

    return {
      userDetails,
      onSubmit,
      isPasswordModalVisible,
      openChangePasswordModal,
      passwordForm,
      changePassword,
    };
  },
};
</script>

<style scoped lang="scss">
.user-profile-page {
  padding: 20px;
}

.box-card {
  height: 90%;
}

.profile-card,
.info-card {
  padding: 20px;
}

.avatar {
  display: flex;
  justify-content: center;
  margin-bottom: 15px;
}

.avatar__img {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
}

.user-info {
  text-align: center;
  margin-bottom: 20px;
}

.user-actions {
  display: flex;
  justify-content: space-around;

  .btn {
    width: 90%;
  }
}

.info-card {
  padding: 20px;
}

h3 {
  margin-bottom: 20px;
}

.confirm-button {
  margin-left: 80%;
}
</style>
