package com.android.ehttp.get;

import android.support.annotation.NonNull;

import com.android.ehttp.CallPool;
import com.android.ehttp.CallRequest;
import com.android.ehttp.EHttpAsync;
import com.android.ehttp.RequestCallback;
import com.android.ehttp.RequestQueue;
import com.android.ehttp.Response;

import java.io.IOException;

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
public class RequestGet extends DoGetRequest {


    @Override
    public void async(@NonNull RequestCallback requestCallback) {
        // 开始异步请求
        EHttpAsync oneAsync = CallPool.getInstance().getOneAsync();
        try {
            oneAsync.execute(doGet(requestCallback));
        } catch (IOException e) {
            e.printStackTrace();
            requestCallback.onFailure(e);
        }
    }

    private CallRequest doGet(RequestCallback requestCallback) throws IOException {
        RequestQueue requestQueen = new RequestQueue(url, GET, queryMap, header, null, requestCallback);
        return requestQueen.getCallRequest();
    }


    @Override
    public synchronized Response execute() throws IOException {
        RequestQueue requestQueue = new RequestQueue(url, GET, queryMap, header);
        return requestQueue.execute();
    }
}
