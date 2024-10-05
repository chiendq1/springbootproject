import axios from "axios";
import Cookies from "js-cookie";
import { post } from "@/services/BaseService";
import API_CODE from "@/utils/api_code";
import { useAuthStore } from "@/store/auth.js";
import { FRONT_END_URL } from "@/constants/application";
import { $PAGES } from "@/utils/variables";

const headers = {
  "x-locale": localStorage.getItem('CurrentLanguage') || "ja",
};

const token = Cookies.get("access_token");
if (token) {
  headers.Authorization = `Bearer ${token}`;
}

const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_SERVER_URL,
  timeout: 80000,
  headers,
});

// Request interceptor to update the token in the headers
axiosInstance.interceptors.request.use((request) => {
  const token = Cookies.get("access_token");
  if (token) {
    request.headers.Authorization = `Bearer ${token}`;
  }

  return request;
});

let isAlreadyFetchingAccessToken = false;

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    const authStore = useAuthStore();
    const { loggedIn } = authStore;

    // Check if the error is due to an expired token (status 401)
    if (error.response.status === 401 && !isAlreadyFetchingAccessToken) {
      isAlreadyFetchingAccessToken = true; // Prevent infinite retry loops

      const refresh_token = Cookies.get("refresh_token");
      if (refresh_token) {
        try {
          await post(API_CODE.API_004, {
            refreshToken: refresh_token,
          }, (res) => {
            if (res.data && res.data.token) {
              isAlreadyFetchingAccessToken = false;
              const newAccessToken = res.data.token;
              Cookies.set("access_token", newAccessToken);
  
              // Update the axios instance defaults
              axiosInstance.defaults.headers.Authorization = `Bearer ${newAccessToken}`;
  
              // Update the original request's headers with the new token
              originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
  
              // Retry the original request
              return axiosInstance(originalRequest);
            } else {
              // Redirect to login page if token refresh fails
              handleRedirect(loggedIn);
            }
          }, () => {
            handleRedirect(loggedIn);
          });
        } catch (err) {
          // Redirect to login page on error
          handleRedirect(loggedIn);
        }
      } else {
        // Redirect to login if no refresh token exists
        handleRedirect(loggedIn);
      }
    }

    return Promise.reject(error);
  }
);

const handleRedirect = (loggedIn) => {
  loggedIn.value = false;
  location.href = FRONT_END_URL + $PAGES.LOGIN;
  return Promise.reject(error);
};

const setHeaders = (customHeaders) => {
  axiosInstance.defaults.headers.common = {
    ...axiosInstance.defaults.headers.common,
    ...customHeaders,
  };
};

export default {
  axiosInstance,
  setHeaders,
};
