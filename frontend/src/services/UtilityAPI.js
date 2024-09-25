import { get } from "@/services/BaseService";
import API_CODE from "@/utils/api_code";

const index = async (params, success, error) => {
  await get(API_CODE.API_UTILITY_001, success, error, params);
};

export const UtilityAPI = {
  index,
};
