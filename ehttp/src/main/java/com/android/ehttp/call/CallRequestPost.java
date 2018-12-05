package com.android.ehttp.call;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.android.ehttp.Queue;
import com.android.ehttp.body.Part;
import com.android.ehttp.body.PostBody;
import com.android.ehttp.body.PostFormBody;
import com.android.ehttp.body.PostModel;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Random;

import static com.android.ehttp.Queue.CONTENT_DISPOSITION;
import static com.android.ehttp.Queue.CONTENT_LENGTH;
import static com.android.ehttp.Queue.CONTENT_TYPE;
import static com.android.ehttp.Queue.TYPE_FORM_DATA_NO_CHARSET;
import static com.android.ehttp.Queue.TYPE_FORM_DATA_NO_CHARSET_NO_MULTIPART;

/**
 * ================================================
 *
 * @author ：ccx
 * 版    本：1.0
 * 创建日期：2018/12/4
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class CallRequestPost {

    private final CallRequest currentCallRequest;

    private CallRequestPost(CallRequest currentCallRequest) {
        this.currentCallRequest = currentCallRequest;
    }


    private void build() throws IOException {
        appendParams();
    }

    private void appendParams() throws IOException {
        PostBody body = currentCallRequest.getERequest().body;
        if (body instanceof PostFormBody) {
            postForm((PostFormBody) body);
        } else {
            PostModel postModel = body.create();
            if (postModel.getLength() == 0) {
                throw new IOException("Content-Length == 0");
            }
            post(postModel.getBuff());
        }
    }

    private void postForm(PostFormBody body) throws IOException {
        // 如果是表单，则用表单提交
        // 否则为直接提交
        if (body.parts != null && !body.parts.isEmpty()) {
            postFile(body);
        } else {
            String queryUrl = CallHelper.getInstance().getQueryUrl("", body.params);
            post(queryUrl.getBytes());
        }
    }


    private void post(byte[] buff) throws IOException {
        OutputStream outputStream = currentCallRequest.getConnection().getOutputStream();
        outputStream.write(buff);
        outputStream.flush();
        outputStream.close();
    }

    private void postFile(PostFormBody body) throws IOException {
        // 说明有文件
        // 边界标识 随机生成
        Random random      = new Random();
        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        String boundary = Base64.encodeToString(randomBytes, Base64.NO_WRAP);
        String prefix   = "--";
        String lineEnd  = "\r\n";
        currentCallRequest.getConnection().setRequestProperty(CONTENT_TYPE, TYPE_FORM_DATA_NO_CHARSET + "; boundary=" + boundary);
        OutputStream     outputStream = currentCallRequest.getConnection().getOutputStream();
        DataOutputStream out          = putParams(body, boundary, prefix, lineEnd, outputStream);
        putFile(body, boundary, prefix, lineEnd, out);
        out.flush();
        out.close();
    }

    @NonNull
    private DataOutputStream putParams(PostFormBody body, String boundary, String prefix, String lineEnd, OutputStream outputStream) throws IOException {
        DataOutputStream    out      = new DataOutputStream(outputStream);
        Map<String, String> queryMap = body.params;
        if (queryMap != null && !queryMap.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : queryMap.entrySet()) {
                String key   = entry.getKey();
                String value = entry.getValue();
                sb.append(prefix).append(boundary).append(lineEnd);
                out.write(sb.toString().getBytes());
                sb.append(CONTENT_DISPOSITION)
                        .append(": ")
                        .append(TYPE_FORM_DATA_NO_CHARSET_NO_MULTIPART)
                        .append("; name=\"")
                        .append(key)
                        .append("\"")
                        .append(lineEnd);
                sb.append(CONTENT_LENGTH)
                        .append(": ")
                        .append(value.length())
                        .append(lineEnd)
                        .append(lineEnd);
                sb.append(value).append(lineEnd);

            }
            out.write(sb.toString().getBytes());
            out.flush();
        }
        return out;
    }

    private void putFile(PostFormBody body, String boundary, String prefix, String lineEnd, DataOutputStream out) throws IOException {
        if (body.parts != null && !body.parts.isEmpty()) {
            for (Part part : body.parts) {
                StringBuilder sb = new StringBuilder();
                sb.append(prefix).append(boundary).append(lineEnd);
                sb.append(CONTENT_DISPOSITION)
                        .append(": ")
                        .append(TYPE_FORM_DATA_NO_CHARSET_NO_MULTIPART)
                        .append("; name=\"")
                        .append(part.getKeyFile())
                        .append("\"; filename=\"")
                        .append(part.getFileName())
                        .append("\"")
                        .append(lineEnd);
                sb.append(CONTENT_TYPE + ": " + Queue.TYPE_STREAM).append(lineEnd);
                sb.append(lineEnd);
                out.write(sb.toString().getBytes());
                out.flush();
                InputStream input = new FileInputStream(part.getFile());
                byte[]      bytes = new byte[1024];
                int         len   = 0;
                while ((len = input.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }
                out.write(lineEnd.getBytes());
                out.flush();
                byte[] endData = (prefix + boundary + prefix + lineEnd).getBytes();
                out.write(endData);
                out.flush();
                input.close();
            }
        }

    }


    public static class CallRequestPostHolder {
        private CallRequestPost mCallRequestPost;

        public CallRequestPostHolder(CallRequest callRequest) {
            mCallRequestPost = new CallRequestPost(callRequest);
        }

        public void build() throws IOException {
            mCallRequestPost.build();
        }
    }

}
