package com.android.ehttp;

import com.android.ehttp.call.CallRequest;

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
public interface Queue {

    String MY_UA                  = "ehttp";
    String UA_KEY                  = "http.agent";
    String CONTENT_TYPE        = "Content-Type";
    String CONTENT_LENGTH      = "Content-Length";
    String CONTENT_DISPOSITION = "Content-Disposition";

    String TYPE_TEXT      = "text/html;charset=utf-8";
    String TYPE_FORM_DATA = "multipart/form-data;charset=utf-8";
    String TYPE_FORM_DATA_NO_CHARSET = "multipart/form-data";
    String TYPE_FORM_DATA_NO_CHARSET_NO_MULTIPART = "form-data";
    String TYPE_JSON      = "application/json;charset=utf-8";
    String TYPE_STREAM    = "application/octet-stream";

    String GET   = "GET";
    String POST  = "POST";
    String HTTPS = "https";
    String UA    = "User-Agent";

    CallRequest build();

    void async() throws IOException;

    Response execute() throws IOException;
}
