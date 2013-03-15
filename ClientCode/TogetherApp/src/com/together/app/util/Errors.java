package com.together.app.util;

import com.together.app.R;

public class Errors {
    public static final String ERROR_SINA_PREFIX = "A";
    public static final String ERROR_TENCENT_PREFIX = "B";
    public static final String ERROR_SUCCESS = "0000";

    public static final String ERROR_LOCAL_BEGIN = "10000";
    public static final String ERROR_NETWORK = "10001";
    public static final String ERROR_RESPONSE_CODE = "10002";
    public static final String ERROR_RESPONSE_FORMAT = "10003";
    public static final String ERROR_ENCRYPTION_KEY_TIMEOUT = "10004";

    public static int getLocalErrorMsg(String error) {
        if (error.equalsIgnoreCase(Errors.ERROR_NETWORK)) {
            return R.string.error_network;
        } else if (error.equalsIgnoreCase(Errors.ERROR_RESPONSE_FORMAT)) {
            return R.string.error_server;
        } else if (error.equalsIgnoreCase(Errors.ERROR_ENCRYPTION_KEY_TIMEOUT)) {
            return R.string.error_key_time_out;
        }
        return R.string.error_unrecognized;
    }
}
