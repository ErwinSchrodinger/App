package com.together.app.net;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.together.app.util.Constant;

public class MessageFactory {

    public static int series = 0;

    public static final String getLoginMsg(int loginType, String account,
            String password) throws JSONException {
        JSONObject result = new JSONObject();
        addHead(result, "1000");
        JSONObject body = new JSONObject();
        result.put(Constant.KEY_BODY, body);
        body.put(Constant.KEY_USER_TYPE, loginType);
        body.put(Constant.KEY_ACCOUNT, account);
        body.put(Constant.KEY_PASSWORD, password);
        return result.toString();
    }

    public static final String getLoginMsg(int loginType, String openid)
            throws JSONException {
        JSONObject result = new JSONObject();
        addHead(result, "1000");
        JSONObject body = new JSONObject();
        result.put(Constant.KEY_BODY, body);
        body.put(Constant.KEY_USER_TYPE, loginType);
        body.put(Constant.KEY_OPENID, openid);
        return result.toString();
    }

    private static final void addHead(JSONObject json, String transType)
            throws JSONException {
        JSONObject head = new JSONObject();
        json.put(Constant.KEY_HEAD, head);
        head.put(Constant.KEY_TRANS_TIME_SOURCE, currentTime());
        head.put(Constant.KEY_TRANS_NO_SOURCE, currentNoTrans());
        head.put(Constant.KEY_TRANS_TYPE, transType);
    }

    private static String currentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    private static String currentNoTrans() {
        SimpleDateFormat formatter = new SimpleDateFormat("ddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        str = getIncreasedSerialString() + str;
        return str;
    }

    private static String getIncreasedSerialString() {
        return formatNumber(series++, "0000");
    }

    public static String formatNumber(int num, String formatAs) {
        DecimalFormat df = new DecimalFormat(formatAs);
        String str2 = df.format(num);
        return str2;
    }
}
