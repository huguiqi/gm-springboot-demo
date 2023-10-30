package com.example.demo.common.restClient;

import org.apache.http.ssl.SSLContexts;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created with IntelliJ IDEA.
 *
 * @version v1
 * @Author: sam.hu (huguiqi@zaxh.cn)
 * @Copyright (c) 2023, zaxh Group All Rights Reserved.
 * @since: 2023/10/11/18:32
 * @summary:
 */
public class HttpsClientRequestFactory  extends SimpleClientHttpRequestFactory {

    private static final String PREFERRED_CIPHER_SUITE = "ECC_SM4_CBC_SM3";
//    private static final String PREFERRED_CIPHER_SUITE = "RSA_EXPORT";
//    private static final String PREFERRED_CIPHER_SUITE = "ECDHE-SM2-WITH-SMS4-GCM-SM3";

    static {
        try {
            Security.insertProviderAt((Provider)Class.forName("cn.gmssl.jce.provider.GMJCE").newInstance(), 1);
            Security.insertProviderAt((Provider)Class.forName("cn.gmssl.jsse.provider.GMJSSE").newInstance(), 2);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) {
        try {
            if (!(connection instanceof HttpsURLConnection)) {
                throw new RuntimeException("An instance of HttpsURLConnection is expected");
            }

            HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("GMSSLv1.1", "GMJSSE");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            sslContext.getServerSessionContext().setSessionCacheSize(8192);
            sslContext.getServerSessionContext().setSessionTimeout(3600);
            MyCustomSSLSocketFactory fact2 = new MyCustomSSLSocketFactory(sslContext.getSocketFactory());
            httpsConnection.setSSLSocketFactory(fact2);
            httpsConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            super.prepareConnection(httpsConnection, httpMethod);
            httpsConnection.setDoOutput(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


     static private class MyCustomSSLSocketFactory extends SSLSocketFactory {
        private final SSLSocketFactory delegate;
        public MyCustomSSLSocketFactory(SSLSocketFactory delegate) {
            this.delegate = delegate;
        }

        // 返回默认启用的密码套件。除非一个列表启用，对SSL连接的握手会使用这些密码套件。
        // 这些默认的服务的最低质量要求保密保护和服务器身份验证
        @Override
        public String[] getDefaultCipherSuites() {
            return setupPreferredSupportedCipherSuites();
        }

        // 返回的密码套件可用于SSL连接启用的名字
        @Override
        public String[] getSupportedCipherSuites() {
            return setupPreferredSupportedCipherSuites();
        }

        @Override
        public Socket createSocket(final Socket socket, final String host, final int port,
                                   final boolean autoClose) throws IOException {
            final Socket underlyingSocket = delegate.createSocket(socket, host, port, autoClose);
            return overrideProtocol(underlyingSocket);
        }

        @Override
        public Socket createSocket(final String host, final int port) throws IOException {
            final Socket underlyingSocket = delegate.createSocket(host, port);
            return overrideProtocol(underlyingSocket);
        }

        @Override
        public Socket createSocket(final String host, final int port, final InetAddress localAddress,
                                   final int localPort) throws
                IOException {
            final Socket underlyingSocket = delegate.createSocket(host, port, localAddress, localPort);
            return overrideProtocol(underlyingSocket);
        }

        @Override
        public Socket createSocket(final InetAddress host, final int port) throws IOException {
            final Socket underlyingSocket = delegate.createSocket(host, port);
            return overrideProtocol(underlyingSocket);
        }

        @Override
        public Socket createSocket(final InetAddress host, final int port, final InetAddress localAddress,
                                   final int localPort) throws
                IOException {
            final Socket underlyingSocket = delegate.createSocket(host, port, localAddress, localPort);
            return overrideProtocol(underlyingSocket);
        }

        private Socket overrideProtocol(final Socket socket) {
            if (!(socket instanceof SSLSocket)) {
                throw new RuntimeException("An instance of SSLSocket is expected");
            }
//            ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1","TLSv1.1","TLSv1.2"});
            ((SSLSocket) socket).setEnabledProtocols(new String[]{"GMSSLv1.1"});
            return socket;
        }
    }

    private static String[] setupPreferredSupportedCipherSuites()
    {
//        ArrayList<String> suitesList = new ArrayList<String>(Arrays.asList(new String[] {"ECDHE-RSA-AES128-GCM-SHA256","AES128-SHA","DES-CBC3-SHA","ECC-SM4-CBC-SM3","ECC-SM4-GCM-SM3"}));
        ArrayList<String> suitesList = new ArrayList<String>(Arrays.asList(new String[] {PREFERRED_CIPHER_SUITE}));
        return suitesList.toArray(new String[suitesList.size()]);
    }
}






