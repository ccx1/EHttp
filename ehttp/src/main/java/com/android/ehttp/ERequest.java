package com.android.ehttp;

import com.android.ehttp.body.PostBody;

import java.util.HashMap;
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
public class ERequest {
    public               PostBody            body;
    public               String              url;
    public               Map<String, String> header;
    public               int                 readTimeOut;
    public               int                 writeTimeOut;
    public               int                 connectTimeOut;
    private static final String              GET  = "GET";
    private static final String              POST = "POST";
    public               String              method;
    public               Map<String, String> params;

    private ERequest(Builder builder) {
        url = builder.url;
        header = builder.header;
        readTimeOut = builder.readTimeOut;
        writeTimeOut = builder.writeTimeOut;
        connectTimeOut = builder.connectTimeOut;
        method = builder.method;
        params = builder.params;
        if (method.equalsIgnoreCase(GET)) {
            url = CallHelper.getInstance().getQueryUrl(url, params);
        }
        body = builder.body;
    }


    public static final class Builder {
        private String              url;
        private Map<String, String> header;
        private int                 readTimeOut;
        private int                 writeTimeOut;
        private int                 connectTimeOut;
        private String              method;
        private Map<String, String> params;
        private PostBody            body;

        public Builder() {
            header = new HashMap<>();
            readTimeOut = 3000;
            writeTimeOut = 3000;
            connectTimeOut = 3000;
            method = GET;
            params = new HashMap<>();
        }

        public Builder get() {
            method = GET;
            return this;
        }

        public Builder post(PostBody postBody) {
            method = POST;
            this.body = postBody;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder addParams(String key, String value) {
            params.put(key, value);
            return this;
        }


        public Builder addHeader(String key, String value) {
            header.put(key, value);
            return this;
        }

        public void setHeader(Map<String, String> header) {
            this.header = header;
        }


        public ERequest build() {
            return new ERequest(this);
        }

        public Builder setReadTimeOut(int val) {
            readTimeOut = val;
            return this;
        }

        public Builder setWriteTimeOut(int val) {
            writeTimeOut = val;
            return this;
        }

        public Builder setConnectTimeOut(int val) {
            connectTimeOut = val;
            return this;
        }
    }
}
