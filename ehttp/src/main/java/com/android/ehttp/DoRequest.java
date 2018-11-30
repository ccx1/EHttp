package com.android.ehttp;

import com.android.ehttp.get.GetRequest;

import java.util.HashMap;
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
public abstract class DoRequest<T> implements Request<T> {
    protected           String              url;
    protected           Map<String, String> queryMap;
    protected           Map<String, String> header         = new HashMap<>();
    public static final String              GET            = "GET";
    public static final String              POST           = "POST";
    public static final String              HTTPS          = "https";
    public static final String              UA             = "User-Agent";
    public              int                 ReadTimeOut    = 10;
    public              int                 WriteTimeOut   = 10;
    public              int                 ConnectTimeOut = 10;


    @Override
    public void setReadTimeOut(int readTimeOut) {
        ReadTimeOut = readTimeOut;
    }

    @Override
    public void setWriteTimeOut(int writeTimeOut) {
        WriteTimeOut = writeTimeOut;
    }

    @Override
    public void setConnectTimeOut(int connectTimeOut) {
        ConnectTimeOut = connectTimeOut;
    }

}
