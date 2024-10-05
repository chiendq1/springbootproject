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
  API_UTILITY_001: '/utilities',
  API_UTILITY_002: '/utilities/deactivate',

  //Contracts
  API_CONTRACT_001: '/contracts',
  API_CONTRACT_002: '/contracts/pdf',

  //Bills
  API_BILL_001: '/bills',
  API_BILL_002: '/bills/create',
  API_BILL_003: '/bills/pdf',

  //Currency
  API_CURRENCY_001: '/currencies'
};

export default API_CODE;
