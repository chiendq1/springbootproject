const API_CODE = {
  // Auth
  API_001: '/auth/login',
  API_002: '/auth/reset-password',
  API_003: '/auth/get-otp',
  API_004: '/auth/refreshtoken',

  //Users
  API_USER_001: '/users',
  API_USER_002: '/users/password/',
  API_USER_003: '/users/free',
  API_USER_004: '/users/list-by-role',

  //Rooms
  API_ROOM_001: '/rooms',
  API_ROOM_002: (id) => `/rooms/${id}/tenants`,
  API_ROOM_003: '/rooms/list-by-role',

  //Utilities
  API_UTILITY_001: '/utility',

  //Contracts
  API_CONTRACT_001: '/contracts'
};

export default API_CODE;
