import { ElLoading, ElNotification } from "element-plus";
import Cookies from "js-cookie";
import moment from "moment";
import { RULES_VALIDATION } from "@/constants/application";

moment.locale(Cookies.get("CurrentLanguage") || "ja");

const mixins = {
  data() {
    return {
      screenLoading: false,
    };
  },
  methods: {
    startLoading() {
      this.screenLoading = ElLoading.service({
        lock: true,
        text: "Loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
    },
    async endLoading() {
      if (!this.checkEmpty(this.screenLoading)) {
        await this.screenLoading.close();
        // this.screenLoading = false;
      }
    },
    strLimit(value, size) {
      if (!value) return "";
      value = value.toString();

      if (value.length <= size) {
        return value;
      }
      return value.substr(0, size) + "...";
    },
    objectEmpty(obj) {
      if (this.checkEmpty(obj)) {
        return true;
      }
      const keys = Object.keys(obj);
      // eslint-disable-next-line eqeqeq
      if (keys.length == 0) {
        return true;
      }

      let status = true;
      keys.forEach((item) => {
        if (!this.checkEmpty(obj[item])) {
          status = false;
        }
      });

      return status;
    },
    checkEmpty(value) {
      // Array
      if (Array.isArray(value)) {
        // eslint-disable-next-line eqeqeq
        return value.length == 0;
      }

      // Object
      if (value && typeof value === "object" && value.constructor === Object) {
        // eslint-disable-next-line eqeqeq
        return Object.keys(value).length == 0;
      }

      // string
      if (typeof value === "string" || value instanceof String) {
        // eslint-disable-next-line eqeqeq
        return value.trim().length == 0;
      }

      // Null & undefined
      if (value === null || typeof value === "undefined") {
        return true;
      }

      // Number
      if (value === 0) {
        return true;
      }
      return false;
    },
    checkEmptyWithOutZero(value) {
      // Array
      if (Array.isArray(value)) {
        // eslint-disable-next-line eqeqeq
        return value.length == 0;
      }

      // Object
      if (value && typeof value === "object" && value.constructor === Object) {
        // eslint-disable-next-line eqeqeq
        return Object.keys(value).length == 0;
      }

      // string
      if (typeof value === "string" || value instanceof String) {
        // eslint-disable-next-line eqeqeq
        return value.trim().length == 0;
      }

      // Null & undefined
      if (value === null || typeof value === "undefined" || value === "") {
        return true;
      }

      return false;
    },
    saveAccessToken(value) {
      Cookies.set("access_token", value, {
        expires: 90,
      });
    },
    saveExpiredDateTime(value) {
      Cookies.set("expired_date_time", value, {
        expires: 90,
      });
    },
    saveUserId(value) {
      Cookies.set("user_id", value, {
        expires: 90,
      });
    },
    saveRole(role) {
      Cookies.set("role", role, {
        expires: 90,
      });
    },
    removeAccessToken() {
      Cookies.remove("access_token");
    },
    arrayChunk(array, size) {
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
    },
    notifyError(messages, useHTML = false, notify = null) {
      let message = "";
      if (useHTML && Array.isArray(messages)) {
        messages.forEach((item) => {
          message += `<p>${item}</p>`;
        });
      } else if (useHTML && typeof messages === "object") {
        for (let key in messages) {
          message += `<p>${this.$t(`${messages[key]}`)}</p>`;
        }
      } else {
        message = messages;
      }

      // Use provided notify or default to ElNotification
      let notifyObj = notify ?? ElNotification;

      notifyObj.error({
        message,
        duration: 2000,
        position: "top-right",
        dangerouslyUseHTMLString: useHTML,
        showClose: false,
      });
    },
    notifySuccess(messages, useHTML = false, notify = null) {
      let message = "";
      if (useHTML && Array.isArray(messages)) {
        messages.forEach((item) => {
          message += `<p>${item}</p>`;
        });
      } else {
        message = messages;
      }

      // Use provided notify or default to ElNotification
      let notifyObj = notify ?? ElNotification;

      notifyObj.success({
        message,
        duration: 2000,
        position: "top-right",
        dangerouslyUseHTMLString: useHTML,
        showClose: false,
      });
    },

    validateInvalidEmail(email) {
      const emailRegex = RULES_VALIDATION.EMAIL_FORMAT;
      return emailRegex.test(email);
    },

    showDateTime(dateTime, formatString = undefined) {
      if (this.checkEmpty(dateTime)) return;
      if (!formatString) {
        formatString = process.env.VUE_APP_FORMATDATE;
      }
      return moment(dateTime, [
        "YYYY/MM",
        "YYYY/MM/DD",
        "YYYY-MM-DD",
        "YYYY-MM-DD HH:mm:ss",
        "YYYY/MM/DD HH:mm:ss",
        "YYYY-MM-DD HH:mm",
        "YYYY/MM/DD HH:mm",
      ]).format(formatString);
    },
    getDayOfMonth(type, offset = 0, formatString = undefined) {
      let currentDate = moment().add(offset, "months");
      switch (type) {
        case "first":
          currentDate.startOf("month");
          break;
        case "mid":
          currentDate
            .startOf("month")
            .add(Math.floor(currentDate.daysInMonth() / 2), "days");
          break;
        case "last":
          currentDate.endOf("month");
          break;
        default:
          throw new Error(
            'Invalid type. Choose from "first", "mid", or "last".'
          );
      }
      return this.showDateTime(currentDate.format("YYYY-MM-DD"), formatString);
    },
    splitEndLine(data) {
      return data.split(/(?:\r\n|\r|\n|↵)/g);
    },
    resizeImage(settings) {
      let file = settings.file;
      let maxSize = settings.maxSize;
      let reader = new FileReader();
      let image = new Image();
      let canvas = document.createElement("canvas");
      let dataURItoBlobA = function (dataURI) {
        let bytes =
          dataURI.split(",")[0].indexOf("base64") >= 0
            ? atob(dataURI.split(",")[1])
            : unescape(dataURI.split(",")[1]);
        let mime = dataURI.split(",")[0].split(":")[1].split(";")[0];
        let max = bytes.length;
        let ia = new Uint8Array(max);
        for (let i = 0; i < max; i++) ia[i] = bytes.charCodeAt(i);
        return new Blob([ia], { type: mime });
      };
      let resize = function () {
        let width = image.width;
        let height = image.height;
        if (width > height) {
          if (width > maxSize) {
            height *= maxSize / width;
            width = maxSize;
          }
        } else {
          if (height > maxSize) {
            width *= maxSize / height;
            height = maxSize;
          }
        }
        canvas.width = width;
        canvas.height = height;
        canvas.getContext("2d").drawImage(image, 0, 0, width, height);
        let dataUrl = canvas.toDataURL("image/jpeg");
        return dataURItoBlobA(dataUrl);
      };
      return new Promise(function (ok, no) {
        if (!file.type.match(/image.*/)) {
          no(new Error("Not an image"));
          return;
        }
        reader.onload = function (readerEvent) {
          image.onload = function () {
            return ok(resize());
          };
          image.src = readerEvent.target.result;
        };
        reader.readAsDataURL(file);
      });
    },
    urlParse(obj) {
      let str = [];
      // eslint-disable-next-line no-prototype-builtins
      for (let p in obj) {
        // eslint-disable-next-line no-prototype-builtins
        if (obj.hasOwnProperty(p)) {
          str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        }
      }
      return str.join("&");
    },
    removeEmptyObject(obj) {
      let objClone = obj;
      Object.entries(obj).forEach(([key, value]) => {
        if (this.checkEmpty(value)) {
          delete objClone[key];
        }
      });

      return objClone;
    },
    convertParams(obj) {
      let formData = new FormData();
      Object.entries(obj).forEach(([key, value]) => {
        if (value != undefined && value != null && value !== "") {
          if (Array.isArray(value)) {
            value = JSON.stringify(value);
          }
          formData.append(key, value);
        }
      });
      return formData;
    },
    base64ToArrayBuffer(base64) {
      const binaryString = window.atob(base64); // Comment this if not using base64
      const bytes = new Uint8Array(binaryString.length);
      return bytes.map((byte, i) => binaryString.charCodeAt(i));
    },
    createAndDownloadBlobFile(body, filename, extension = "csv") {
      const blob = new Blob([body]);
      const fileName = `${filename}.${extension}`;
      if (navigator.msSaveBlob) {
        // IE 10+
        navigator.msSaveBlob(blob, fileName);
      } else {
        const link = document.createElement("a");
        // Browsers that support HTML5 download attribute
        if (link.download !== undefined) {
          const url = URL.createObjectURL(blob);
          link.setAttribute("href", url);
          link.setAttribute("download", fileName);
          link.style.visibility = "hidden";
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        }
      }
    },
    diffDate(date, dateCompare, type = "days") {
      return moment(date).diff(moment(dateCompare), type);
    },
    diffDateTrip(date, dateCompare, type = "days") {
      if (date === null || dateCompare === null) return 0;
      return Number(moment(date).diff(moment(dateCompare), type)) + 1;
    },
    dataURItoBlob(dataURI) {
      const binary = atob(dataURI.split(",")[1]);
      let array = [];
      for (let i = 0; i < binary.length; i++) {
        array.push(binary.charCodeAt(i));
      }
      return new Blob([new Uint8Array(array)], { type: "image/jpeg" });
    },
    formatNumber(number, unit = 2) {
      return number.toFixed(unit).replace(/\d(?=(\d{3})+\.)/g, "$&,");
    },
    isNumeric(n) {
      return !isNaN(parseFloat(n)) && isFinite(n);
    },
    isValidEmail(string) {
      let res = string.match(
        /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/i
      );
      if (res == null) {
        return false;
      } else {
        return true;
      }
    },
    getFullPathImage(image_path, base_path = "") {
      if (!base_path) base_path = process.env.AWS_S3_URL;
      if (this.checkEmpty(image_path)) {
        return false;
      }

      return base_path + image_path;
    },
    randomString(len = 7) {
      const randomStr = Math.random().toString(36).substring(len);
      return randomStr;
    },
    formatCurrency(money, decimal = 0, s_delimiter = ",", currency = "円") {
      if (!money) return "";

      const rex = "\\d(?=(\\d{3})+" + (decimal > 0 ? "\\D" : "$") + ")",
        num = money.toFixed(Math.max(0, ~~decimal));

      const str = num.replace(
        new RegExp(rex, "g"),
        "$&" + (s_delimiter || ",")
      );

      if (currency === "$") {
        return currency + str;
      }

      return str + currency;
    },
    cloneObject(obj) {
      return JSON.parse(JSON.stringify(obj));
    },
    objToArray(obj, keyField, valueField) {
      // Object
      if (obj && typeof obj === "object" && obj.constructor === Object) {
        const response = [];
        Object.keys(obj).forEach((k) => {
          const newObj = {};
          let vl = k;
          if (!isNaN(Number(vl))) {
            vl = parseInt(vl);
          }
          newObj[keyField] = vl;
          newObj[valueField] = obj[k];
          response.push(newObj);
        });
        return response;
      }
      return [];
    },
    labelLocale(data) {
      let label = "";
      const locale = this.$i18n.locale;
      if (locale === "ja") {
        if (data?.jp_name) label = data.jp_name;
        if (data?.ja_name) label = data.ja_name;
      }

      if (locale === "en" && data?.en_name) {
        label = data.en_name;
      }

      return label;
    },
    strLen(str = "") {
      if (str) return String(str).split("").length;

      return 0;
    },
    getNotificationTypeLabel(id) {
      if (!id) return "";

      return this.$t("information.type." + id);
    },
    getTripTypeLabel(id) {
      if (!id) return "";

      return this.$t("trip.type." + id);
    },
    getDestinationTypeLabel(id) {
      if (!id) return "";

      return this.$t("trip.destination_type." + id);
    },
    getRequirementStatusLabel(id) {
      if (!id) return "";

      return this.$t("requirement.status." + id);
    },
    getInputTypeLabel(id) {
      if (!id) return "";

      return this.$t("input.type." + id);
    },
    getInputStatusLabel(id) {
      if (!id) return "";

      return this.$t("input.status." + id);
    },
    getRequestTypeLabel(id) {
      if (!id) return "";

      return this.$t("trip.request_type." + id);
    },
    getCostItemTypeLabel(id) {
      if (!id) return this.$t("trip.cost_item_type.5");

      return this.$t("trip.cost_item_type." + id);
    },
    isTypeImage(type) {
      return type.match(/image-*/);
    },
    isTypePDF(type) {
      return type.match(/pdf*/);
    },
    isTypeCSV(type) {
      return type.match(/csv-*/);
    },
    isTypeVideo(type) {
      return type.match(/video-*/);
    },
    splitThousandYen(number) {
      return number != undefined
        ? this.$t("common.money") +
            String(number).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,")
        : "";
    },
    splitThousandNumber(number) {
      return number != undefined
        ? String(number).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,")
        : "";
    },
    convertToInteger(data) {
      if (data == "") return null;
      return parseInt(String(data).replaceAll(",", ""));
    },
    //Check if array is subset
    isSubsetCountryArray(parentArray, subsetArray) {
      if (
        (this.checkEmpty(parentArray) && this.checkEmpty(subsetArray)) ||
        (!this.checkEmpty(parentArray) && this.checkEmpty(subsetArray))
      )
        return true;
      if (
        (this.checkEmpty(parentArray) && !this.checkEmpty(subsetArray)) ||
        subsetArray.length > parentArray.length
      )
        return false;

      let flag = true;
      subsetArray.every((ele) => {
        if (
          !this.checkEmpty(ele.parent) &&
          parentArray.findIndex((parentEle) => parentEle.id == ele.id) == -1
        ) {
          flag = false;
          return false;
        } else if (
          this.checkEmpty(ele.parent) &&
          parentArray.findIndex((parentEle) => parentEle.name == ele.name) == -1
        ) {
          flag = false;
          return false;
        }
        return false;
      });

      return flag;
    },
    sortCountryByName(listCountry) {
      const locale = this.$i18n.locale;
      if (locale === "ja") {
        listCountry.sort((a, b) => {
          if (a["ja_name"] < b["ja_name"]) return -1;
          if (a["ja_name"] > b["ja_name"]) return 1;
          return 0;
        });
      } else {
        listCountry.sort((a, b) => {
          if (a["en_name"] < b["en_name"]) return -1;
          if (a["en_name"] > b["en_name"]) return 1;
          return 0;
        });
      }

      listCountry.sort(function (x, y) {
        return x["code"] == "japan" ? -1 : y["code"] == "japan" ? 1 : 0;
      });

      return listCountry;
    },
    handleDownloadCSV(file_path = false) {
      if (!file_path) return;

      window.open(file_path, "_blank").focus();
    },
    scrollIntoDivCustom(elementId = "") {
      if (elementId == "") return;
      let ele = document.getElementById(`${elementId}`);
      if (!this.checkEmpty(ele)) {
        ele.scrollIntoView({
          block: "start",
          behavior: "smooth",
        });
      }
    },
    fallbackCopyTextToClipboard(text) {
      var textArea = document.createElement("textarea");
      textArea.value = text;
      textArea.style.top = "0";
      textArea.style.left = "0";
      textArea.style.position = "fixed";

      document.body.appendChild(textArea);
      textArea.focus();
      textArea.select();

      document.body.removeChild(textArea);
    },
    copyTextToClipboard(text) {
      if (!navigator.clipboard) {
        this.fallbackCopyTextToClipboard(text);
        return;
      }
      navigator.clipboard.writeText(text);
    },
    isEmail(email) {
      return String(email)
        .toLowerCase()
        .match(
          /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        );
    },
    isPhoneNumber(phone) {
      var regex = /^[0-9-]{12,13}$|^[0-9-]{12}$/;
      return String(phone).match(regex);
    },
    isJapanesePhoneNumber(phone) {
      var regex =
        /^(0([1-9]{1}-?[1-9]\d{3}|[1-9]{2}-?\d{3}|[1-9]{2}\d{1}-?\d{2}|[1-9]{2}\d{2}-?\d{1})-?\d{4}|0[789]0-?\d{4}-?\d{4}|050-?\d{4}-?\d{4})$/;
      return String(phone).match(regex);
    },
    isZipCode(code) {
      var regex = /^[0-9]{3}-?[0-9]{4}$/;
      return String(code).match(regex);
    },
    isInt(n) {
      return Number(n) === n && n % 1 === 0;
    },
    // convertNumberFullToHalf(number) {
    //   number = number.replace(/[^0-9０-９]/g, '');
    //   number = String(number).replace(/[\uFF10-\uFF19]/g, function (m) {
    //     return String.fromCharCode(m.charCodeAt(0) - 0xfee0);
    //   });

    //   return number;
    // },
    convertNumberFullToHalfCommon(number) {
      number = number.replace(/[^0-9０-９.．]/g, "");
      number = String(number).replace(/[\uFF10-\uFF19]/g, function (m) {
        return String.fromCharCode(m.charCodeAt(0) - 0xfee0);
      });

      return number;
    },
    //Get child data inside object using dataName contain dot split
    getDataBySplitNameCommon(dataName, dataIndex = -1) {
      let dataNameArr = dataName.split(".");
      const splitLength = dataName.split(".").length;
      if (splitLength == 3) {
        return dataIndex == -1
          ? this[`${dataNameArr[0]}`][`${dataNameArr[1]}`][
              `${dataNameArr.pop()}`
            ]
          : this[`${dataNameArr[0]}`][`${dataNameArr[1]}`][dataIndex][
              `${dataNameArr.pop()}`
            ];
        //Default with 2 level of split name
      } else {
        return dataIndex == -1
          ? this[`${dataNameArr[0]}`][`${dataNameArr.pop()}`]
          : this[`${dataNameArr[0]}`][dataIndex][`${dataNameArr.pop()}`];
      }
    },
    //Set data value for variable get from split data name string
    setDataSplitNameCommon(dataName, value, index = -1) {
      let dataNameArr = dataName.split(".");
      const totalChildLevel = dataName.split(".").length;
      switch (totalChildLevel) {
        case 3:
          if (index != -1) {
            this[`${dataNameArr[0]}`][`${dataNameArr[1]}`][index][
              `${dataNameArr.pop()}`
            ] = value;
          } else {
            this[`${dataNameArr[0]}`][`${dataNameArr[1]}`][
              `${dataNameArr.pop()}`
            ] = value;
          }
          break;
        default:
          if (index != -1) {
            this[`${dataNameArr[0]}`][index][`${dataNameArr.pop()}`] = value;
          } else {
            this[`${dataNameArr[0]}`][`${dataNameArr.pop()}`] = value;
          }
      }
    },

    //
    convertDataCommon(
      dataName,
      haveDataIndex = false,
      index = -1,
      splitDataName = false
    ) {
      //Check if current task needs update object element having index (update Object Array)
      if (haveDataIndex) {
        let data = "";
        if (splitDataName) {
          //Number of child level object we need to access
          const childDotSplitSum = dataName.split(".").length;
          switch (childDotSplitSum) {
            case 3:
              data = this.getDataBySplitNameCommon(dataName, index);
              break;
            //Default we have 2 child level need to access
            default:
              data = this.getDataBySplitNameCommon(dataName, index);
          }
        } else {
          data = this[`${dataName}`][index];
        }

        if (this.checkEmpty(data)) return;
        if (String(data).includes(".") || String(data).includes("．")) {
          if (String(data).includes("．")) {
            data = data.replaceAll("．", ".");
          }
          //Return a string contain ',' split
          const returnStr =
            data == 0
              ? 0
              : String(parseFloat(data).toFixed(2))
                  .replace(/^(?!00[^0])0/, "")
                  .replace(/\B(?=(\d{3})+(?!\d))/g, ",");
          splitDataName
            ? this.setDataSplitNameCommon(dataName, returnStr, index)
            : (this[`${dataName}`][index] = returnStr);
        } else {
          data = data.replaceAll(",", "");
          //Return a string contain ',' split
          const returnStr =
            data == 0
              ? 0
              : data
                  .replace(/(\..*?)\..*/g, "$1")
                  .replace(/^(?!00[^0])0/, "")
                  .replace(/\B(?=(\d{3})+(?!\d))/g, ",");
          splitDataName
            ? this.setDataSplitNameCommon(dataName, returnStr, index)
            : (this[`${dataName}`][index] = returnStr);
        }

        return;
      }

      let data = splitDataName
        ? this.getDataBySplitNameCommon(dataName)
        : this[`${dataName}`];
      if (this.checkEmptyWithOutZero(data)) return;
      if (String(data).includes(".") || String(data).includes("．")) {
        if (String(data).includes("．")) {
          data = data.replaceAll("．", ".");
        }
        //Return a string contain ',' split
        const returnStr =
          data == 0
            ? 0
            : String(parseFloat(data).toFixed(2))
                .replace(/^(?!00[^0])0/, "")
                .replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        splitDataName
          ? this.setDataSplitNameCommon(dataName, returnStr)
          : (this[`${dataName}`] = returnStr);
      } else {
        data = String(data).replaceAll(",", "");
        //Return a string contain ',' split
        const returnStr =
          data == 0
            ? 0
            : data
                .replace(/(\..*?)\..*/g, "$1")
                .replace(/^(?!00[^0])0/, "")
                .replace(/\B(?=(\d{3})+(?!\d))/g, ",");

        splitDataName
          ? this.setDataSplitNameCommon(dataName, returnStr)
          : (this[`${dataName}`] = returnStr);
      }
    },
    isFullSizeCharacter(value) {
      for (let i = 0; i < value?.length; i++) {
        const code = value.charCodeAt(i);
        if (
          (code >= 0x0020 && code <= 0x1fff) ||
          (code >= 0xff61 && code <= 0xff9f)
        ) {
          return false;
        }
      }

      return !!(value && "string" === typeof value) && true;
    },
    compareObject(objAfter, objBefore) {
      return JSON.stringify(objAfter) === JSON.stringify(objBefore);
    },
    compareData(array1, array2) {
      let array1Clone = JSON.stringify(array1);
      let array2Clone = JSON.stringify(array2);
      array1Clone = array1Clone.replaceAll('"', "");
      array2Clone = array2Clone.replaceAll('"', "");

      return array1Clone === array2Clone;
    },
    compareArray(array1, array2) {
      if (!this.checkEmpty(array1) || !this.checkEmpty(array2)) {
        let result =
          array1?.length == array2?.length &&
          array1.every(function (element, index) {
            return JSON.stringify(element) === JSON.stringify(array2[index]);
          });
        return result;
      } else {
        return true;
      }
    },
    convertToFloatNumber(data) {
      if (this.checkEmpty(data)) return (0).toFixed(2);
      if (String(data).includes(".") || String(data).includes("．")) {
        if (String(data).includes("．")) {
          data = String(data).replaceAll("．", ".");
        }
        return parseFloat(String(data).replaceAll(",", "")).toFixed(2);
      } else {
        data = String(data).replaceAll(",", "");
        return parseFloat(String(data).replaceAll(",", "")).toFixed(2);
      }
    },
    isIntegerCustom(N) {
      // Convert float value
      // of N to integer
      let X = Math.floor(N);
      let temp2 = N - X;

      // If N is not equivalent
      // to any integer
      if (temp2 > 0) {
        return false;
      }
      return true;
    },
    urlify(text) {
      if (!text) return "";

      var urlRegex = /(https?:\/\/[^\s]+)/g;
      return text.replace(urlRegex, function (url) {
        return '<a href="' + url + '" target="_blank">' + url + "</a>";
      });
    },
    isValidTime(string) {
      let res = string.match(/^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/g);
      return !(res == null);
    },
    autoFormatTime(time = "") {
      time = time.replace(/[^0-9０-９:：]/g, "");
      time = String(time).replace(/[\uFF10-\uFF19]/g, function (m) {
        return String.fromCharCode(m.charCodeAt(0) - 0xfee0);
      });
      if (time.length == 2) {
        time = time + ":";
      } else if (time.length > 2 && !time.includes(":")) {
        time = time.substring(0, 2) + ":" + time.substring(2, time.length);
      } else if (time.length > 2 && time.includes(":")) {
        let timeData = time.split(":");
        let hour = timeData[0].length > 1 ? timeData[0] : `0${timeData[0]}`;
        time = `${String(hour)}:${timeData[1]}`;
      }

      return time;
    },

    formatCurrencyRate(value) {
      if (!value) return 0;
      let stringValue = String(value);
      if (stringValue.indexOf(".00") !== -1) {
        stringValue = stringValue.replace(".00", "");
      }
      return stringValue.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },

    formatInputCurrency(value, isEmpty = false, withoutComma = false) {
      if (!value) {
        return isEmpty ? "" : 0;
      }
      let stringValue = String(value).replace(/[^0-9.+-]/g, "");
      if (withoutComma) return Math.round(parseFloat(stringValue) * 100) / 100;
      const decimalIndex = stringValue.indexOf(".");
      if (decimalIndex !== -1) {
        stringValue = stringValue.slice(0, decimalIndex + 3);
      }
      if (stringValue.indexOf(".00") !== -1) {
        stringValue = stringValue.replace(".00", "");
      }

      return stringValue.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },

    setDefaultValueInputFile(elementId, filename) {
      const fileInput = document.getElementById(elementId);
      let file = new File([], filename);
      if (filename instanceof File) {
        file = filename;
      }

      const dataTransfer = new DataTransfer();
      dataTransfer.items.add(file);

      return (fileInput.files = dataTransfer.files);
    },

    handleChangeNumber(e, max = 10) {
      let value = e.target.value.replaceAll(",", "");
      if (value.length > max) value = value.slice(0, max);
      e.target.value = this.formatInputCurrency(value);
    },
    ucfirst(string) {
      if (this.checkEmpty(string)) {
        return "";
      }

      return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
    },
    handleErrorResponse(error) {
      const formattedErrors = {};
      // Iterate over each error field in the response
      for (const field in error) {
        if (error.hasOwnProperty(field)) {
          const errorValue = error[field];

          // Check if the error value is just a simple code (e.g., "E-CM-004")
          if (!errorValue.includes(",")) {
            formattedErrors[field] = {
              code: errorValue,
              options: {}, // Store the error code directly
            };
          } else {
            // Split the string by comma and trim spaces for more complex errors
            const parts = errorValue.split(",").map((part) => part.trim());

            // Extract the code (first part) and options (remaining parts)
            const code = parts[0];
            const options = {};

            // Process the remaining parts to extract key-value pairs (like min: 8)
            parts.slice(1).forEach((option) => {
              const [key, value] = option.split(":").map((part) => part.trim());
              if (key && value) {
                options[key] = isNaN(value) ? value : Number(value); // Convert numeric strings to numbers
              }
            });

            // Construct the final structure
            formattedErrors[field] = {
              code: code.split(":")[1], // Extract the error code part
              ...(Object.keys(options).length > 0 && { options }), // Add options only if they exist
            };
          }
        }
      }

      return formattedErrors;
    },
  },
};

export default mixins;
