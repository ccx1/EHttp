package com.android.ehttp;

import com.android.ehttp.post.DoPostRequest;

import org.json.JSONObject;

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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.android.ehttp.DoRequest.POST;

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
    private        String              method;
    private        Map<String, String> header;
    private        RequestCallback     requestCallback;
    private        String              url;
    private static SSLContext          ctx           = null;
    private static HostnameVerifier    verifier      = null;
    private static SSLSocketFactory    socketFactory = null;
    private        HttpURLConnection   connection;
    private        InputStream         resultStream;

    public CallRequest(String queryUrl, Map<String, String> header, String method, RequestCallback requestCallback) {
        this(queryUrl, null, header, method, requestCallback);
    }

    public CallRequest(String queryUrl, Map<String, String> header, String method) {
        this(queryUrl, header, method, null);
    }

    public CallRequest(String url, Map<String, String> queryMap, Map<String, String> header, String method, RequestCallback requestCallback) {
        this.url = url;
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
            if (this.getMethod().equalsIgnoreCase(POST)) {
                switch (DoPostRequest.TYPE) {
                    case 0:
                        Map<String, String> queryMap = this.getQueryMap();
                        if (queryMap == null) {
                            break;
                        }
                        JSONObject jsonObject = new JSONObject(queryMap);
                        String json = jsonObject.toString();
                        OutputStream outputStream = this.getConnection().getOutputStream();
                        outputStream.write(json.getBytes());
                        outputStream.flush();
                        outputStream.close();
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
            }
            this.getConnection().connect();
            InputStream inputStream = this.getConnection().getInputStream();
            this.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(e, this);
        }
        return new Response(this);
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
