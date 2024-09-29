import { get, put, post } from "@/services/BaseService";
import API_CODE from "@/utils/api_code";

const index = async (params, success, error) => {
  await get(API_CODE.API_CONTRACT_001, success, error, params);
};

const show = async (id, params, success, error) => {
  await get(API_CODE.API_CONTRACT_001 + '/' + id , success, error, params);
};

const update = async (id, params, success, error) => {
  await put(API_CODE.API_CONTRACT_001 + '/' + id, params, success, error);
};

const getContractPdf = async (params, success, error) => {
  await post(API_CODE.API_CONTRACT_002, params, success, error, {
    responseType: "blob",
  });
};

const create = async (params, success, error) => {
  await post(API_CODE.API_CONTRACT_001, params, success, error);
};

export const ContractAPI = {
  index,
  create,
  update,
  show,
  getContractPdf,
};
