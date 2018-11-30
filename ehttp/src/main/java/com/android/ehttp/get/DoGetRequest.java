package com.android.ehttp.get;

import com.android.ehttp.DoRequest;
import com.android.ehttp.Request;
import com.android.ehttp.RequestCallback;
import com.android.ehttp.Response;

import java.io.IOException;
import java.util.HashMap;

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
public abstract class DoGetRequest extends DoRequest<GetRequest> implements GetRequest {
    @Override
    public DoGetRequest map2params(HashMap<String, String> objectObjectHashMap) {
        this.queryMap = objectObjectHashMap;
        return this;
    }

    @Override
    public DoGetRequest url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public DoGetRequest addHeader(String key, String value) {
        header.put(key, value);
        return this;
    }


}
