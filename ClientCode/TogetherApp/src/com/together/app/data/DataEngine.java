package com.together.app.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.together.app.util.Constant;
import com.together.app.util.SinaConstant;
import com.together.app.util.TencentConstant;

public class DataEngine {
    private static DataEngine sInstance = new DataEngine();

    private int mCurrentUserType;
    private SinaInfo mSinaInfo;
    private TencentInfo mTencentInfo;

    private TogetherInfo mTogetherInfo;

    private class UserInfo {
        String uid;
        String name;
        String avatar;
        // String avatarLarge;
    }

    public class SinaInfo extends UserInfo {
        int gender;
    }

    public class TencentInfo extends UserInfo {
    }

    public class TogetherInfo extends UserInfo {
        int gender;
        long lastLoginTime;
    }

    public static synchronized DataEngine getInstance() {
        return sInstance;
    }

    public synchronized void setSinaUID(String id) {
        mSinaInfo.uid = id;
    }

    public synchronized String getSinaUID() {
        return mSinaInfo.uid;
    }

    public synchronized String getSinaName() {
        return mSinaInfo.name;
    }

    public synchronized int getSinaGender() {
        return mSinaInfo.gender;
    }

    public synchronized String getSinaAvatar() {
        return mSinaInfo.avatar;
    }

    // public synchronized String getSinaAvatarLarge() {
    // return mSinaInfo.avatarLarge;
    // }

    public synchronized void setTencentUID(String id) {
        mTencentInfo.uid = id;
    }

    public synchronized String getTencentUID() {
        return mTencentInfo.uid;
    }

    public synchronized String getTencentName() {
        return mTencentInfo.name;
    }

    public synchronized String getTencentAvatar() {
        return mTencentInfo.avatar;
    }

    // public synchronized String getTencentAvatarLarge() {
    // return mTencentInfo.avatarLarge;
    // }

    public synchronized int getCurrentUserType() {
        return mCurrentUserType;
    }

    public synchronized void setCurrentUserType(int userType) {
        mCurrentUserType = userType;
    }

    // public synchronized UserInfo getUserInfo() {
    // return mTogetherInfo;
    // }
    public synchronized String getUserID() {
        return mTogetherInfo.uid;
    }

    public synchronized String getUserName() {
        return mTogetherInfo.name;
    }

    public synchronized String getUserAvatar() {
        return mTogetherInfo.avatar;
    }

    public synchronized int getUserGender() {
        return mTogetherInfo.gender;
    }

    public synchronized long getLastLoginTime() {
        return mTogetherInfo.lastLoginTime;
    }

    public synchronized String getLastLoginTime(String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mTogetherInfo.lastLoginTime);
        return formater.format(calendar.getTime());
    }

    public synchronized void initSinaInfo(JSONObject json) throws JSONException {
        mSinaInfo.name = json.getString(SinaConstant.KEY_NAME);
        mSinaInfo.gender = mapSinaGender(json
                .getString(SinaConstant.KEY_GENDER));
        mSinaInfo.avatar = json.getString(SinaConstant.KEY_AVATAR_LARGE);
        // mSinaInfo.avatarLarge =
        // json.getString(SinaConstant.KEY_AVATAR_LARGE);
    }

    public synchronized void initTencentInfo(JSONObject json)
            throws JSONException {
        mTencentInfo.name = json.getString(TencentConstant.KEY_NAME);
        mTencentInfo.avatar = json.getString(TencentConstant.KEY_AVATAR_LARGE);
        // mTencentInfo.avatarLarge = json
        // .getString(TencentConstant.KEY_AVATAR_LARGE);
    }

    public synchronized void initTogetherInfo(JSONObject json)
            throws JSONException {
        mTogetherInfo.uid = json.getString(Constant.KEY_ID);
        mTogetherInfo.name = json.getString(Constant.KEY_NAME);
        mTogetherInfo.avatar = json.getString(Constant.KEY_AVATAR);
        mTogetherInfo.gender = json.getInt(Constant.KEY_SEX);
        mTogetherInfo.lastLoginTime = json
                .getLong(Constant.KEY_LAST_LOGIN_TIME);
    }

    private DataEngine() {
        mSinaInfo = new SinaInfo();
        mTencentInfo = new TencentInfo();
        mTogetherInfo = new TogetherInfo();
    }

    private int mapSinaGender(String sinaGender) {
        if (SinaConstant.GENDER_MALE.equalsIgnoreCase(sinaGender)) {
            return Constant.GENDER_MALE;
        } else if (SinaConstant.GENDER_FEMALE.equalsIgnoreCase(sinaGender)) {
            return Constant.GENDER_FEMALE;
        } else {
            return Constant.GENDER_UNKNOWN;
        }
    }
}
