import axios from "@/plugins/axios";
import { initializeService } from "@/services/BaseService";
import { AuthenticationAPI } from "@/services/AuthenticationAPI";
import { UserAPI } from "@/services/UserAPI";

// Initialize the BaseService with the axios instance and API prefix
(function() {
  initializeService(axios, "/api");
})();

const services = {
  AuthenticationAPI,
  UserAPI,
};

export default services;
