package com.android.ehttp;

import com.android.ehttp.get.DoGetRequest;
import com.android.ehttp.get.RequestGet;
import com.android.ehttp.post.DoPostRequest;
import com.android.ehttp.post.PostRequest;
import com.android.ehttp.post.RequestPost;

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
public class EHttp {

    public static DoGetRequest get() {
        return new RequestGet();
    }

    public static DoPostRequest post() {
        return new RequestPost();
    }
}
