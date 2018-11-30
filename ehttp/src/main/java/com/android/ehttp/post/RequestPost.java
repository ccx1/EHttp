package com.android.ehttp.post;

import com.android.ehttp.CallPool;
import com.android.ehttp.CallRequest;
import com.android.ehttp.DoRequest;
import com.android.ehttp.EHttpAsync;
import com.android.ehttp.Request;
import com.android.ehttp.RequestCallback;
import com.android.ehttp.RequestQueue;
import com.android.ehttp.Response;

import java.io.IOException;
import java.util.HashMap;

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
public class RequestPost extends DoPostRequest {

    @Override
    public void async(RequestCallback requestCallback) {
        // 开始异步请求
        EHttpAsync oneAsync = CallPool.getInstance().getOneAsync();
        try {
            oneAsync.execute(doPost(requestCallback));
        } catch (IOException e) {
            e.printStackTrace();
            requestCallback.onFailure(e);
        }
    }

    private CallRequest doPost(RequestCallback requestCallback) throws IOException {
        RequestQueue requestQueen = new RequestQueue(url, POST, queryMap, header, requestCallback);
        return requestQueen.getCallRequest();
    }

    @Override
    public Response execute() throws IOException {
        RequestQueue requestQueen = new RequestQueue(url, POST, queryMap, header);
        return requestQueen.execute();
    }
}
