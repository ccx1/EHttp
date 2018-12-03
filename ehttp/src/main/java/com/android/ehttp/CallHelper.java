package com.android.ehttp;

import android.text.TextUtils;

import java.util.Map;

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
public class CallHelper {

    private static CallHelper sCallHelper;

    private CallHelper() {
    }

    public static CallHelper getInstance() {
        if (sCallHelper == null) {
            sCallHelper = new CallHelper();
        }
        return sCallHelper;
    }

    /**
     * 拼接get数据
     *
     * @param url    地址
     * @param params get参数
     * @return 新的url
     */
    public String getQueryUrl(String url, Map<String, String> params) {
        StringBuilder neoUrl = new StringBuilder(url);
        if (params != null && !params.isEmpty()) {
            if (!TextUtils.isEmpty(url)) {
                neoUrl.append("?");
            }
            for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
                neoUrl.append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue()).append("&");
            }
            neoUrl = new StringBuilder(neoUrl.substring(0, neoUrl.length() - 1));
        }
        return neoUrl.toString();
    }


}
