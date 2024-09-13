export const JA_LOCALE = 'ja';
export const EN_LOCALE = 'en';

export const RULES_VALIDATION = {
    EMAIL_FORMAT: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
    PHONE_FORMAT: /^\+?\d[\d()\-\s]*$/,
    FLOAT_FORMAT: /^\d+(\.\d+)?$/,
    ALLOWED_FILE_SIZE_2MB: 2 * 1024 * 1024,
    ALLOWED_FILE_SIZE_10MB: 10 * 1024 * 1024,
}

export const MODAL_TITLE = {
    ADD: 'add',
    UPDATE: 'update',
}

export const COOKIE_EXPIRE_TIME = import.meta.env.VITE_COOKIE_EXPIRE_TIME || 365;
export const FRONT_END_URL = import.meta.env.VITE_FRONTEND_URL || "http://localhost:9999";

export const DEPARTMENT_ACTIVE = 1;

export const MIN_CHARACTER = 1;
export const MAX_CHARACTER = 255;
export const MAX_SHORT_NUMBER = 20;
export const MAX_LONG_TEXT = 32000;
export const MAX_DESCRIPTION = 2000;

export const IMPORT_VALID_EXTENSIONS = [".xlsx"];
export const PDF_CONTENT_TYPE = /^application\/pdf/;
export const NUMBER_FORMAT = 2;
export const HTTP_NOT_FOUND = 404;
export const HTTP_FORBIDDEN = 403;
export const OFFSET = 0;
export const LIMIT = 20;
export const ENTER_KEY_CODE = 13;
export const MAX_MONTH = 12;
export const MIN_MONTH = 1;

export const MONTHS = {
    1: 'common.january_short',
    2: 'common.february_short',
    3: 'common.march_short',
    4: 'common.april_short',
    5: 'common.may_short',
    6: 'common.june_short',
    7: 'common.july_short',
    8: 'common.august_short',
    9: 'common.september_short',
    10: 'common.october_short',
    11: 'common.november_short',
    12: 'common.december_short',
}
