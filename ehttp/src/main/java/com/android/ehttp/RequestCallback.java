package com.android.ehttp;

import android.telecom.Call;

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
public interface RequestCallback {
    void onResponse(Response response) throws IOException;

    void onFailure(IOException e);
}
