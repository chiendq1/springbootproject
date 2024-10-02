import { get, put, post } from "@/services/BaseService";
import API_CODE from "@/utils/api_code";

const index = async (params, success, error) => {
  await get(API_CODE.API_BILL_001, success, error, params);
};

const show = async (id, success, error, params) => {
  await get(API_CODE.API_BILL_001 + "/" + id, success, error, params);
};

const create = async (params, success, error) => {
  await post(API_CODE.API_BILL_002, params, success, error);
};

export const BillAPI = {
  index,
  show,
  create,
};
