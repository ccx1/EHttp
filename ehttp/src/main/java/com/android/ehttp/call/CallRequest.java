package com.android.ehttp.call;

import android.support.annotation.NonNull;

import com.android.ehttp.ERequest;
import com.android.ehttp.HostnameVerifier;
import com.android.ehttp.RequestCallback;
import com.android.ehttp.Response;
import com.android.ehttp.body.PostModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownServiceException;
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
    private        byte[]              resultByte;

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

    public void setInputStream(InputStream inputStream) throws IOException {
        this.resultStream = inputStream;
//        InputStream inputStream = resultStream;
        if (inputStream == null) {
            throw new UnknownServiceException("service connect fail");
        }
        ByteArrayOutputStream bos = getResult(resultStream);
        this.resultByte = bos.toByteArray();
    }

    @NonNull
    private ByteArrayOutputStream getResult(InputStream inputStream) throws IOException {

        byte[]                buffer = new byte[1024];
        int                   len;
        ByteArrayOutputStream bos    = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        inputStream.close();
        return bos;
    }

    public InputStream getResultStream() {
        return resultStream;
    }

    public byte[] getResultByte() {
        return resultByte;
    }

    public Response execute() {
        try {
            new CallRequestPost.CallRequestPostHolder(this).build();
            this.getConnection().connect();
            InputStream inputStream = this.getConnection().getInputStream();
            this.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            InputStream errorStream = this.getConnection().getErrorStream();
            try {
                this.setInputStream(errorStream);
                return new Response(e, this);
            } catch (IOException e1) {
                e1.printStackTrace();
                return new Response(e1, this);
            }

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
