package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.restClient.RestClient;
import com.example.demo.common.restClient.domain.BaseResponse;
import com.example.demo.common.restClient.impl.HttpsRestClient;
import com.example.demo.common.restClient.impl.HttpsRestClientWithTemplate;
import org.json.JSONString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by guiqi on 2017/8/10.
 */

//@Disabled
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(locations = { "classpath*:applicationContext-test.xml" })
//@WebAppConfiguration
public class JFRestClientTest {

//    @Autowired
    private static RestClient restClient = new HttpsRestClient();

    private static RestClient restClient2 = new HttpsRestClientWithTemplate();



//    @Autowired
//    MockHttpSession session;

//    @BeforeEach
//    public void setUp(){
//        session.setAttribute("TokenId","VIANu_09zaV7RvBr6RwXgoKhjZWw22vuIC4yIYwJYvhycZqA3_HzXn56hKPuqvvNSAZIWrHQo7UYOTP309NDilSflcFVyDUPmv2I-GS-LEg");
//    }


    @Test
    @Disabled
    public void testPostForAddAppraise(){

//        AppraiseVO dto =  new AppraiseVO();
//        dto.setRecord(new EmpRecordVO(null,23,new Date(),12,12,12,"fdsfsdf", AppraiseStatus.CHECKER_VIEW,null,null,null));
//        List<EmpStageVO> stageParamList = new ArrayList<EmpStageVO>();
//        List<EmpBehaviourVO> behaviourParamList = new ArrayList<EmpBehaviourVO>();
//        for (int i=0;i<10;i++){
//            EmpStageVO stageParam = new EmpStageVO(null,null, TimeBlockType.ELEVEN_CLOCK,"plan","llllllkdfjd","llllllkdfjd","llllllkdfjd","llllllkdfjd","jfjfjddd");
//            EmpBehaviourVO behaviourParam = new EmpBehaviourVO(null,null, BehaviorType.COMMUNICATE,"fdsflkjjkkjk","llllllkdfjd","llllllkdfjd");
//            stageParamList.add(stageParam);
//            behaviourParamList.add(behaviourParam);
//        }
//
//        dto.setStageList(stageParamList);
//        dto.setBehaviourList(behaviourParamList);
//
//        BaseResponse response = (BaseResponse) restClient.post(CommonConstant.KEY_APPRAISE_ADD,dto);
//
//        Assert.isTrue(response.getCode().equals("200000"),"返回成功");

    }



    @Test
//    @Disabled
    public void testPostForModify(){

//        AppraiseVO dto =  new AppraiseVO();
//        dto.setRecord(new EmpRecordVO(1,23,new Date(),12,12,12,"fdsfsdf",AppraiseStatus.CHECKER_VIEW,null,null,null));
//        List<EmpStageVO> stageParamList = new ArrayList<EmpStageVO>();
//        List<EmpBehaviourVO> behaviourParamList = new ArrayList<EmpBehaviourVO>();
//        for (int i=1;i<11;i++){
//            EmpStageVO stageParam = new EmpStageVO(i,null, TimeBlockType.TEN_CLOCK,"plan"+i,"llllllkdfjd","llllllkdfjd","llllllkdfjd","llllllkdfjd","jfjfjddd");
//            EmpBehaviourVO behaviourParam = new EmpBehaviourVO(i,null,BehaviorType.DEDICATED,"behaviour"+i,"llllllkdfjd","llllllkdfjd");
//            stageParamList.add(stageParam);
//            behaviourParamList.add(behaviourParam);
//        }
//
//        dto.setStageList(stageParamList);
//        dto.setBehaviourList(behaviourParamList);
//
//        BaseResponse response = (BaseResponse) restClient.patch(CommonConstant.KEY_APPRAISE_MODIFY,dto);
//
//        Assert.isTrue(response.getCode().equals("200000"),"返回成功");

    }


    @Test
    public void testQuery(){

//        QueryRecord queryRecord = new QueryRecord();
//        queryRecord.setUserId("24");
//        BaseResponse response = (BaseResponse)restClient.get(CommonConstant.KEY_APPARISE_QUERY,queryRecord);
//        if (response.getCode().equals("200000")){
//            List<EmpRecordVO> list =(List<EmpRecordVO>) restClient.toResultBean(response.getData(),new TypeReference<ArrayList<EmpRecordVO>>(){});
//            Assert.notNull(list.get(0).getRecordId(),"record没查到");
//        }else
//            Assert.notNull(response.getMessage(),"response message not null");
//        System.out.println("jjjjjjjjjjjjj");
    }


    @Test
    public void testGuoMiQuery(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("farmId","4683");
        jsonObject.put("mobile","13641667601");
        jsonObject.put("type","ok");
        jsonObject.put("timestamp","1696243055923");

        Object  returnObj = this.restClient.post("https://snbt.nyncw.sh.gov.cn/seeyon/rest/snbt/whiteBlack/exists",jsonObject);
        System.out.println(JSON.toJSONString(returnObj));
    }

    @Test
    public void testGuoMiGuanWang(){
        Object  returnObj = restClient.get("https://ebssec.boc.cn/",null);
        System.out.println(JSON.toJSONString(returnObj));

    }

    @Test
    public void testGuoMiGuanWangWithTemplate(){
        Object  returnObj = restClient2.get("https://ebssec.boc.cn/",null);
        System.out.println(JSON.toJSONString(returnObj));

    }


    @Test
    public void testGuoMiZaxhWithTemplate(){
        Object  returnObj = restClient2.get("https://192.168.1.199",null);
        System.out.println(JSON.toJSONString(returnObj));

    }


    @Test
    public void testPostSnbt(){
        JSONObject data = new JSONObject();
        data.put("farmId","4683");
        data.put("mobile","13641667601");
        data.put("type","ok");
        data.put("timestamp","1696243055923");
        restClient2.post("https://snbt.nyncw.sh.gov.cn/seeyon/rest/snbt/whiteBlack/exists",data);
    }


    @Test
    public void testPostSnbt2(){
        String json = "{\n" +
                " \"farmId\": \"4683\",\n" +
                " \"mobile\": \"13641667601\",\n" +
                " \"type\": \"ok\",\n" +
                " \"timestamp\": \"1696243055923\"\n" +
                "}";
        restClient.post("https://snbt.nyncw.sh.gov.cn/seeyon/rest/snbt/whiteBlack/exists",json);
    }
}
