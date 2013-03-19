package com.together.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.together.app.data.EventSearchCondition;
import com.together.app.data.TencentAccessToken;
import com.weibo.sdk.android.Oauth2AccessToken;

/**
 * 该类用于保存Oauth2AccessToken到sharepreference，并提供读取功能
 * 
 * @author xiaowei6@staff.sina.com.cn
 * 
 */
public class SharePreferencesKeeper {
    private static final String PREFERENCES_NAME = "token_preference";

    private static final String KEY_SINA_TOKEN = "sina_token";
    private static final String KEY_SINA_TIME = "sina_expiresTime";

    private static final String KEY_TENCENT_TOKEN = "tencent_token";
    private static final String KEY_TENCENT_TIME = "tencent_expiresTime";
    private static final String KEY_TENCENT_OPENID = "tencent_openid";

    private static final String KEY_SEARCH_EVENT_TYPE = "event_type";
    private static final String KEY_SEARCH_AREA_ID = "area_id";
    private static final String KEY_SEARCH_COST_TYPE = "cost_type";
    private static final String KEY_SEARCH_START_DATE = "start_date";
    private static final String KEY_SEARCH_END_DATE = "end_date";

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void keepSinaAccessToken(Oauth2AccessToken token) {
        SharedPreferences pref = mContext.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(KEY_SINA_TOKEN, token.getToken());
        editor.putLong(KEY_SINA_TIME, token.getExpiresTime());
        editor.commit();
    }

    public static void keepTencentAccessToken(TencentAccessToken token) {
        SharedPreferences pref = mContext.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(KEY_TENCENT_TOKEN, token.getToken());
        editor.putString(KEY_TENCENT_TIME, token.getExpiresTime());
        editor.putString(KEY_TENCENT_OPENID, token.getOpenID());
        editor.commit();
    }

    public static Oauth2AccessToken readSinaAccessToken() {
        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences pref = mContext.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_APPEND);
        token.setToken(pref.getString(KEY_SINA_TOKEN, ""));
        token.setExpiresTime(pref.getLong(KEY_SINA_TIME, 0));
        return token;
    }

    public static TencentAccessToken readTencentAccessToken() {
        TencentAccessToken token = new TencentAccessToken();
        SharedPreferences pref = mContext.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_APPEND);
        token.setToken(pref.getString(KEY_TENCENT_TOKEN, ""));
        token.setExpiresTime(pref.getString(KEY_TENCENT_TIME, "0"));
        token.setOpenID(pref.getString(KEY_TENCENT_OPENID, ""));
        return token;
    }

    public static void clearSina() {
        SharedPreferences pref = mContext.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(KEY_SINA_TOKEN, "");
        editor.putLong(KEY_SINA_TIME, 0);
        editor.commit();
    }

    public static void clearTencent() {
        SharedPreferences pref = mContext.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putString(KEY_TENCENT_TOKEN, "");
        editor.putString(KEY_TENCENT_TIME, "0");
        editor.putString(KEY_TENCENT_OPENID, "");
        editor.commit();
    }

    public static void clearAll() {
        SharedPreferences pref = mContext.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public static EventSearchCondition getLastSearchCondition() {
        EventSearchCondition condition = new EventSearchCondition();
        SharedPreferences pref = mContext.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_APPEND);
        condition.setEventType(pref.getInt(KEY_SEARCH_EVENT_TYPE,
                Constant.SEARCH_ALL));
        condition.setAreaID(pref
                .getInt(KEY_SEARCH_AREA_ID, Constant.SEARCH_ALL));
        condition.setCostType(pref.getInt(KEY_SEARCH_COST_TYPE,
                Constant.SEARCH_ALL));
        condition.setStartDate(pref.getLong(KEY_SEARCH_START_DATE,
                Constant.SEARCH_ALL));
        condition.setEndDate(pref.getLong(KEY_SEARCH_END_DATE,
                Constant.SEARCH_ALL));
        return condition;
    }

    public static void keepSearchCondition(EventSearchCondition condition) {
        SharedPreferences pref = mContext.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_APPEND);
        Editor editor = pref.edit();
        editor.putInt(KEY_SEARCH_EVENT_TYPE, condition.getEventType());
        editor.putInt(KEY_SEARCH_AREA_ID, condition.getAreaID());
        editor.putInt(KEY_SEARCH_COST_TYPE, condition.getCostType());
        editor.putLong(KEY_SEARCH_START_DATE, condition.getStartDate());
        editor.putLong(KEY_SEARCH_END_DATE, condition.getEndDate());
        editor.commit();
    }
}
