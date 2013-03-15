package com.together.app.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.together.app.util.SinaConstant;
import com.together.app.util.TencentConstant;

public class DataEngine {
    private static DataEngine sInstance = new DataEngine();

    private SinaInfo mSinaInfo;
    private TencentInfo mTencentInfo;

    public class SinaInfo {
        private String uid;
        private String name;
        private String gender;
        private String avatar;
        private String avatarLarge;
    }

    public class TencentInfo {
        private String uid;
        private String name;
        private String avatar;
        private String avatarLarge;
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

    public synchronized String getSinaGender() {
        return mSinaInfo.gender;
    }

    public synchronized String getSinaAvatar() {
        return mSinaInfo.avatar;
    }

    public synchronized String getSinaAvatarLarge() {
        return mSinaInfo.avatarLarge;
    }

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

    public synchronized String getTencentAvatarLarge() {
        return mTencentInfo.avatarLarge;
    }

    private DataEngine() {
        mSinaInfo = new SinaInfo();
        mTencentInfo = new TencentInfo();
    }

    public void initSinaInfo(JSONObject json) throws JSONException {
        mSinaInfo.name = json.getString(SinaConstant.KEY_NAME);
        mSinaInfo.gender = json.getString(SinaConstant.KEY_GENDER);
        mSinaInfo.avatar = json.getString(SinaConstant.KEY_AVATAR_SMALL);
        mSinaInfo.avatarLarge = json.getString(SinaConstant.KEY_AVATAR_LARGE);
    }

    public void initTencentInfo(JSONObject json) throws JSONException {
        mTencentInfo.name = json.getString(TencentConstant.KEY_NAME);
        mTencentInfo.avatar = json.getString(TencentConstant.KEY_AVATAR_SMALL);
        mTencentInfo.avatarLarge = json
                .getString(TencentConstant.KEY_AVATAR_LARGE);
    }
}
