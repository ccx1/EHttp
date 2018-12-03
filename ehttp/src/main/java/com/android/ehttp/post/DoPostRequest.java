package com.android.ehttp.post;

import android.support.annotation.NonNull;

import com.android.ehttp.DoRequest;

import java.io.File;
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
    public static       int TYPE = -1;
    public static final int JSON = 0;
    public static final int FORM = 1;
    public static final int FILE = 2;

    @Override
    public DoPostRequest map2Json(Map<String, String> params) {
        TYPE = JSON;
        this.queryMap = params;
        return this;
    }

    @Override
    public DoPostRequest map2form(Map<String, String> params) {
        TYPE = FORM;
        this.queryMap = params;
        return this;
    }

    @Override
    public DoPostRequest map2formPostFile(Map<String, String> params, @NonNull File file) {
        return map2formPostFile(params, file.getName(), file);
    }

    @Override
    public DoPostRequest map2formPostFile(Map<String, String> params, String key, File file) {
        TYPE = FILE;
        this.queryMap = params;
        this.file = file;
        this.key = key;
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
