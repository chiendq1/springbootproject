import { get, put, post } from "@/services/BaseService";
import API_CODE from "@/utils/api_code";

const index = async (params, success, error) => {
  await get(API_CODE.API_BILL_001, success, error, params);
};

const show = async (id, success, error, params) => {
  await get(API_CODE.API_BILL_001 + "/" + id, success, error, params);
};

const update = async (id, params, success, error) => {
  await put(API_CODE.API_BILL_001 + "/" + id, params, success, error);
};

const create = async (params, success, error) => {
  await post(API_CODE.API_BILL_002, params, success, error);
};

const getBillPdf = async (params, success, error) => {
  await post(API_CODE.API_BILL_003, params, success, error, {
    responseType: "blob",
  });
};

export const BillAPI = {
  index,
  show,
  update,
  getBillPdf,
  create,
};
