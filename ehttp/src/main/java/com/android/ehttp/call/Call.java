package com.android.ehttp.call;

import com.android.ehttp.RequestCallback;
import com.android.ehttp.Response;

import java.io.IOException;

/**
 * ================================================
 *
 * @author ：ccx
 * 版    本：1.0
 * 创建日期：2018/12/3
 * 描    述：
 * 修订历史：
 * ================================================
 */
public interface Call {

    void async(RequestCallback requestCallback);

    Response execute() throws IOException;
}
