package com.together.app.net;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.together.app.data.DataEngine;
import com.together.app.data.Event;
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

    public static final String getLoginMsg(int loginType, String openid,
            String name, String avatar) throws JSONException {
        JSONObject result = new JSONObject();
        addHead(result, "1000");
        JSONObject body = new JSONObject();
        result.put(Constant.KEY_BODY, body);
        body.put(Constant.KEY_USER_TYPE, loginType);
        body.put(Constant.KEY_OPENID, openid);
        body.put(Constant.KEY_NAME, name);
        body.put(Constant.KEY_AVATAR, avatar);
        return result.toString();
    }

    public static final String getRegisterMsg(String account, String password,
            String name, int sex) throws JSONException {
        JSONObject result = new JSONObject();
        addHead(result, "1001");
        JSONObject body = new JSONObject();
        result.put(Constant.KEY_BODY, body);
        body.put(Constant.KEY_ACCOUNT, account);
        body.put(Constant.KEY_PASSWORD, password);
        body.put(Constant.KEY_NAME, name);
        body.put(Constant.KEY_SEX, sex);
        return result.toString();
    }

    public static final String getEventListMsg(int eventType, int areaId,
            int costType, long startDate, long endDate) throws JSONException {
        JSONObject result = new JSONObject();
        addHead(result, "1003");
        JSONObject body = new JSONObject();
        result.put(Constant.KEY_BODY, body);
        body.put(Constant.KEY_EVENT_TYPE, eventType);
        body.put(Constant.KEY_AREA_ID, areaId);
        body.put(Constant.KEY_COST_TYPE, costType);
        body.put(Constant.KEY_START_DATE, startDate);
        body.put(Constant.KEY_END_DATE, endDate);
        return result.toString();
    }

    public static final String getEventDetailMsg(int eventId)
            throws JSONException {
        JSONObject result = new JSONObject();
        addHead(result, "1004");
        JSONObject body = new JSONObject();
        result.put(Constant.KEY_BODY, body);
        body.put(Constant.KEY_EVENT_ID, eventId);
        return result.toString();
    }

    public static final void parseEventList(JSONObject resp)
            throws JSONException {
        DataEngine engine = DataEngine.getInstance();
        JSONArray array = resp.getJSONArray(Constant.KEY_EVENT_LIST);
        for (int i = 0; i < array.length(); ++i) {
            engine.addOrUpdateEvent(Event.initFromList(array.getJSONObject(i)));
        }
    }

    public static final void parseEventDetail(JSONObject resp)
            throws JSONException {
        Event e = Event.initFromDetail(resp);
        DataEngine.getInstance().addOrUpdateEvent(e);
    }

    private static final void addHead(JSONObject json, String transType)
            throws JSONException {
        JSONObject head = new JSONObject();
        json.put(Constant.KEY_HEAD, head);
        head.put(Constant.KEY_TRANS_TIME_SOURCE, currentTime());
        head.put(Constant.KEY_TRANS_NO_SOURCE, currentNoTrans());
        head.put(Constant.KEY_TRANS_TYPE, transType);
    }

    private static final String currentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    private static final String currentNoTrans() {
        SimpleDateFormat formatter = new SimpleDateFormat("ddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        str = getIncreasedSerialString() + str;
        return str;
    }

    private static final String getIncreasedSerialString() {
        return formatNumber(series++, "0000");
    }

    private static final String formatNumber(int num, String formatAs) {
        DecimalFormat df = new DecimalFormat(formatAs);
        String str2 = df.format(num);
        return str2;
    }
}
