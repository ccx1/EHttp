package com.android.ehttp;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownServiceException;

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
        InputStream inputStream = request.getResultStream();
        if (inputStream == null) {
            throw new UnknownServiceException("service connect fail");
        }
        ByteArrayOutputStream bos = getResult(inputStream);
        return new String(bos.toByteArray());
    }

    @NonNull
    private ByteArrayOutputStream getResult(InputStream inputStream) throws IOException {
        byte[]                buffer = new byte[1024];
        int                   len;
        ByteArrayOutputStream bos    = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        inputStream.close();
        return bos;
    }
}
