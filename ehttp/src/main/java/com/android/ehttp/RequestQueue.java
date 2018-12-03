package com.android.ehttp;

import com.android.ehttp.get.GetQueue;
import com.android.ehttp.post.PostModel;
import com.android.ehttp.post.PostQueue;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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
public class RequestQueue {

    private PostModel           mPostModel;
    private String              url;
    private Map<String, String> queryMap;
    private Map<String, String> header;
    private RequestCallback     requestCallback;
    private Queue               mQueen;

    public RequestQueue(String url, String method, Map<String, String> queryMap, Map<String, String> header) throws IOException {
        this(url, method, queryMap, header, null);
    }

    public RequestQueue(String url, String method, Map<String, String> queryMap, Map<String, String> header, PostModel postModel) throws IOException {
        this(url, method, queryMap, header, postModel, null);
    }

    public RequestQueue(String url, String method, Map<String, String> queryMap, Map<String, String> header, PostModel postModel, RequestCallback requestCallback) throws IOException {
        this.url = url;
        this.queryMap = queryMap;
        this.header = header;
        this.requestCallback = requestCallback;
        if (method.equalsIgnoreCase(DoRequest.GET)) {
            doGet();
        } else {
            this.mPostModel = postModel;
            doPost();
        }
    }


    private void doPost() throws IOException {
        mQueen = new PostQueue(url, queryMap, header, mPostModel, requestCallback);
        mQueen.async();
    }

    private void doGet() throws IOException {
        // 拼接数据处理
        mQueen = new GetQueue(url, queryMap, header, requestCallback);
        // 异步处理数据
        mQueen.async();
    }

    public CallRequest getCallRequest() {
        return mQueen.build();
    }

    public Response execute() throws IOException {
        return mQueen.execute();
    }
}
