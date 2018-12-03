package com.android.ehttp;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.android.ehttp.post.DoPostRequest;
import com.android.ehttp.post.PostModel;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

import static com.android.ehttp.DoRequest.POST;
import static com.android.ehttp.Queue.CONTENT_DISPOSITION;
import static com.android.ehttp.Queue.CONTENT_TYPE;

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
    private final  Map<String, String> queryMap;
    private final  PostModel           mP;
    private        String              method;
    private        Map<String, String> header;
    private        RequestCallback     requestCallback;
    private        String              url;
    private static SSLContext          ctx           = null;
    private static HostnameVerifier    verifier      = null;
    private static SSLSocketFactory    socketFactory = null;
    private        HttpURLConnection   connection;
    private        InputStream         resultStream;

    public CallRequest(String queryUrl, Map<String, String> header, String method) {
        this(queryUrl, null, header, method, null, null);
    }

    public CallRequest(String queryUrl, Map<String, String> header, String method, RequestCallback requestCallback) {
        this(queryUrl, null, header, method, null, requestCallback);
    }

    public CallRequest(String queryUrl, Map<String, String> queryMap, Map<String, String> header, String method, PostModel p) {
        this(queryUrl, queryMap, header, method, p, null);
    }

    public CallRequest(String url, Map<String, String> queryMap, Map<String, String> header, String method, PostModel p, RequestCallback requestCallback) {
        this.url = url;
        this.mP = p;
        this.queryMap = queryMap;
        this.header = header;
        this.method = method;
        this.requestCallback = requestCallback;
    }

    public void build() throws IOException {
        // 生产链接
        HttpURLConnection connection = getConnection(new URL(url), method);
        for (Map.Entry<String, String> stringStringEntry : header.entrySet()) {
            connection.setRequestProperty(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
        this.connection = connection;

    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getQueryMap() {
        return queryMap;
    }

    public RequestCallback getRequestCallback() {
        return requestCallback;
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    private HttpURLConnection getConnection(URL url, String method) throws
            IOException {
        HttpURLConnection conn = null;
        if (DoRequest.HTTPS.equals(url.getProtocol())) {
            HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
            connHttps.setSSLSocketFactory(socketFactory);
            connHttps.setHostnameVerifier(verifier);
            conn = connHttps;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setRequestMethod(method);
        conn.setAllowUserInteraction(true);
        conn.setInstanceFollowRedirects(true);
        if (method.equalsIgnoreCase(DoRequest.POST)) {
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
            OutputStream outputStream = null;
            switch (DoPostRequest.TYPE) {
                case DoPostRequest.JSON:
                    postJson();
                    break;
                case DoPostRequest.FORM:
                    postForm();
                    break;
                case DoPostRequest.FILE:
                    postFile();
                    break;
                default:
                    break;
            }
        }
    }

    private void postJson() throws IOException {
        OutputStream        outputStream;
        Map<String, String> queryMap = this.getQueryMap();
        if (queryMap == null) {
            return;
        }
        JSONObject jsonObject = new JSONObject(queryMap);
        String     json       = jsonObject.toString();
        outputStream = this.getConnection().getOutputStream();
        outputStream.write(json.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private void postForm() throws IOException {
        OutputStream outputStream;
        String       queryUrl = CallHelper.getInstance().getQueryUrl("", this.getQueryMap());
        outputStream = this.getConnection().getOutputStream();
        outputStream.write(queryUrl.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private void postFile() throws IOException {
        // 边界标识 随机生成
        String boundary = UUID.randomUUID().toString();
        String prefix   = "--";
        String lineEnd  = "\r\n";

        DataOutputStream    out      = new DataOutputStream(this.getConnection().getOutputStream());
        Map<String, String> queryMap = this.getQueryMap();
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
        if (!mP.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix);
            sb.append(boundary);
            sb.append(lineEnd);
            sb.append(CONTENT_DISPOSITION + ": form-data; name=\"").append(mP.getName()).append("\"; filename=\"").append(mP.getFile().getName()).append("\"").append(lineEnd);
            sb.append(CONTENT_TYPE + Queue.TYPE_STREAM).append(lineEnd);
            sb.append(lineEnd);
            out.write(sb.toString().getBytes());
            InputStream input = new FileInputStream(mP.getFile());
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
        }
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
