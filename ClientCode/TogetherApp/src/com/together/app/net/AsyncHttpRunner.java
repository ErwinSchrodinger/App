package com.together.app.net;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.together.app.data.DataEngine;
import com.together.app.data.EventSearchCondition;
import com.together.app.util.AppConfig;
import com.together.app.util.Constant;
import com.together.app.util.Errors;
import com.together.app.util.SinaConstant;
import com.together.app.util.TencentConstant;

public class AsyncHttpRunner {
    public static final void login(final Bundle data,
            final IHttpRequestListener listener) {
        new Thread() {

            @Override
            public void run() {
                try {
                    String message = null;
                    int type = data.getInt(Constant.KEY_USER_TYPE);
                    if (Constant.TYPE_USER_NORMAL == type) {
                        message = MessageFactory.getLoginMsg(type,
                                data.getString(Constant.KEY_ACCOUNT),
                                data.getString(Constant.KEY_PASSWORD));
                    } else {
                        message = MessageFactory.getLoginMsg(type,
                                data.getString(Constant.KEY_OPENID),
                                data.getString(Constant.KEY_NAME),
                                data.getString(Constant.KEY_AVATAR));

                    }

                    String response = HttpManager.sendMessage(
                            AppConfig.SERVER_URL + Constant.ACTION_LOGIN,
                            message, HttpManager.POST);

                    if (null != response) {
                        JSONObject body = new JSONObject(response)
                                .getJSONObject(Constant.KEY_BODY);
                        JSONObject status = body
                                .getJSONObject(Constant.KEY_STATUS);
                        String respCode = status
                                .getString(Constant.KEY_STATUS_CODE);
                        if (Errors.ERROR_SUCCESS.equals(respCode)) {
                            DataEngine.getInstance().initTogetherInfo(body);
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_LOGIN, respCode,
                                    null);
                        } else {
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_LOGIN, respCode,
                                    status.getString(Constant.KEY_STATUS_DESC));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_LOGIN,
                            Errors.ERROR_NETWORK, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_LOGIN,
                            Errors.ERROR_RESPONSE_FORMAT, null);
                }
            }
        }.start();
    }

    public static final void register(final Bundle data,
            final IHttpRequestListener listener) {
        new Thread() {

            @Override
            public void run() {
                try {
                    String message = MessageFactory.getRegisterMsg(
                            data.getString(Constant.KEY_ACCOUNT),
                            data.getString(Constant.KEY_PASSWORD),
                            data.getString(Constant.KEY_NAME),
                            data.getInt(Constant.KEY_SEX));

                    String response = HttpManager.sendMessage(
                            AppConfig.SERVER_URL + Constant.ACTION_REGISTER,
                            message, HttpManager.POST);

                    if (null != response) {
                        JSONObject body = new JSONObject(response)
                                .getJSONObject(Constant.KEY_BODY);
                        JSONObject status = body
                                .getJSONObject(Constant.KEY_STATUS);
                        String respCode = status
                                .getString(Constant.KEY_STATUS_CODE);
                        if (Errors.ERROR_SUCCESS.equals(respCode)) {
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_REGISTER,
                                    respCode, null);
                        } else {
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_REGISTER,
                                    respCode,
                                    status.getString(Constant.KEY_STATUS_DESC));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_REGISTER,
                            Errors.ERROR_NETWORK, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_REGISTER,
                            Errors.ERROR_RESPONSE_FORMAT, null);
                }
            }
        }.start();
    }

    public static final void getEventList(final Bundle data,
            final IHttpRequestListener listener) {
        new Thread() {

            @Override
            public void run() {
                try {
                    EventSearchCondition condition = (EventSearchCondition) data
                            .get(Constant.KEY_SEARCH_CONDITION);
                    String message = MessageFactory.getEventListMsg(
                            condition.getEventType(), condition.getAreaID(),
                            condition.getCostType(), condition.getStartDate(),
                            condition.getEndDate());

                    String response = HttpManager.sendMessage(
                            AppConfig.SERVER_URL + Constant.ACTION_EVENT_LIST,
                            message, HttpManager.POST);

                    if (null != response) {
                        JSONObject body = new JSONObject(response)
                                .getJSONObject(Constant.KEY_BODY);
                        JSONObject status = body
                                .getJSONObject(Constant.KEY_STATUS);
                        String respCode = status
                                .getString(Constant.KEY_STATUS_CODE);
                        if (Errors.ERROR_SUCCESS.equals(respCode)) {
                            MessageFactory.parseEventList(body);
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_GET_EVENT_LIST,
                                    respCode, null);
                        } else {
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_GET_EVENT_LIST,
                                    respCode,
                                    status.getString(Constant.KEY_STATUS_DESC));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_GET_EVENT_LIST,
                            Errors.ERROR_NETWORK, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_GET_EVENT_LIST,
                            Errors.ERROR_RESPONSE_FORMAT, null);
                }
            }
        }.start();
    }

    public static final void getEventDetail(final Bundle data,
            final IHttpRequestListener listener) {
        new Thread() {

            @Override
            public void run() {
                try {
                    String message = MessageFactory.getEventDetailMsg(data
                            .getInt(Constant.KEY_EVENT_ID));

                    String response = HttpManager
                            .sendMessage(AppConfig.SERVER_URL
                                    + Constant.ACTION_EVENT_DETAIL, message,
                                    HttpManager.POST);

                    if (null != response) {
                        JSONObject body = new JSONObject(response)
                                .getJSONObject(Constant.KEY_BODY);
                        JSONObject status = body
                                .getJSONObject(Constant.KEY_STATUS);
                        String respCode = status
                                .getString(Constant.KEY_STATUS_CODE);
                        if (Errors.ERROR_SUCCESS.equals(respCode)) {
                            MessageFactory.parseEventDetail(body);
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_GET_EVENT_DETAIL,
                                    respCode, null);
                        } else {
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_GET_EVENT_DETAIL,
                                    respCode,
                                    status.getString(Constant.KEY_STATUS_DESC));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_GET_EVENT_DETAIL,
                            Errors.ERROR_NETWORK, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_GET_EVENT_DETAIL,
                            Errors.ERROR_RESPONSE_FORMAT, null);
                }
            }
        }.start();
    }

    public static final void getSinaUID(final IHttpRequestListener listener) {
        new Thread() {

            @Override
            public void run() {
                try {
                    String token = SinaManager.getInstance().getToken();

                    String response = HttpManager.sendMessage(
                            SinaConstant.URL_GET_UID
                                    + "?"
                                    + URLEncoder.encode(SinaConstant.KEY_TOKEN,
                                            "UTF-8") + "="
                                    + URLEncoder.encode(token, "UTF-8"), "",
                            HttpManager.GET);

                    if (null != response) {
                        JSONObject json = new JSONObject(response);
                        if (!json.has(SinaConstant.KEY_ERROR_CODE)) {
                            String id = json.getString(SinaConstant.KEY_UID);
                            DataEngine.getInstance().setSinaUID(id);
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_SINA_UID,
                                    Errors.ERROR_SUCCESS, null);
                        } else {
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_SINA_UID,
                                    Errors.ERROR_SINA_PREFIX
                                            + json.getString(SinaConstant.KEY_ERROR_CODE),
                                    json.getString(SinaConstant.KEY_ERROR_MSG));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_SINA_UID,
                            Errors.ERROR_NETWORK, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_SINA_UID,
                            Errors.ERROR_RESPONSE_FORMAT, null);
                }
            }
        }.start();
    }

    public static final void getSinaInfo(final IHttpRequestListener listener) {
        new Thread() {

            @Override
            public void run() {
                try {
                    String token = SinaManager.getInstance().getToken();
                    String uid = DataEngine.getInstance().getSinaUID();

                    String response = HttpManager.sendMessage(
                            SinaConstant.URL_GET_USER_INFO
                                    + "?"
                                    + URLEncoder.encode(SinaConstant.KEY_TOKEN,
                                            "UTF-8")
                                    + "="
                                    + URLEncoder.encode(token, "UTF-8")
                                    + "&"
                                    + URLEncoder.encode(SinaConstant.KEY_UID,
                                            "UTF-8") + "="
                                    + URLEncoder.encode(uid, "UTF-8"), "",
                            HttpManager.GET);

                    if (null != response) {
                        JSONObject json = new JSONObject(response);
                        if (!json.has(SinaConstant.KEY_ERROR_CODE)) {
                            DataEngine.getInstance().initSinaInfo(json);
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_SINA_INFO,
                                    Errors.ERROR_SUCCESS, null);
                        } else {
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_SINA_INFO,
                                    Errors.ERROR_SINA_PREFIX
                                            + json.getString(SinaConstant.KEY_ERROR_CODE),
                                    json.getString(SinaConstant.KEY_ERROR_MSG));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_SINA_INFO,
                            Errors.ERROR_NETWORK, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_SINA_INFO,
                            Errors.ERROR_RESPONSE_FORMAT, null);
                }
            }
        }.start();
    }

    public static final void getTencentInfo(final IHttpRequestListener listener) {
        new Thread() {

            @Override
            public void run() {
                try {
                    String token = TencentManager.getInstance().getToken();
                    String uid = DataEngine.getInstance().getTencentUID();

                    String response = HttpManager.sendMessage(
                            TencentConstant.URL_GET_USER_INFO
                                    + "?"
                                    + URLEncoder.encode(
                                            TencentConstant.KEY_TOKEN, "UTF-8")
                                    + "="
                                    + URLEncoder.encode(token, "UTF-8")
                                    + "&"
                                    + URLEncoder.encode(
                                            TencentConstant.KEY_APPID, "UTF-8")
                                    + "="
                                    + URLEncoder.encode(TencentManager.APP_ID,
                                            "UTF-8")
                                    + "&"
                                    + URLEncoder.encode(
                                            TencentConstant.KEY_UID, "UTF-8")
                                    + "=" + URLEncoder.encode(uid, "UTF-8"),
                            "", HttpManager.GET);

                    if (null != response) {
                        JSONObject json = new JSONObject(response);
                        if (!json.has(SinaConstant.KEY_ERROR_CODE)) {
                            DataEngine.getInstance().initTencentInfo(json);
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_TENCENT_INFO,
                                    Errors.ERROR_SUCCESS, null);
                        } else {
                            listener.onRequestComplete(
                                    AbstractModel.MODEL_ACTION_TENCENT_INFO,
                                    Errors.ERROR_TENCENT_PREFIX
                                            + json.getString(TencentConstant.KEY_RESP_CODE),
                                    json.getString(TencentConstant.KEY_RESP_MSG));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_TENCENT_INFO,
                            Errors.ERROR_NETWORK, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onRequestComplete(
                            AbstractModel.MODEL_ACTION_TENCENT_INFO,
                            Errors.ERROR_RESPONSE_FORMAT, null);
                }
            }
        }.start();
    }
}
