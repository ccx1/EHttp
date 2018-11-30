package com.android.ehttp.post;

import com.android.ehttp.DoRequest;

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
public abstract class DoPostRequest extends DoRequest<PostRequest> implements PostRequest {
    public static int TYPE = -1;

    @Override
    public DoPostRequest map2Json(Map<String, String> params) {
        TYPE = 0;
        this.queryMap = params;
        return this;
    }

    @Override
    public DoPostRequest map2form(Map<String, String> params) {
        TYPE = 1;
        this.queryMap = params;
        return this;
    }

    @Override
    public DoPostRequest url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public DoPostRequest addHeader(String key, String value) {
        this.header.put(key, value);
        return this;
    }
}
