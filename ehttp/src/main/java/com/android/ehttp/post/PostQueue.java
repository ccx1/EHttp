package com.android.ehttp.post;

import android.text.TextUtils;
import android.view.View;

import com.android.ehttp.CallRequest;
import com.android.ehttp.DoRequest;
import com.android.ehttp.Queue;
import com.android.ehttp.RequestCallback;
import com.android.ehttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.android.ehttp.DoRequest.POST;

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
public class PostQueue implements Queue {
    private String              url;
    private Map<String, String> queryMap;
    private Map<String, String> header;
    private RequestCallback     requestCallback;
    private CallRequest         mCallRequest;

    public PostQueue(String url, Map<String, String> queryMap, Map<String, String> header, RequestCallback requestCallback) {
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
            switch (DoPostRequest.TYPE) {
                case 0:
                    header.put("Content-Type", "application/json;charset=utf-8");
                    break;
                case 1:
                    header.put("Content-Type", "multipart/form-data");
                    break;
                default:
                    header.put("Content-Type", "text/html;charset=utf-8");
                    break;
            }

        }
        if (TextUtils.isEmpty(header.get(DoRequest.UA))) {
            String property = System.getProperty("http.agent");
            header.put(DoRequest.UA, TextUtils.isEmpty(property) ? "ehttp" : property);
        }
        mCallRequest = new CallRequest(url, queryMap, header, POST, requestCallback);
        mCallRequest.build();
    }

    @Override
    public Response execute() throws IOException {
        mCallRequest = new CallRequest(url, queryMap, header, POST, requestCallback);
        mCallRequest.build();
        return mCallRequest.execute();
    }
}
