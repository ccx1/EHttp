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

    CallRequest build();

    void async() throws IOException;

    Response execute() throws IOException;
}
