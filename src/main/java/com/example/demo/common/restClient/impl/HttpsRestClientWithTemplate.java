package com.example.demo.common.restClient.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.common.restClient.HttpsClientRequestFactory;
import com.example.demo.common.restClient.RestClient;
import com.example.demo.common.restClient.domain.BaseResponse;
import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by guiqi on 2017/8/10.
 */

public class HttpsRestClientWithTemplate implements RestClient {


    private Logger logger = LoggerFactory.getLogger(getClass());

//    @Resource
    private RestTemplate restTemplate;




    public Object post(String url, Object data) {
        return sendFor(url, data);
    }



    public Object get(String url, Object data) {
        try {
            supportAllProcol(url);
            HttpHeaders headers = buildHttpHeaders(MediaType.TEXT_HTML);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
            HttpEntity<Object> entity = new HttpEntity<Object>(headers);
            if (data != null){
                Map<String,String> map = (Map<String, String>) objectToMap(data);
                for (Map.Entry<String , String > setEntity : map.entrySet()){
                    builder.queryParam(setEntity.getKey(),setEntity.getValue());
                }
            }

            ResponseEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
            System.out.println("Result - status (" + response.getStatusCode() + ") has body: " + response.getBody());
            if (response.getStatusCode().value() == HttpStatus.OK.value()){
                return  JSONObject.parseObject(response.getBody()).toJavaObject(BaseResponse.class);
            }
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
            supportAllProcol(url);
            HttpHeaders headers = buildHttpHeaders(MediaType.APPLICATION_JSON);
            HttpEntity<HashMap> entity = null;
            if (data != null){
                HashMap<String, Object> paramMap = new HashMap<String,Object>();
                paramMap.put("data", data);
                entity = new HttpEntity<HashMap>(paramMap, headers);
            }
            ResponseEntity<BaseResponse> response = restTemplate.postForEntity(url,entity,BaseResponse.class);
            System.out.println("Result - status (" + response.getStatusCode() + ") has body: " + response.getBody());
            if (response.getStatusCode().value() == HttpStatus.OK.value()){
                return response.getBody();
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


    private HttpHeaders buildHttpHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(mediaType);
        headers.setAccept(Arrays.asList(mediaType));
        //todo 暂时性写死User-Agent，后续真实环境干掉
//        headers.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
//        headers.set("clientId", "服务器认证的key");
//        headers.set("clientTime", String.valueOf(System.currentTimeMillis()));
//        headers.set("token", tokenId);
//            headers.set("sign", "6379fa8e554585470aaad3a332deafd9");

        return headers;
    }

    public Map<?, ?> objectToMap(Object obj)  throws Exception {
        if(obj == null)
            return null;
        return new BeanMap(obj);
    }


    public void supportAllProcol(String url){
        if (url.contains("https:")){
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
            System.setProperty("sun.net.https.allowRestrictedHeaders", "true");
            this.restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        }
    }
}
