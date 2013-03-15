package com.together.app.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.together.app.util.AppLog;
import com.together.app.util.Errors;

public class NetModel extends AbstractModel {
    private static final String KEY_RESP_CODE = "respCode";
    private static final String KEY_RESP_MSG = "respMsg";
    private static final String KEY_ACTION = "action";
    private static NetModel sInstance = new NetModel();

    private Handler mHandler = new Handler(new Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            Bundle data = msg.getData();
            String respCode = data.getString(KEY_RESP_CODE);
            String respMsg = data.getString(KEY_RESP_MSG);
            int action = data.getInt(KEY_ACTION);
            boolean success = Errors.ERROR_SUCCESS.equalsIgnoreCase(respCode);
            if (!success) {
                put(KEY_DIALOG_ERROR_CODE, respCode);
                put(KEY_DIALOG_ERROR_MSG, respMsg);
            }

            postModelEvent(action, success);
            switch (action) {
            case MODEL_ACTION_SINA_UID:
                if (success) {
                    doAction(MODEL_ACTION_SINA_INFO);
                }
                break;
            }
            return false;
        }
    });

    public static final synchronized NetModel getInstance() {
        return sInstance;
    }

    public synchronized void doAction(int action) {
        switch (action) {
        case MODEL_ACTION_LOGIN:
            login();
            break;
        case MODEL_ACTION_SINA_UID:
            getSinaUID();
            break;
        case MODEL_ACTION_SINA_INFO:
            getSinaInfo();
            break;
        case MODEL_ACTION_TENCENT_INFO:
            getTencentInfo();
            break;
        case MODEL_ACTION_REGISTER:
            register();
            break;
        }
    }

    @Override
    public synchronized void onRequestComplete(int action, String respCode,
            String respMsg) {
        AppLog.d("onRequestComplete:" + action + "," + respCode + "," + respMsg);
        Bundle data = new Bundle();
        data.putString(KEY_RESP_CODE, respCode);
        data.putString(KEY_RESP_MSG, respMsg);
        data.putInt(KEY_ACTION, action);
        Message msg = Message.obtain();
        msg.setData(data);
        mHandler.sendMessage(msg);
    }

    private void login() {
        Bundle data = (Bundle) fetch(KEY_SUBMIT_INFO);
        AsyncHttpRunner.login(data, this);
    }

    private void getSinaUID() {
        AsyncHttpRunner.getSinaUID(this);
    }

    private void getSinaInfo() {
        AsyncHttpRunner.getSinaInfo(this);
    }

    private void getTencentInfo() {
        AsyncHttpRunner.getTencentInfo(this);
    }

    private void register() {
        Bundle data = (Bundle) fetch(KEY_SUBMIT_INFO);
        AsyncHttpRunner.register(data, this);
    }

    private NetModel() {

    }
}
