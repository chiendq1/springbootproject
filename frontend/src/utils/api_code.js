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
  API_USER_004: '/users/landlord',

  //Rooms
  API_ROOM_001: '/rooms',
  API_ROOM_002: (id) => `/rooms/${id}/tenants`,

  //Utilities
  API_UTILITY_001: '/utility'
};

export default API_CODE;
