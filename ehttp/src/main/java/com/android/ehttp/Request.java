package com.android.ehttp;

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
public interface Request<T> {
    T url(String url);

    T addHeader(String key, String value);

    void async(RequestCallback requestCallback);

    Response execute() throws IOException;

    void setReadTimeOut(int readTimeOut);

    void setWriteTimeOut(int writeTimeOut);

    void setConnectTimeOut(int connectTimeOut);
}
