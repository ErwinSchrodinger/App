package com.together.app.net;

import android.content.Context;
import android.os.Bundle;

import com.together.app.util.SharePreferencesKeeper;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class SinaManager {
    private static final String CONSUMER_KEY = "966056985";// 替换为开发者的appkey，例如"1646212860";
    private static final String REDIRECT_URL = "http://www.sina.com";

    private static SinaManager sInstance = new SinaManager();

    private Weibo mWeibo;
    private Oauth2AccessToken mToken;

    public static final synchronized SinaManager getInstance() {
        return sInstance;
    }

    public synchronized void authorize(Context context,
            final WeiboAuthListener listener) {
        mWeibo.authorize(context, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
                listener.onWeiboException(arg0);
            }

            @Override
            public void onError(WeiboDialogError arg0) {
                listener.onError(arg0);
            }

            @Override
            public void onComplete(Bundle arg0) {
                synchronized (SinaManager.this) {
                    String token = arg0.getString("access_token");
                    String expires_in = arg0.getString("expires_in");
                    mToken = new Oauth2AccessToken(token, expires_in);
                    SharePreferencesKeeper.keepSinaAccessToken(mToken);
                    SharePreferencesKeeper.clearTencent();
                    listener.onComplete(arg0);
                }
            }

            @Override
            public void onCancel() {
                listener.onCancel();
            }
        });
    }

    public synchronized boolean isSessionValid() {
        return mToken.isSessionValid();
    }

    public synchronized String getToken() {
        return mToken.getToken();
    }

    private SinaManager() {
        mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
        mToken = SharePreferencesKeeper.readSinaAccessToken();
        mWeibo.accessToken = mToken;
    }
}
