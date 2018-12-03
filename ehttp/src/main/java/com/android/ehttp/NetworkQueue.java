package com.android.ehttp;

import android.text.TextUtils;

import com.android.ehttp.body.MediaType;

import java.io.IOException;

/**
 * ================================================
 *
 * @author ：ccx
 * 版    本：1.0
 * 创建日期：2018/12/3
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class NetworkQueue implements Queue {
    private ERequest        eRequest;
    private RequestCallback requestCallback;
    private CallRequest     mCallRequest;

    public NetworkQueue(ERequest eRequest, RequestCallback requestCallback) {
        this.eRequest = eRequest;
        this.requestCallback = requestCallback;
    }

    public NetworkQueue(ERequest eRequest) {
        this(eRequest, null);
    }

    @Override
    public CallRequest build() {
        return mCallRequest;
    }

    @Override
    public void async() throws IOException {
        String property = System.getProperty("http.agent");
        if (TextUtils.isEmpty(eRequest.header.get(UA))) {
            eRequest.header.put(UA, property == null ? MY_UA : property);
        }
        if (eRequest.method.equals(POST)) {
            MediaType mediaType = eRequest.body.contentType();
            // 设置请求类型
            eRequest.header.put(CONTENT_TYPE, mediaType.toString());
            eRequest.header.put(CONTENT_LENGTH, eRequest.body.contentLength() + "");
        }
        mCallRequest = new CallRequest(eRequest, requestCallback);
        mCallRequest.build();
    }

    @Override
    public Response execute() throws IOException {
        mCallRequest = new CallRequest(eRequest);
        mCallRequest.build();
        return mCallRequest.execute();
    }
}
