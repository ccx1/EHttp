package com.android.ehttp.body;

import android.support.annotation.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.android.ehttp.Queue.TYPE_FORM_DATA;

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
public class PostFormBody extends PostBody {

    public Map<String, String> params;
    public String              keyFile;
    public File                mFile;

    private PostFormBody(Builder builder) {
        params = builder.params;
        keyFile = builder.keyFile;
        mFile = builder.mFile;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse(TYPE_FORM_DATA);
    }

    public static final class Builder {
        private Map<String, String> params;
        private String              keyFile;
        private File                mFile;

        public Builder() {
            params = new HashMap<>();
        }

        public Builder addParams(String key, String value) {
            params.put(key, value);
            return this;
        }

        public Builder addKeyAndFile(String key, File file) {
            keyFile = key;
            mFile = file;
            return this;
        }

        public PostFormBody build() {
            return new PostFormBody(this);
        }
    }
}
