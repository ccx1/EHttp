package com.android.ehttp;

import android.os.AsyncTask;

import com.android.ehttp.call.CallRequest;
import com.android.ehttp.call.CallRequestPost;

import java.io.IOException;
import java.io.InputStream;

import static com.android.ehttp.Queue.POST;

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
public class EHttpAsync extends AsyncTask<CallRequest, Void, Response> {


    private CallRequest     currentCallRequest;
    private OnStatusChanged mOnStatusChanged;

    @Override
    protected void onPreExecute() {
        // 异步前的操作
        // 主要是做一些状态记录
        mOnStatusChanged.onCreate(this);
    }


    @Override
    protected Response doInBackground(CallRequest... callRequests) {
        CallRequest callRequest = callRequests[0];
        this.currentCallRequest = callRequest;
        try {
            if (currentCallRequest.getMethod().equalsIgnoreCase(POST)) {
                new CallRequestPost.CallRequestPostHolder(currentCallRequest).build();
            }
            currentCallRequest.getConnection().connect();
            InputStream inputStream = currentCallRequest.getConnection().getInputStream();
            currentCallRequest.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            InputStream errorStream = currentCallRequest.getConnection().getErrorStream();
            try {
                currentCallRequest.setInputStream(errorStream);
                return new Response(e, callRequest);
            } catch (IOException e1) {
                e1.printStackTrace();
                return new Response(e1, callRequest);
            }

        }
        return new Response(callRequest);
    }

    @Override
    protected void onPostExecute(Response response) {
        // 结果返回
        if (response.getResponseCode() >= 200 && response.getResponseCode() < 400) {
            try {
                currentCallRequest.getRequestCallback().onResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            currentCallRequest.getRequestCallback().onFailure(response.getException());
        }
        if (this.isCancelled()) {
            this.onCancelled();
            mOnStatusChanged.onCancelled(this);
        }
    }


    public void setOnCancelled(OnStatusChanged onStatusChanged) {
        mOnStatusChanged = onStatusChanged;
    }

    public interface OnStatusChanged {
        void onCreate(EHttpAsync eHttpAsync);

        void onCancelled(EHttpAsync eHttpAsync);
    }

}
