import { get } from "@/services/BaseService";
import API_CODE from "@/utils/api_code";

const getExchangeRate = async (params, success, error) => {
  await get(API_CODE.API_CURRENCY_001, success, error, params);
};

export const CurrencyAPI = {
  getExchangeRate,
};
