import axios from "@/plugins/axios";
import { initializeService } from "@/services/BaseService";
import { AuthenticationAPI } from "@/services/AuthenticationAPI";
import { UserAPI } from "@/services/UserAPI";
import { RoomAPI } from "@/services/RoomAPI";
import { UtilityAPI } from "@/services/UtilityAPI";
import { ContractAPI } from "@/services/ContractAPI";
import { BillAPI } from "@/services/BillAPI";

// Initialize the BaseService with the axios instance and API prefix
(function() {
  initializeService(axios, "/api");
})();

const services = {
  AuthenticationAPI,
  UserAPI,
  RoomAPI,
  UtilityAPI,
  ContractAPI,
  BillAPI,
};

export default services;
