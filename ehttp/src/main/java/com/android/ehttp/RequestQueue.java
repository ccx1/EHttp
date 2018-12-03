package com.android.ehttp;

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
public class RequestQueue {

    private Queue mQueen;


    public RequestQueue(ERequest eRequest, RequestCallback requestCallback) throws IOException {
        mQueen = new NetworkQueue(eRequest, requestCallback);
        mQueen.async();
    }

    public RequestQueue(ERequest eRequest) throws IOException {
        mQueen = new NetworkQueue(eRequest);
        mQueen.async();
    }


    public CallRequest getCallRequest() {
        return mQueen.build();
    }

    public Response execute() throws IOException {
        return mQueen.execute();
    }
}
