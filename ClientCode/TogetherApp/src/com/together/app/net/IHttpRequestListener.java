package com.together.app.net;

public interface IHttpRequestListener {
    public void onRequestComplete(int action, String respCode, String respMsg);
}
