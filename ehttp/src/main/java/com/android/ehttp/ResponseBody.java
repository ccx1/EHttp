package com.android.ehttp;

import com.android.ehttp.call.CallRequest;

import java.io.ByteArrayOutputStream;
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
public class ResponseBody {

    private CallRequest request;

    public ResponseBody(CallRequest request) {
        this.request = request;

    }

    public String string() throws IOException {
        return new String(request.getResultByte().toByteArray());
    }


}
