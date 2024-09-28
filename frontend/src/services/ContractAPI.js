import { get, put, post, del } from "@/services/BaseService";
import API_CODE from "@/utils/api_code";

const index = async (params, success, error) => {
  await get(API_CODE.API_CONTRACT_001, success, error, params);
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
  getContractPdf,
};
