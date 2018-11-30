package com.android.ehttp.get;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebSettings;

import com.android.ehttp.CallRequest;
import com.android.ehttp.DoRequest;
import com.android.ehttp.Queue;
import com.android.ehttp.RequestCallback;
import com.android.ehttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.android.ehttp.DoRequest.GET;

/**
 * ================================================
 *
 * @author ：ccx
 * 版    本：1.0
 * 创建日期：2018/11/30
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GetQueue implements Queue {
    private String              url;
    private Map<String, String> queryMap;
    private Map<String, String> header;
    private RequestCallback     requestCallback;
    private CallRequest         mCallRequest;

    public GetQueue(String url, Map<String, String> queryMap, Map<String, String> header, RequestCallback requestCallback) {
        this.url = url;
        this.queryMap = queryMap;
        this.header = header;
        this.requestCallback = requestCallback;
    }

    @Override
    public CallRequest build() {
        return mCallRequest;
    }

    @Override
    public void async() throws IOException {
        if (header == null) {
            header = new HashMap<>(16);
            String property = System.getProperty("http.agent");
            header.put(DoRequest.UA, TextUtils.isEmpty(property) ? "ehttp" : property);
            header.put("Content-Type", "text/html;charset=utf-8");
        }
        if (TextUtils.isEmpty(header.get(DoRequest.UA))) {
            String property = System.getProperty("http.agent");
            header.put(DoRequest.UA, TextUtils.isEmpty(property) ? "ehttp" : property);
        }
        mCallRequest = new CallRequest(getQueryUrl(url, queryMap), header, GET, requestCallback);
        mCallRequest.build();
    }

    /**
     * 拼接get数据
     *
     * @param url    地址
     * @param params get参数
     * @return 新的url
     */
    private static String getQueryUrl(String url, Map<String, String> params) {
        StringBuilder neoUrl = new StringBuilder(url);
        if (params != null && !params.isEmpty()) {
            neoUrl.append("?");
            for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
                neoUrl.append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue()).append("&");
            }
            neoUrl = new StringBuilder(neoUrl.substring(0, neoUrl.length() - 1));
        }
        return neoUrl.toString();
    }


    @Override
    public Response execute() throws IOException {
        mCallRequest = new CallRequest(getQueryUrl(url, queryMap), header, GET);
        mCallRequest.build();
        return mCallRequest.execute();
    }
}
