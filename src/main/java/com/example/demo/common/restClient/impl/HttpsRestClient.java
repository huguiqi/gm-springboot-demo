package com.example.demo.common.restClient.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.common.restClient.HttpsClientRequestFactory;
import com.example.demo.common.restClient.RestClient;
import com.example.demo.common.restClient.domain.BaseModel;
import com.example.demo.common.restClient.domain.BaseResponse;
import com.fasterxml.jackson.databind.ser.std.ByteArraySerializer;
import org.apache.commons.beanutils.BeanMap;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.tomcat.util.buf.ByteBufferUtils;
import org.bc.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by guiqi on 2017/8/10.
 */

public class HttpsRestClient implements RestClient {


    private Logger logger = LoggerFactory.getLogger(getClass());

//    @Resource
    private HttpsClientRequestFactory clientRequestFactory = new HttpsClientRequestFactory();




    public Object post(String url, Object data) {
        return sendFor(url, data);
    }



    public Object get(String url, Object data) {
        try {
            ClientHttpRequest request = this.clientRequestFactory.createRequest(URI.create(url),HttpMethod.GET);

            request.getHeaders().setContentType(MediaType.TEXT_HTML);
            request.getHeaders().setAccept(Arrays.asList(MediaType.TEXT_HTML));
            ClientHttpResponse response = request.execute();
            System.out.println(response.getStatusCode().toString());
        } catch (Exception eek) {
            System.out.println("** Exception: " + eek.getMessage());
            logger.error(eek.getMessage(),eek);
        }
        return null;
    }

    public Object put(String url, Object data) {
        return sendFor(url, data);
    }

    private Object sendFor(String url, Object data) {
        try {
            ClientHttpRequest request = this.clientRequestFactory.createRequest(URI.create(url),HttpMethod.POST);
            request.getHeaders().clearContentHeaders();
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            request.getHeaders().addIfAbsent("sign","6379fa8e554585470aaad3a332deafd9");
            String json = (String) data;;
            FileCopyUtils.copy(json.getBytes(), request.getBody());
            ClientHttpResponse response = null;
            try {
                 response = request.execute();
                System.out.println(response.getStatusCode().toString());
                System.out.println("Result - status (" + response.getStatusCode() + ") has body: " + response.getBody());
                if (response.getStatusCode().value() == HttpStatus.OK.value()){
                    InputStream inputStream = response.getBody();
                    String text = new BufferedReader(
                            new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    System.out.println(text);
                    return response.getBody();
                }
            } finally {
                response.close();
            }
        } catch (Exception eek) {
            System.out.println("** Exception: " + eek.getMessage());
            logger.error(eek.getMessage(),eek);
        }
        return null;
    }

    public Object patch(String url, Object data) {
        return post(url+"?_method=patch",data);
    }

    @Override
    public Object toResultBean(Object data, TypeReference typeRef) {
        return null;
    }


    private HttpHeaders buildHttpHeaders() {
//        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
//        HttpServletRequest request = requestAttributes.getRequest();
//        HttpSession session = request.getSession();
//        String tokenId = (String) session.getAttribute("登陆后的token");
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
        //todo 暂时性写死User-Agent，后续真实环境干掉
//        headers.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
//        headers.set("clientId", "服务器认证的key");
//        headers.set("clientTime", String.valueOf(System.currentTimeMillis()));
//        headers.set("token", tokenId);
//        headers.set("sign", "6379fa8e554585470aaad3a332deafd9");


        return headers;
    }

    public Map<?, ?> objectToMap(Object obj)  throws Exception {
        if(obj == null)
            return null;
        return new BeanMap(obj);
    }


    public static void main(String[] args) {
        RestClient RestClient = new HttpsRestClient();
        Object  returnObj = RestClient.get("https://ebssec.boc.cn/",null);
        System.out.println(JSON.toJSONString(returnObj));
    }


}
