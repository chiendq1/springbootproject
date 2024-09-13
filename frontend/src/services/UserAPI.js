import { get, put } from "@/services/BaseService";
import API_CODE from "@/utils/api_code";

const show = async (id, params, success, error) => {
  await get(API_CODE.API_USER_001 + id, success, error, params);
};

const update = async (id, params, success, error) => {
  await put(API_CODE.API_USER_001 + id, params, success, error);
};

export const UserAPI = {
    show,
    update,
};
