import axios from "axios";
import Cookies from "js-cookie";
import { post } from "@/services/BaseService";
import API_CODE from "@/utils/api_code";
import { useRouter } from "vue-router";
import { $PAGES } from "@/utils/variables";

const headers = {
  "x-locale": Cookies.get("CurrentLanguage") || "ja",
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

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const router = useRouter();
    const originalRequest = error.config;

    // Check if the error is due to an expired token (status 401)
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true; // Prevent infinite retry loops

      const refresh_token = Cookies.get("refresh_token");
      if (refresh_token) {
        try {
          await post(
            API_CODE.API_004,
            { refreshToken: refresh_token },
            (res) => {
              if (res.data && res.data.token) {
                const newAccessToken = res.data.token;
                Cookies.set("access_token", newAccessToken);

                // Update the axios instance defaults
                axiosInstance.defaults.headers.Authorization = `Bearer ${newAccessToken}`;

                // Update the original request's headers with the new token
                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;

                // Retry the original request
                return axiosInstance(originalRequest);
              } else {
                router.push($PAGES.LOGIN);
              }
            },
            (err) => {
              console.log("Failed to call api:", err);
              router.push($PAGES.LOGIN);
            }
          );
        } catch (err) {
          console.log("Failed to refresh token:", err);
          router.push($PAGES.LOGIN);
        }
      } else {
        router.push($PAGES.LOGIN);
      }
    }

    // If the error is not a 401, reject the promise with the error
    return Promise.reject(error);
  }
);

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
