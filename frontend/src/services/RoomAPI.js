import { get, put, post, del } from "@/services/BaseService";
import API_CODE from "@/utils/api_code";

const index = async (params, success, error) => {
  await get(API_CODE.API_ROOM_001, success, error, params);
};

const create = async (params, success, error) => {
  await post(API_CODE.API_ROOM_001, params, success, error);
};

const show = async (id, params, success, error) => {
  await get(API_CODE.API_ROOM_001 + '/' + id, success, error, params);
}

const getListRoomsByRole = async (params, success, error) => {
  await get(API_CODE.API_ROOM_003, success, error, params);
}

const update = async (id, params, success, error) => {
  await put(API_CODE.API_ROOM_001 + '/' + id, params, success, error);
}

const addTenants = async (roomId, params, success, error) => {
  const url = API_CODE.API_ROOM_002(roomId);
  await post(url, params, success, error);
}

const deleteTenant = async (roomId, data, success, error) => {
  const url = API_CODE.API_ROOM_002(roomId);
  await del(url, data, success, error);
}

const deleteRoom = async (id, params, success, error) => {
  await del(API_CODE.API_ROOM_001 + '/' + id, params, success, error);
}

export const RoomAPI = {
  index,
  show,
  create,
  update,
  addTenants,
  deleteTenant,
  deleteRoom,
  getListRoomsByRole,
};
