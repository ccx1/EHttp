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
public interface Queue {

    String UA                  = "ehttp";
    String CONTENT_TYPE        = "Content-Type";
    String CONTENT_DISPOSITION = "Content-Disposition";

    String TYPE_TEXT      = "text/html;charset=utf-8";
    String TYPE_FORM_DATA = "multipart/form-data";
    String TYPE_JSON      = "application/json;charset=utf-8";
    String TYPE_STREAM    = "application/octet-stream;charset=utf-8";

    CallRequest build();

    void async() throws IOException;

    Response execute() throws IOException;
}
