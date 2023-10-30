//package com.example.demo.common.restClient;
//
//import org.apache.http.config.Registry;
//import org.apache.http.config.RegistryBuilder;
//import org.apache.http.conn.socket.ConnectionSocketFactory;
//import org.apache.http.conn.socket.PlainConnectionSocketFactory;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.impl.client.BasicCookieStore;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.ssl.SSLContexts;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.DefaultResourceLoader;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.client.ResponseErrorHandler;
//import org.springframework.web.client.RestTemplate;
//
//import javax.net.ssl.*;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Array;
//import java.security.*;
//import java.security.cert.X509Certificate;
//import org.apache.http.conn.ssl.NoopHostnameVerifier;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Created by guiqi on 2017/8/10.
// */
//@Configuration
//public class RestBeanConfig {
//
//
//    private static final String PREFERRED_CIPHER_SUITE = "ECC_SM4_CBC_SM3";
//
//    /**
//     * resources 目录下证书路径
//     * cerPath = "classpath:/keystore/client_trust.keystore";
//     */
////    @Value("${cer-path:classpath:/keystore/client_trust.keystore}")
////    private String cerPath;
//
//    /**
//     * 使用"keytool"命令导入证书时输入的密码
//     * cerPwd = "111111";
//     */
////    @Value("${cer-pwd:111111}")
////    private String cerPwd;
//
//
//
////    @Bean
////    public List<HttpMessageConverter<?>>  messageConverters(){
////
////        // 添加转换器
////        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
////        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
////        messageConverters.add(new FormHttpMessageConverter());
////        messageConverters.add(new MappingJackson2HttpMessageConverter());
////
////        return messageConverters;
////    }
//
//
//    static {
//        try {
//            Security.insertProviderAt((Provider)Class.forName("cn.gmssl.jce.provider.GMJCE").newInstance(), 1);
//            Security.insertProviderAt((Provider)Class.forName("cn.gmssl.jsse.provider.GMJSSE").newInstance(), 2);
//        } catch (InstantiationException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Bean(name = "cerRestTemplate")
//    public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory requestFactory, @Autowired List<HttpMessageConverter<?>> messageConverters) throws NoSuchAlgorithmException, KeyManagementException, NoSuchProviderException {
//        RestTemplate restTemplate = new RestTemplate(messageConverters);
//        restTemplate.setRequestFactory(requestFactory);
//        restTemplate.setErrorHandler(new ResponseErrorHandler() {
//                                         @Override
//                                         public boolean hasError(ClientHttpResponse clientHttpResponse) {
//                                             return false;
//                                         }
//
//                                         @Override
//                                         public void handleError(ClientHttpResponse clientHttpResponse) {
//                                             //默认处理非200的返回，会抛异常
//                                         }
//                                     });
//
//
//
////        SSLContext sslContext = SSLContext.getInstance("SSL");
////        SSLContext sslContext = SSLContext.getInstance("GMSSLv1.1", "GMJSSE");
//        SSLContext sslContext = SSLContext.getInstance("GMSSLv1.1", "GMJSSE");
//        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
//            @Override
//            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
//
//            }
//
//            @Override
//            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
//
//            }
//
//            @Override
//            public X509Certificate[] getAcceptedIssuers() {
//                return new X509Certificate[]{};
//            }
//        }}, new java.security.SecureRandom());
//
//
//
//        return restTemplate;
//    }
//
//    @Bean
//    public HttpComponentsClientHttpRequestFactory requestFactory() throws Exception {
//        CloseableHttpClient httpClient = createCloseableHttpClient();
//        HttpComponentsClientHttpRequestFactory httpsFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//        httpsFactory.setReadTimeout(2000);
//        httpsFactory.setConnectTimeout(2000);
//        return httpsFactory;
//    }
//
//
//    /**
//     * https协议证书认证
//     *
//     * @return
//     * @throws Exception
//     */
//    private CloseableHttpClient createCloseableHttpClient() throws Exception {
////        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
////        keyStore.load(resourceLoader(cerPath), cerPwd.toCharArray());
////        SSLContext sslContext = SSLContexts.createSystemDefault();
////                .loadTrustMaterial(keyStore, new TrustSelfSignedStrategy()).build();
//        // 这里的通信协议要根据使用的JDK版本来适配
////        SSLConnectionSocketFactory sslfactory = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
//
//        SSLConnectionSocketFactory sslfactory = createSocketFactory(null,null);
//
//        SSLContext ctx = SSLContext.getInstance("GMSSLv1.1");
//        ctx.init(null, null, null);
//        SSLContext.setDefault(ctx);
//
//        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//                .register("http", PlainConnectionSocketFactory.getSocketFactory())
//                .register("https", sslfactory)
//                .build();
//
//        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//        connMgr.setMaxTotal(200);
//        connMgr.setDefaultMaxPerRoute(100);
//
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//
//        httpClientBuilder.setConnectionManager(connMgr);
//        httpClientBuilder.setDefaultCookieStore(new BasicCookieStore());
//        CloseableHttpClient closeableHttpClient = httpClientBuilder.setSSLSocketFactory(sslfactory).build();
//        return closeableHttpClient;
//    }
//
//
//    public  SSLConnectionSocketFactory createSocketFactory(KeyStore kepair, char[] pwd) throws Exception
//    {
//        TrustAllManager[] trust = { new TrustAllManager() };
//
//        KeyManager[] kms = null;
//        if (kepair != null)
//        {
//            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
//            kmf.init(kepair, pwd);
//            kms = kmf.getKeyManagers();
//        }
//
//        SSLContext ctx = SSLContext.getInstance("GMSSLv1.1", "GMJSSE");
//        java.security.SecureRandom secureRandom = new java.security.SecureRandom();
//        ctx.init(kms, trust, secureRandom);
//
//
//        ctx.getServerSessionContext().setSessionCacheSize(8192);
//        ctx.getServerSessionContext().setSessionTimeout(3600);
//
////        SSLSocketFactory factory = ctx.getSocketFactory();
//
//        SSLContext.setDefault(ctx);
//
//        ArrayList<String> suitesList = new ArrayList<String>(Arrays.asList(new String[] {PREFERRED_CIPHER_SUITE}));
//        ArrayList<String> suportProcolList = new ArrayList<String>(Arrays.asList(new String[] {"GMSSLv1.1","TLSv1","TLSv1.1","TLSv1.2"}));
//
//
//        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(ctx,suportProcolList.toArray(new String[suportProcolList.size()]),suitesList.toArray(new String[suitesList.size()]),new HostnameVerifier()
//        {
//            public boolean verify(String hostname, SSLSession session)
//            {
//                return true;
//            }
//        });
//        return sslConnectionSocketFactory;
//    }
//
//    /**
//     * 读取文件信息
//     *
//     * @param fileFullPath
//     * @return
//     */
//    public InputStream resourceLoader(String fileFullPath) throws IOException {
//        ResourceLoader resourceLoader = new DefaultResourceLoader();
//        return resourceLoader.getResource(fileFullPath).getInputStream();
//    }
//
//
//
//}
