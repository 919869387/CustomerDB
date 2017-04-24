package test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pojo.Customer;
import pojo.QuestionData;
import service.CustomerDataService;
import service.QuestionDataService;
import util.ToolRequestParamsToSQLParams;

public class TestCustomerDataService {

	
	public CustomerDataService getService() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring.xml");
		CustomerDataService service = (CustomerDataService) context
				.getBean("customerDataService");
		return service;
	}
	
//	@Test
//	public void existCustomerByCustomerid(){
//		System.out.println(getService().existCustomerByCustomerid("jinpai0001_9322aeda-a102-4fde-9451-6f60286e1b54"));
//	}
	
//	@Test
//	public void testJSONArray(){
////		JSONArray customeridArr = new JSONArray();
////		System.out.println(customeridArr.size());
//		JSONObject customeridArr = new JSONObject();
//		System.out.println(customeridArr.get("123"));
//	}

//	@Test
//	public void daleteValueByCustomeridsAndRecordtime(){
//		List<String> customerids = new ArrayList<>();
//		customerids.add("jinpai0001_e475800e-d403-4b24-96df-f81f56c05728");
//		
//		String recordtime = "2017-03-06 09:21:16.531";
//		System.out.println(getService().daleteValueByCustomeridsAndRecordtime(customerids, recordtime));
//	}
	
//	@Test
//	public void getCustomersForOptionalParams() {
//
//		String necessaryParams = "{\"27\": \"男\"}";
//		//String necessaryParams = "{}";
//		//String optionalParams = "{\"x\": \"xx\",\"name\": \"游秀强\"}";
//		String optionalParams = "{}";
//		String removeCustomerIds = "[]";
//		//String removeCustomerIds = "['jinpai0001_3376c97c-b9f7-4943-aa37-c74cc48050bd','jinpai0001_50e91807-dc7c-4203-b833-7986b7815d84']";
//
//		JSONObject requestNecessaryParams = JSONObject.fromObject(necessaryParams);
//		JSONObject requestOptionalParams = JSONObject.fromObject(optionalParams);
//		JSONArray requestRemoveCustomerIds = JSONArray.fromObject(removeCustomerIds);
//		
//		List<Customer> customers = getService().getCustomersForOptionalParams(requestNecessaryParams, requestOptionalParams, requestRemoveCustomerIds,true);
//		for (Customer customer : customers) {
//			System.out.println(customer.getContent());
//		}
//		System.out.println(customers.size());
//	}
//
//	@Test
//	public void getCustomers() {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"spring.xml");
//		CustomerDataService service = (CustomerDataService) context
//				.getBean("customerDataService");
//
//		//String contentParams = "content->'name'?'杨潇' and content->'telnumber' ? '1111'";
//		String contentParams = "{\"x\":\"xx\"}";
//		//String contentParams = "{}";
//		JSONObject contentParams_json = JSONObject.fromObject(contentParams);
//		//String removeCustomerIds = "[]";
//		String removeCustomerIds = "['jinpai0001_3376c97c-b9f7-4943-aa37-c74cc48050bd','jinpai0001_50e91807-dc7c-4203-b833-7986b7815d84']";
//		JSONArray removeCustomerIds_json = JSONArray.fromObject(removeCustomerIds);
//
//		List<Customer> customers = service.getCustomers(contentParams_json,removeCustomerIds_json);
//		for (Customer customer : customers) {
//			System.out.println(customer.getContent());
//		}
//	}
//
//	@Test
//	public void testJSON() {
//
//		JSONObject requestNecessaryParams = new JSONObject();
//		JSONObject requestOptionalParams = new JSONObject();
//		requestOptionalParams.put("1", "A");
//
//		JSONObject requestTotalParams = new JSONObject();
//		requestTotalParams.putAll(requestNecessaryParams);
//		requestTotalParams.putAll(requestOptionalParams);
//
//		System.out.println(requestTotalParams);
//	}
//
//	@Test
//	public void getPageCustomerAfterValueSelectForOptionalParams() {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"spring.xml");
//		CustomerDataService service = (CustomerDataService) context
//				.getBean("customerDataService");
//
//		//String necessaryParams = "{\"sex\": \"男\"}";
//		String necessaryParams = "{}";
//		String optionalParams = "{\"x\": \"xx\",\"name\": \"游秀强\"}";
//		//String optionalParams = "{}";
//		String removeCustomerIds = "[]";
//		//String removeCustomerIds = "['jinpai0001_3376c97c-b9f7-4943-aa37-c74cc48050bd','jinpai0001_50e91807-dc7c-4203-b833-7986b7815d84']";
//
//		JSONObject requestNecessaryParams = JSONObject.fromObject(necessaryParams);
//		JSONObject requestOptionalParams = JSONObject.fromObject(optionalParams);
//		JSONArray requestRemoveCustomerIds = JSONArray.fromObject(removeCustomerIds);
//
//		int startPosition = 6;
//		int eachPageRowNum = 9;
//		List<Customer> customers = service.getPageCustomerForOptionalParams(requestNecessaryParams, requestOptionalParams, requestRemoveCustomerIds, eachPageRowNum, startPosition);
//		for (Customer customer : customers) {
//			System.out.println(customer.getContent());
//		}
//	}
//
//
//	@Test
//	public void createNewCustomerDataContent() {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"spring.xml");
//		CustomerDataService service = (CustomerDataService) context
//				.getBean("customerDataService");
//
//		String content = "{\"name\": \"游秀强\", \"telnumber\": \"0\", \"ansSheetId\": 43429}";
//		String datetime = "2016-10-22 13:11:27.419";
//		String newcontent = service.createNewCustomerDataContent(content, datetime);
//		System.out.println(newcontent);
//	}
//
//	@Test
//	public void updateCustomerDataContent() {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"spring.xml");
//		CustomerDataService service = (CustomerDataService) context
//				.getBean("customerDataService");
//
//		String oldContent = "{\"name\":[{\"value\":\"游秀强\",\"datetime\":\"2016-10-22 13:11:27.419\"}],\"telnumber\":[{\"value\":\"0\",\"datetime\":\"2016-10-22 13:11:27.419\"}],\"ansSheetId\":[{\"value\":\"43429\",\"datetime\":\"2016-10-22 13:11:27.419\"}]}";
//		String addContent = "{\"name\": \"游秀强\", \"telnumber\": \"0\", \"ansSheetId\": 43429, \"aaaa\": \"bbb\"}";
//		String datetime = "2016-10-22";
//
//		String newcontent = service.updateCustomerContent(oldContent, addContent, datetime);
//		System.out.println(newcontent);
//	}
//
////	@Test
////	public void inputDataToCustomer() {
////		ApplicationContext context = new ClassPathXmlApplicationContext(
////				"spring.xml");
////		CustomerDataService service = (CustomerDataService) context
////				.getBean("customerDataService");
////
////		String addContent = "{\"name\": \"游秀强11\", \"telnumber\": \"0\", \"ansSheetId\": 4342911, \"aaaa\": \"bbb\"}";
////
////		QuestionData data = new QuestionData();
////		data.setCustomerid("jinpai0001_81431d6b-2871-4c36-bbbd-a0a4f5a5f695");
////		data.setContent(addContent);
////		data.setRecord_datetime(new Timestamp(new Date().getTime()));
////
////		service.inputDataToCustomer(data);
////	}
//
//
//	@Test
//	public void getCustomerRecordCount() {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"spring.xml");
//		CustomerDataService service = (CustomerDataService) context
//				.getBean("customerDataService");
//		//'{"a":1, "b":2}'::jsonb @> '{"b":2}'::jsonb
//		//String contentParams = "content->>'sex' = '男'";
//		//String contentParams = "content::jsonb @> '{'value'='you'}'::jsonb";
//		//String contentParams = "content::json -> 'name' ->> 'value'::varchar = 'you'";
//		//String contentParams = "content::json -> 'name' ->> 'value' = 'you'";
//		//String contentParams = "content::jsonb ?& array['name']";
//		//String contentParams = "content -> 'name'::json ?& array['value']";
//		//String contentParams = "content::json #>> {'name','value'} = 'you'";
//		//*String contentParams = "content ?& array['name']";
//		//String value = "\"value\":\"游秀强\"";
//		//String contentParams = "content ~* '"+value+"'";
//
//		//String contentParams = "{'name':'杨潇','telnumber':'1111'}";
//		String contentParams = "{}";
//		JSONObject contentParams_json = JSONObject.fromObject(contentParams);
//		String removeCustomerIds = "[]";
//		//String removeCustomerIds = "['jinpai0001_3376c97c-b9f7-4943-aa37-c74cc48050bd','jinpai0001_50e91807-dc7c-4203-b833-7986b7815d84']";
//		JSONArray removeCustomerIds_json = JSONArray.fromObject(removeCustomerIds);
//		//String contentParams = "content->'name'?'游秀强' and content->'telnumber'?'0'";
//		int count = service.getCustomerRecordCount(contentParams_json,removeCustomerIds_json);
//		System.out.println(count);
//
//	}
//
//
//	@Test
//	public void getPageCustomer() {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"spring.xml");
//		CustomerDataService service = (CustomerDataService) context
//				.getBean("customerDataService");
//
//		//String contentParams = "content->'name'?'杨潇' and content->'telnumber' ? '1111'";
//		//String contentParams = "{'telnumber':'1111'}";
//		String contentParams = "{}";
//		JSONObject contentParams_json = JSONObject.fromObject(contentParams);
//		String removeCustomerIds = "[]";
//		//String removeCustomerIds = "['jinpai0001_3376c97c-b9f7-4943-aa37-c74cc48050bd','jinpai0001_50e91807-dc7c-4203-b833-7986b7815d84']";
//		JSONArray removeCustomerIds_json = JSONArray.fromObject(removeCustomerIds);
//
//		int eachPageRowNum = 10;
//		int startPosition = 0;
//		List<Customer> customers = service.getPageCustomer(contentParams_json,removeCustomerIds_json,eachPageRowNum, startPosition);
//		for (Customer customer : customers) {
//			System.out.println(customer.getContent());
//		}
//	}
//
//
//	@Test
//	public void stringToDate() throws ParseException {
//
//		String tagvalue = "2016-11-04 16:13:18";//初始化
//		String value = "2016-11-04 16:13:16.381";//初始化
//
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//
//		Date tagdate=format.parse(tagvalue);
//		Date date=format.parse(value); 
//
//		System.out.println(tagdate.compareTo(date));
//
//	}
//
//	@Test
//	public void sortJsonValue() {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"spring.xml");
//		CustomerDataService service = (CustomerDataService) context
//				.getBean("customerDataService");
//
//		String valuestr = "{\"you\":\"2016-11-03 19:50:17.847\",\"you1\":\"2016-11-07 19:50:17.847\",\"you2\":\"2016-11-04 19:51:17.847\"}";
//
//		System.out.println(service.getLastTimeValue(valuestr));
//	}



}
