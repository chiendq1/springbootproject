import { get, put, post, del } from "@/services/BaseService";
import API_CODE from "@/utils/api_code";

const index = async (params, success, error) => {
  await get(API_CODE.API_USER_001, success, error, params);
};

const show = async (id, params, success, error) => {
  await get(API_CODE.API_USER_001 + '/' + id, success, error, params);
};

const update = async (id, params, success, error) => {
  await put(API_CODE.API_USER_001 + '/' + id, params, success, error);
};

const store = async (params, success, error) => {
  await post(API_CODE.API_USER_001, params, success, error);
};

const remove = async (id, params, success, error) => {
  await del(API_CODE.API_USER_001 + '/' + id, params, success, error);
};

const changePassword = async (id, params, success, error) => {
  await put(API_CODE.API_USER_002 + id, params, success, error);
};

const getListFreeUsers = async (params, success, error) => {
  await get(API_CODE.API_USER_003, success, error, params);
};

const getListLandlords = async (params, success, error) => {
  await get(API_CODE.API_USER_004, success, error, params);
};

export const UserAPI = {
  index,
  remove,
  show,
  update,
  store,
  changePassword,
  getListFreeUsers,
  getListLandlords,
};
