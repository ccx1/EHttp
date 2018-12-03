package com.android.ehttp;

import android.os.AsyncTask;

import com.android.ehttp.body.PostBody;
import com.android.ehttp.body.PostFormBody;
import com.android.ehttp.body.PostModel;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

import static com.android.ehttp.Queue.CONTENT_DISPOSITION;
import static com.android.ehttp.Queue.CONTENT_TYPE;
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
                appendParams();

            }
//            currentCallRequest.getConnection().connect();
            InputStream inputStream = currentCallRequest.getConnection().getInputStream();
            currentCallRequest.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(e, callRequest);
        }
        return new Response(callRequest);
    }

    private void appendParams() throws IOException {
        PostBody body = currentCallRequest.getERequest().body;
        if (body instanceof PostFormBody) {
            postForm((PostFormBody) body);
        } else {
            post(body);
        }
    }

    private void postForm(PostFormBody body) throws IOException {
        // 如果是表单，则用表单提交
        // 否则为直接提交
        if (body.mFile != null) {
            postFile(body);
        } else {
            OutputStream outputStream;
            String       queryUrl = CallHelper.getInstance().getQueryUrl("", body.params);
            outputStream = currentCallRequest.getConnection().getOutputStream();
            outputStream.write(queryUrl.getBytes());
            outputStream.flush();
            outputStream.close();
        }


    }

    private void postFile(PostFormBody body) throws IOException {
        // 说明有文件
        // 边界标识 随机生成
        String              boundary     = UUID.randomUUID().toString();
        String              prefix       = "--";
        String              lineEnd      = "\r\n";
        OutputStream        outputStream = currentCallRequest.getConnection().getOutputStream();
        DataOutputStream    out          = new DataOutputStream(outputStream);
        Map<String, String> queryMap     = body.params;
        if (queryMap != null && !queryMap.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : queryMap.entrySet()) {
                String key   = entry.getKey();
                String value = entry.getValue();
                sb.append(prefix).append(boundary).append(lineEnd);
                sb.append(CONTENT_DISPOSITION + ": form-data; name=\"")
                        .append(key).append("\"").append(lineEnd)
                        .append(lineEnd);
                sb.append(value).append(lineEnd);
            }
            out.write(sb.toString().getBytes());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(boundary);
        sb.append(lineEnd);
        sb.append(CONTENT_DISPOSITION + ": form-data; name=\"").append(body.keyFile).append("\"; filename=\"").append(body.mFile.getName()).append("\"").append(lineEnd);
        sb.append(CONTENT_TYPE + Queue.TYPE_STREAM).append(lineEnd);
        sb.append(lineEnd);
        out.write(sb.toString().getBytes());
        InputStream input = new FileInputStream(body.mFile);
        byte[]      bytes = new byte[1024];
        int         len   = 0;
        while ((len = input.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        out.write(lineEnd.getBytes());
        byte[] endData = (prefix + boundary + prefix + lineEnd).getBytes();
        out.write(endData);
        input.close();
        out.flush();
        out.flush();
        out.close();
    }

    private void post(PostBody body) throws IOException {
        PostModel postModel = body.create();
        if (postModel.getLength() == 0) {
            throw new IOException("Content-Length == 0");
        }
        OutputStream outputStream = currentCallRequest.getConnection().getOutputStream();
        outputStream.write(postModel.getBuff());
        outputStream.flush();
        outputStream.close();
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
