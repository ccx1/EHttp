package com.android.ehttp.body;

import android.support.annotation.Nullable;

import java.nio.charset.Charset;

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
public abstract class PostBody implements RequestBody {

    @Nullable
    public abstract MediaType contentType();

    public long contentLength() {
        return -1;
    }

    public PostModel create() {
        return null;
    }


    public static PostBody create(MediaType mediaType, String content) {
        Charset charset = UTF_8;
        if (mediaType != null) {
            charset = mediaType.charset();
            if (charset == null) {
                charset = UTF_8;
                mediaType = MediaType.parse(mediaType + "; charset=utf-8");
            }
        }
        byte[] bytes = content.getBytes(charset);
        return create(mediaType, bytes);
    }

    public static PostBody create(MediaType mediaType, byte[] content) {
        return create(mediaType, content, 0, content.length);
    }

    private static PostBody create(final MediaType mediaType, final byte[] content, final int i, final int length) {
        // 记录数据
        return new PostBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() {
                return length;
            }

            @Override
            public PostModel create() {
                return new PostModel(content, i, length);
            }
        };
    }

}
