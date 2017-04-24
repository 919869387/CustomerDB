package test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import pojo.QuestionData;
import pojo_out.QuestionData_Out;
import service.QuestionDataService;
import service.TagStoreService;

public class TestQuestionData {
	
	
	public QuestionDataService getService(){
		ApplicationContext context = new
				ClassPathXmlApplicationContext("spring.xml");
		QuestionDataService service = (QuestionDataService)
				context.getBean("questionDataService");
		
		return service;
	}
	
//	@Test
//	public void updateQuestionDataIntegratedToFalse(){
//		getService().updateQuestionDataIntegratedToFalse("jinpai0001_393663a7-1740-412d-8e5c-9734c0b92382");
//	}
	
//	@Test
//	public void deleteQuestionDatasByQid() {
//		int qid = 111;
//		System.out.println(getService().deleteQuestionDatasByQid(qid));
//	}
	
//	@Test
//	public void getQuestionDatasByRequestParams(){
//		int qid = 111;
//		JSONObject requestParams = new JSONObject();
//		requestParams.put("51", "123");
//		JSONArray requestRemoveCustomerIds = new JSONArray();
//		//requestRemoveCustomerIds.add("ads");
//		System.out.println(getService().getQuestionDatasByRequestParams(qid, requestParams, requestRemoveCustomerIds));
//	}
	
//	@Test
//	public void getQuestionDataPageByRequestParams(){
//		int qid = 111;
//		JSONObject requestParams = new JSONObject();
//		requestParams.put("email_address", "456");
//		JSONArray requestRemoveCustomerIds = new JSONArray();
//		//requestRemoveCustomerIds.add("ads");
//		int eachPageRowNum = 10;
//		int startPosition = 0;
//		System.out.println(getService().getQuestionDataPageByRequestParams(qid, requestParams, requestRemoveCustomerIds, eachPageRowNum, startPosition));
//	}
	
//	@Test
//	public void getQuestionDataCountByRequestParams(){
//		int qid = 111;
//		JSONObject requestParams = new JSONObject();
//		requestParams.put("email_address", "123");
//		JSONArray requestRemoveCustomerIds = new JSONArray();
//		//requestRemoveCustomerIds.add("ads");
//		System.out.println(getService().getQuestionDataCountByRequestParams(qid, requestParams, requestRemoveCustomerIds));
//	}
	
	
//	@Test
//	public void insertQuestionData(){
//		QuestionData questionData = new QuestionData();
//		questionData.setCustomerid("adsaaa");
//		questionData.setQid(111222);
//		
//		
//		JSONObject data = new JSONObject();
//		JSONArray value = new JSONArray();
//		value.add("129998888888");
//		value.add("456");
//		data.put("email_address", value);
//		
//		questionData.setData(data.toString());
//		
//		System.out.println(getService().insertQuestionData(questionData));
//	}
	

//	
//	@Test
//	public void datetime() {
//		
//		
//		Date date = new Date(); 
//		Timestamp timestamp = new Timestamp(date.getTime());
//		System.out.println(timestamp.toString()); 
//
//	}

}
