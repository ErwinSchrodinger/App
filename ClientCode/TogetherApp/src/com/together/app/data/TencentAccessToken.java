package com.together.app.data;

public class TencentAccessToken {
    private String mToken;
    private String mExpiresTime;
    private String mOpenID;

    public TencentAccessToken() {
    }

    public TencentAccessToken(String token, String expiresTime, String openid) {
        mToken = token;
        mExpiresTime = expiresTime;
        mOpenID = openid;
    }

    public String getToken() {
        return mToken;
    }

    public String getExpiresTime() {
        return mExpiresTime;
    }

    public String getOpenID() {
        return mOpenID;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public void setExpiresTime(String expiresTime) {
        mExpiresTime = expiresTime;
    }

    public void setOpenID(String openid) {
        mOpenID = openid;
    }
}
