import { ref } from "vue";
import { ElLoading } from "element-plus";
import Cookies from "js-cookie";
import moment from "moment";
import { RULES_VALIDATION } from "@/constants/application";
import { $exchangeRate, $globalLocale } from "@/utils/variables";
import { ElNotification } from "element-plus";

const screenLoading = ref(false);
let loadingInstance = null;

const startLoading = () => {
  screenLoading.value = true;
  loadingInstance = ElLoading.service({
    lock: true,
    text: "Loading",
    background: "rgba(0, 0, 0, 0.7)",
  });
};

const endLoading = async () => {
  if (!checkEmpty(loadingInstance)) {
    await loadingInstance.close();
    loadingInstance = null;
  }
  screenLoading.value = false;
};

const checkEmpty = (value) => value === null || value === undefined;

const checkEmptyWithOutZero = (value) => {
  if (Array.isArray(value)) return value.length === 0;
  if (value && typeof value === "object" && value.constructor === Object)
    return Object.keys(value).length === 0;
  if (typeof value === "string" || value instanceof String)
    return value.trim().length === 0;
  return value === null || typeof value === "undefined" || value === "";
};

const saveAccessToken = (value) =>
  Cookies.set("access_token", value, { expires: 90 });
const saveExpiredDateTime = (value) =>
  Cookies.set("expired_date_time", value, { expires: 90 });
const saveUserId = (value) => Cookies.set("user_id", value, { expires: 90 });
const saveRole = (role) => Cookies.set("role", role, { expires: 90 });
const removeAccessToken = () => Cookies.remove("access_token");

const arrayChunk = (array, size) => {
  const chunkedArr = [];
  for (let i = 0; i < array.length; i++) {
    const last = chunkedArr[chunkedArr.length - 1];
    if (!last || last.length === size) {
      chunkedArr.push([array[i]]);
    } else {
      last.push(array[i]);
    }
  }
  return chunkedArr;
};

const notifyError = (messages, useHTML = false, notify = null) => {
  let message = "";

  if (useHTML && Array.isArray(messages)) {
    messages.forEach((item) => (message += `<p>${item}</p>`));
  } else if (useHTML && typeof messages === "object") {
    for (let key in messages) message += `<p>${messages[key]}</p>`;
  } else {
    message = messages;
  }

  const notifyObj = notify ?? ElNotification;
  notifyObj.error({
    message,
    duration: 2000,
    position: "top-right",
    dangerouslyUseHTMLString: useHTML,
    showClose: false,
  });
};

const notifySuccess = (messages, useHTML = false, notify = null) => {
  let message = "";

  if (useHTML && Array.isArray(messages)) {
    messages.forEach((item) => (message += `<p>${item}</p>`));
  } else {
    message = messages;
  }

  const notifyObj = notify ?? ElNotification;
  notifyObj.success({
    message,
    duration: 2000,
    position: "top-right",
    dangerouslyUseHTMLString: useHTML,
    showClose: false,
  });
};

const validateInvalidEmail = (email) =>
  RULES_VALIDATION.EMAIL_FORMAT.test(email);

const showDateTime = (dateTime, formatString = undefined) => {
  if (checkEmptyWithOutZero(dateTime)) return;

  formatString = formatString || import.meta.env.VITE_APP_FORMATDATE;

  return moment(dateTime, [
    "YYYY/MM",
    "YYYY/MM/DD",
    "YYYY-MM-DD",
    "YYYY-MM-DD HH:mm:ss",
    "YYYY/MM/DD HH:mm:ss",
    "YYYY-MM-DD HH:mm",
    "YYYY/MM/DD HH:mm",
  ]).format(formatString);
};

const formatCurrency = (money, decimal = 0, s_delimiter = ",") => {
  if (!money) return "";

  const isJapaneseLocale = $globalLocale.value._value === "ja";
  const currencySymbol = isJapaneseLocale ? "円" : "$";
  const rex = "\\d(?=(\\d{3})+" + (decimal > 0 ? "\\D" : "$") + ")";
  const convertedMoney = (money * $exchangeRate.value._value).toFixed(
    Math.max(0, ~~decimal)
  );

  const str = convertedMoney.replace(
    new RegExp(rex, "g"),
    "$&" + (s_delimiter || ",")
  );
  return isJapaneseLocale ? str + currencySymbol : currencySymbol + str;
};

const handleChangeNumber = (number) => number.toString().replaceAll(",", "");

const formatInputCurrency = (value, isEmpty = false, withoutComma = false) => {
  if (!value) return isEmpty ? "" : 0;
  let stringValue = String(value).replace(/[^0-9.+-]/g, "");
  if (withoutComma) return Math.round(parseFloat(stringValue) * 100) / 100;
  const decimalIndex = stringValue.indexOf(".");
  if (decimalIndex !== -1) stringValue = stringValue.slice(0, decimalIndex + 3);
  if (stringValue.indexOf(".00") !== -1)
    stringValue = stringValue.replace(".00", "");

  return stringValue.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
};

const handleErrorResponse = (error) => {
  const formattedErrors = {};
  try {
    for (const field in error) {
      if (error.hasOwnProperty(field) && field !== "success") {
        const errorValue = error[field];
        if (!errorValue.includes(",")) {
          formattedErrors[field] = { code: errorValue, options: {} };
        } else {
          const parts = errorValue.split(",").map((part) => part.trim());
          const code = parts[0];
          const options = {};
          parts.slice(1).forEach((option) => {
            const [key, value] = option.split(":").map((part) => part.trim());
            if (key && value)
              options[key] = isNaN(value) ? value : Number(value);
          });
          formattedErrors[field] = {
            code: code.split(":")[1],
            ...(Object.keys(options).length > 0 && { options }),
          };
        }
      }
    }
  } catch (err) {
    return formattedErrors;
  }

  return formattedErrors;
};

export const mixins = {
  screenLoading,
  startLoading,
  endLoading,
  checkEmptyWithOutZero,
  saveAccessToken,
  saveExpiredDateTime,
  saveUserId,
  saveRole,
  checkEmpty,
  formatCurrency,
  removeAccessToken,
  formatInputCurrency,
  handleErrorResponse,
  arrayChunk,
  notifyError,
  handleChangeNumber,
  notifySuccess,
  validateInvalidEmail,
  showDateTime,
};
