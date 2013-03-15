package com.together.app.net;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.together.app.data.DataEngine;
import com.together.app.data.TencentAccessToken;
import com.together.app.util.AccessTokenKeeper;

public class TencentManager {
    public static final String APP_ID = "222222";// 替换为开发者的appkey，例如"1646212860";
    private static final String SCOPE = "get_simple_userinfo";

    private static TencentManager sInstance = new TencentManager();

    private Tencent mTencent;
    private TencentAccessToken mToken;
    private Context mContext;

    public static final synchronized TencentManager getInstance() {
        return sInstance;
    }

    public synchronized void login(final Activity context,
            final IUiListener listener) {
        mTencent.login(context, SCOPE, new IUiListener() {

            @Override
            public void onCancel() {
                listener.onCancel();
            }

            @Override
            public void onComplete(JSONObject arg0) {
                synchronized (TencentManager.this) {
                    try {
                        String token = arg0.getString("access_token");
                        String expires_in = arg0.getString("expires_in");
                        String openid = arg0.getString("openid");
                        mToken = new TencentAccessToken(token, expires_in,
                                openid);
                        AccessTokenKeeper.keepTencentAccessToken(mToken);
                        AccessTokenKeeper.clearSina();
                        DataEngine.getInstance().setTencentUID(openid);
                        listener.onComplete(arg0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError(new UiError(0, "", ""));
                    }
                }
            }

            @Override
            public void onError(UiError arg0) {
                listener.onError(arg0);
            }
        });
    }

    public synchronized boolean isSessionValid() {
        return mTencent.isSessionValid();
    }

    public synchronized String getToken() {
        return mToken.getToken();
    }

    public synchronized String getOpenID() {
        return mToken.getOpenID();
    }

    public synchronized void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        mTencent.onActivityResult(requestCode, resultCode, data);
    }

    public void init(Context context) {
        mContext = context;
        mTencent = Tencent.createInstance(APP_ID, mContext);
        mToken = AccessTokenKeeper.readTencentAccessToken();
        mTencent.setAccessToken(mToken.getToken(), mToken.getExpiresTime());
        mTencent.setOpenId(mToken.getOpenID());
    }

    private TencentManager() {
    }
}
