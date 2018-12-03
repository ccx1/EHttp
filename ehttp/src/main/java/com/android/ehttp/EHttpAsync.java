package com.android.ehttp;

import android.os.AsyncTask;

import com.android.ehttp.post.DoPostRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
public class EHttpAsync extends AsyncTask<CallRequest, Void, Response> {


    private CallRequest currentCallRequest;

    @Override
    protected void onPreExecute() {
        // 异步前的操作
        // 主要是做一些状态记录
    }


    @Override
    protected Response doInBackground(CallRequest... callRequests) {
        CallRequest callRequest = callRequests[0];
        this.currentCallRequest = callRequest;
        try {

            if (currentCallRequest.getMethod().equalsIgnoreCase(POST)) {
                switch (DoPostRequest.TYPE) {
                    case 0:
                        Map<String, String> queryMap = currentCallRequest.getQueryMap();
                        if (queryMap == null) {
                            break;
                        }
                        JSONObject jsonObject = new JSONObject(queryMap);
                        String json = jsonObject.toString();
                        OutputStream outputStream = currentCallRequest.getConnection().getOutputStream();
                        outputStream.write(json.getBytes());
                        outputStream.flush();
                        outputStream.close();
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
            }
            currentCallRequest.getConnection().connect();
            InputStream inputStream = currentCallRequest.getConnection().getInputStream();
            currentCallRequest.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            DoPostRequest.TYPE = DoPostRequest.DEFAULT;
            return new Response(e, callRequest);
        }
        DoPostRequest.TYPE = DoPostRequest.DEFAULT;
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
        }
    }

}
