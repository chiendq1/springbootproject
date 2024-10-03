import { get, post, put } from "@/services/BaseService";
import API_CODE from "@/utils/api_code";

const index = async (params, success, error) => {
  await get(API_CODE.API_UTILITY_001, success, error, params);
};

const show = async (id, params, success, error) => {
  await get(API_CODE.API_UTILITY_001 + "/" + id, success, error, params);
};

const update = async (id, params, success, error) => {
  await put(API_CODE.API_UTILITY_001 + "/" + id, params, success, error);
};

const deactivate = async (id, params, success, error) => {
  await put(API_CODE.API_UTILITY_002 + "/" + id, params, success, error);
};

const create = async (params, success, error) => {
  await post(API_CODE.API_UTILITY_001, params, success, error);
};

export const UtilityAPI = {
  index,
  create,
  deactivate,
  update,
  show
};
