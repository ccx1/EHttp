package com.android.ehttp;

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
public class Call {

    private ERequest eRequest;

    public Call(ERequest eRequest) {
        this.eRequest = eRequest;
    }

    public void async(RequestCallback requestCallback) {
        // 异步池
        EHttpAsync oneAsync = CallPool.getInstance().getOneAsync();
        try {
            oneAsync.execute(doRequestQueue(requestCallback));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CallRequest doRequestQueue(RequestCallback requestCallback) throws IOException {
        RequestQueue requestQueen = new RequestQueue(eRequest, requestCallback);
        return requestQueen.getCallRequest();
    }

    public Response execute() throws IOException {
        RequestQueue requestQueen = new RequestQueue(eRequest);
        return requestQueen.execute();
    }
}
