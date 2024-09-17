import {post, get} from '@/services/BaseService';
import API_CODE from '@/utils/api_code';

// Function to handle login
const login = async (params, success, error) => {
  await post(API_CODE.API_001, params, success, error);
};

const getOTP = async (params, success, error) => {
  await post(API_CODE.API_003, params, success, error);
}
// Function to handle logout
const resetPassword = async (params, success, error) => {
  await post(API_CODE.API_002, params, success, error);
};

export const AuthenticationAPI = {
  login,
  resetPassword,
  getOTP,
};
