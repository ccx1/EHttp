package com.android.ehttp;

import com.android.ehttp.call.CallRequest;

import java.io.IOException;
import java.io.InputStream;

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
public class Response {

    private IOException e;
    private CallRequest mCurrentRequest;

    public Response(CallRequest callRequest) {
        mCurrentRequest = callRequest;

    }

    public Response(IOException e, CallRequest callRequest) {
        this.e = e;
        mCurrentRequest = callRequest;

    }

    public ResponseBody body() {
        return new ResponseBody(mCurrentRequest);
    }

    public int getResponseCode() {
        try {
            return mCurrentRequest.getConnection().getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 400;
    }

    public IOException getException() {
        if (mCurrentRequest.getResultByte() != null && mCurrentRequest.getResultByte().length != 0) {
            return new IOException(new String(mCurrentRequest.getResultByte()));
        }
        return e;
    }

    public int getContentLength() {
        try {
            return mCurrentRequest.getConnection().getContentLength();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getContentEncoding() {
        try {
            return mCurrentRequest.getConnection().getContentEncoding();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "utf-8";
    }

    public InputStream getInputStream() throws IOException {
        return mCurrentRequest.getConnection().getInputStream();
    }

}
