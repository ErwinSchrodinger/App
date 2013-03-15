package com.together.app.net;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.together.app.util.AppLog;

public class HttpManager {
    public static final String POST = "POST";
    public static final String GET = "GET";

    private static final int HTTP_TIME_OUT = 60000;

    public static final String sendMessage(String server, String message,
            String method) throws IOException {
        AppLog.i(server + "," + method + "," + message);
        HttpResponse response = null;

        DefaultHttpClient client = new DefaultHttpClient();
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, HTTP_TIME_OUT);
        HttpConnectionParams.setSoTimeout(params, HTTP_TIME_OUT);
        StringEntity entity = new StringEntity(message, "UTF-8");

        if (POST.equals(method)) {
            HttpPost post = new HttpPost(server);
            post.setEntity(entity);
            response = client.execute(post);
        } else {
            HttpGet get = new HttpGet(server);
            response = client.execute(get);
        }

        if (null != response && 200 == response.getStatusLine().getStatusCode()) {
            String resp = EntityUtils.toString(response.getEntity(), "UTF-8");
            AppLog.i("response:" + resp);
            return resp;
        } else {
            throw new IOException();
        }
    }
}
