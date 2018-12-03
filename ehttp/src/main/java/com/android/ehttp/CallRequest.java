package com.android.ehttp;

import com.android.ehttp.body.PostBody;
import com.android.ehttp.body.PostFormBody;
import com.android.ehttp.body.PostModel;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.android.ehttp.Queue.CONTENT_DISPOSITION;
import static com.android.ehttp.Queue.CONTENT_TYPE;
import static com.android.ehttp.Queue.HTTPS;
import static com.android.ehttp.Queue.POST;

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
public class CallRequest {
    private        Map<String, String> queryMap;
    private        PostModel           mP;
    private        ERequest            eRequest;
    private        String              method;
    private        Map<String, String> header;
    private        RequestCallback     requestCallback;
    private        String              url;
    private static SSLContext          ctx           = null;
    private static HostnameVerifier    verifier      = null;
    private static SSLSocketFactory    socketFactory = null;
    private        HttpURLConnection   connection;
    private        InputStream         resultStream;

    public CallRequest(ERequest eRequest, RequestCallback requestCallback) {
        this.eRequest = eRequest;
        this.requestCallback = requestCallback;
    }

    public CallRequest(ERequest eRequest) {
        this(eRequest, null);
    }

    public void build() throws IOException {
        // 生产链接
        HttpURLConnection connection = getConnection(eRequest);
        for (Map.Entry<String, String> stringStringEntry : eRequest.header.entrySet()) {
            connection.setRequestProperty(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
        this.connection = connection;

    }

    public String getMethod() {
        return eRequest.method;
    }

    public ERequest getERequest() {
        return eRequest;
    }

    public RequestCallback getRequestCallback() {
        return requestCallback;
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    private HttpURLConnection getConnection(ERequest eRequest) throws
            IOException {
        HttpURLConnection conn = null;
        URL               url  = new URL(eRequest.url);
        if (HTTPS.equals(url.getProtocol())) {
            HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
            connHttps.setSSLSocketFactory(socketFactory);
            connHttps.setHostnameVerifier(verifier);
            conn = connHttps;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setRequestMethod(eRequest.method);
        conn.setAllowUserInteraction(true);
        conn.setConnectTimeout(eRequest.connectTimeOut);
        conn.setReadTimeout(eRequest.readTimeOut);
        conn.setInstanceFollowRedirects(true);
        if (eRequest.method.equalsIgnoreCase(POST)) {
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
        }
        return conn;
    }

    static {
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager(null)}, new SecureRandom());
            ctx.getClientSessionContext().setSessionTimeout(15);
            ctx.getClientSessionContext().setSessionCacheSize(1000);
            socketFactory = ctx.getSocketFactory();
        } catch (Exception ignored) {

        }
        verifier = new HostnameVerifier();
    }

    public void setInputStream(InputStream inputStream) {
        this.resultStream = inputStream;
    }

    public InputStream getResultStream() {
        return resultStream;
    }

    public Response execute() {
        try {
            appendParams();
            this.getConnection().connect();
            InputStream inputStream = this.getConnection().getInputStream();
            this.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(e, this);
        }
        return new Response(this);
    }

    private void appendParams() throws IOException {
        if (this.getMethod().equalsIgnoreCase(POST)) {
            PostBody body = this.getERequest().body;
            if (body instanceof PostFormBody) {
                PostFormBody b = (PostFormBody) body;
                if (b.mFile == null) {
                    postForm(b);
                } else {
                    postFile(b);
                }
            } else {
                postJson();
            }

        }
    }

    private void postJson() throws IOException {
        OutputStream outputStream;
        ERequest     eRequest = this.getERequest();
        long         l        = eRequest.body.contentLength();
        if (l == 0) {
            return;
        }
        outputStream = this.getConnection().getOutputStream();
        outputStream.write(eRequest.body.create().getBuff());
        outputStream.flush();
        outputStream.close();
    }

    private void postForm(PostFormBody b) throws IOException {
        OutputStream outputStream;
        String       queryUrl = CallHelper.getInstance().getQueryUrl("", b.params);
        outputStream = this.getConnection().getOutputStream();
        outputStream.write(queryUrl.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private void postFile(PostFormBody b) throws IOException {
        // 边界标识 随机生成
        String boundary = UUID.randomUUID().toString();
        String prefix   = "--";
        String lineEnd  = "\r\n";

        DataOutputStream    out      = new DataOutputStream(this.getConnection().getOutputStream());
        Map<String, String> queryMap = b.params;
        if (queryMap != null && !queryMap.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : queryMap.entrySet()) {
                String key   = entry.getKey();
                String value = entry.getValue();
                sb.append(prefix).append(boundary).append(lineEnd);
                sb.append(CONTENT_DISPOSITION + ": form-data; name=\"")
                        .append(key).append("\"").append(lineEnd)
                        .append(lineEnd);
                sb.append(value).append(lineEnd);
            }
            out.write(sb.toString().getBytes());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(boundary);
        sb.append(lineEnd);
        sb.append(CONTENT_DISPOSITION + ": form-data; name=\"").append(b.keyFile).append("\"; filename=\"").append(b.mFile.getName()).append("\"").append(lineEnd);
        sb.append(CONTENT_TYPE + Queue.TYPE_STREAM).append(lineEnd);
        sb.append(lineEnd);
        out.write(sb.toString().getBytes());
        InputStream input = new FileInputStream(b.mFile);
        byte[]      bytes = new byte[1024];
        int         len   = 0;
        while ((len = input.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        out.write(lineEnd.getBytes());
        byte[] endData = (prefix + boundary + prefix + lineEnd).getBytes();
        out.write(endData);
        input.close();
        out.flush();
        out.close();
    }


    private static class DefaultTrustManager implements X509TrustManager {
        private DefaultTrustManager(Object o) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }

}
