package com.android.ehttp.post;

import android.text.TextUtils;
import android.view.View;

import com.android.ehttp.CallRequest;
import com.android.ehttp.DoRequest;
import com.android.ehttp.Queue;
import com.android.ehttp.RequestCallback;
import com.android.ehttp.Response;

import java.io.File;
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
    private PostModel           mPostModel;
    private String              url;
    private Map<String, String> queryMap;
    private Map<String, String> header;
    private RequestCallback     requestCallback;
    private CallRequest         mCallRequest;

    public PostQueue(String url, Map<String, String> queryMap, Map<String, String> header, PostModel postModel, RequestCallback requestCallback) {
        this.url = url;
        this.queryMap = queryMap;
        this.header = header;
        this.requestCallback = requestCallback;
        this.mPostModel = postModel;
    }

    @Override
    public CallRequest build() {
        return mCallRequest;
    }

    @Override
    public void async() throws IOException {
        String property = System.getProperty("http.agent");
        if (header == null) {
            header = new HashMap<>(16);
            header.put(DoRequest.UA, property == null ? UA : property);
            switch (DoPostRequest.TYPE) {
                case DoPostRequest.JSON:
                    header.put(CONTENT_TYPE, TYPE_JSON);
                    break;
                case DoPostRequest.FORM:
                case DoPostRequest.FILE:
                    header.put(CONTENT_TYPE, TYPE_FORM_DATA);
                    break;
                default:
                    header.put(CONTENT_TYPE, TYPE_TEXT);
                    break;
            }

        }
        if (TextUtils.isEmpty(header.get(DoRequest.UA))) {
            header.put(DoRequest.UA, property == null ? UA : property);
        }
        mCallRequest = new CallRequest(url, queryMap, header, POST, mPostModel, requestCallback);
        mCallRequest.build();
    }

    @Override
    public Response execute() throws IOException {
        mCallRequest = new CallRequest(url, queryMap, header, POST, mPostModel);
        mCallRequest.build();
        return mCallRequest.execute();
    }
}
