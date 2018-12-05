package com.android.ehttp.body;

import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public List<Part>          parts;
    public Map<String, String> params;

    private PostFormBody(Builder builder) {
        params = builder.params;
        parts = builder.parts;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse(TYPE_FORM_DATA);
    }

    public static final class Builder {
        private Map<String, String> params;


        private List<Part> parts;

        public Builder() {
            params = new HashMap<>();
            parts = new ArrayList<>();
        }

        public Builder addParams(String key, String value) {
            params.put(key, value);
            return this;
        }

        public Builder addKeyAndFile(String key, String name, File file) {
            parts.clear();
            parts.add(new Part(key, name, file));
            return this;
        }

        public Builder addKeyAndFile(String key, File file) {
            return addKeyAndFile(key, file.getName(), file);
        }

        public Builder addFileParts(List<Part> parts) {
            this.parts = parts;
            return this;
        }

        public PostFormBody build() {
            return new PostFormBody(this);
        }
    }
}
