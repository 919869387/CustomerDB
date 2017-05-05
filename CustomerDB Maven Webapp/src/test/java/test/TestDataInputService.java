package test;

import java.sql.Timestamp;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import service.DataInputService;
import util.ToolGlobalParams;

public class TestDataInputService {

	public DataInputService getService(){
		ApplicationContext context = new
				ClassPathXmlApplicationContext("spring.xml");
		DataInputService service = (DataInputService)context.getBean("dataInputService");

		return service;
	}
	
//	@Test
//	public void InputQuertionDataAndCustomerData(){
//		int qid = 1653;
//		int qid = 1675;
//		JSONArray sampleData_arr = new JSONArray();
//		JSONObject sampleData = new JSONObject();
//		
//		JSONArray valueTel = new JSONArray();
//		valueTel.add("123");
//		JSONArray valueCustomerID = new JSONArray();
//		valueCustomerID.add("jinpai0001_9c142138-bac9-4813-880e-e3076dd269b2");
//		JSONArray valueOther = new JSONArray();
//		valueOther.add("eee");
//
//		sampleData.put("106", valueOther);
//		sampleData.put(ToolGlobalParams.telnumberTagId, valueTel);
//		sampleData.put("customerId", valueCustomerID);
//		
//		sampleData_arr.add(sampleData);
//		
//		System.out.println(getService().InputQuertionDataAndCustomerData(qid, sampleData_arr));
//	}
	
//	@Test
//	public void deleteAllInfoByQid(){
//		System.out.println(getService().deleteAllInfoByQid(12));
//	}

//	@Test
//	public void InputQInfoAndRelations(){
//
//		JSONArray relations = new JSONArray();
//
//		JSONObject relate = new JSONObject();
//		relate.put("tagId", 60);
//		JSONArray value = new JSONArray();
//		value.add("一万");
//		value.add("两万");
//		value.add("一千万");
//		relate.put("option",value);
//		relations.add(relate);
//
//		JSONObject relate1 = new JSONObject();
//		relate1.put("tagId", 63);
//		JSONArray value1 = new JSONArray();
//		value1.add("半年");
//		value1.add("五年");
//		relate1.put("option",value1);
//		relations.add(relate1);
//
//		JSONObject relate2 = new JSONObject();
//		relate2.put("tagId", 59);
//		JSONArray value2 = new JSONArray();
//		value2.add("高中");
//		value2.add("大学");
//		relate2.put("option",value2);
//		relations.add(relate2);
//
//		JSONObject relate3 = new JSONObject();
//		relate3.put("tagId", 16);
//		relations.add(relate3);
//
//		JSONObject relate4 = new JSONObject();
//		relate4.put("tagId", 23);
//		relations.add(relate4);
//
//		int qid = 1;
//		String qname = "测试问卷-没有问卷数据20170312";
//
//		System.out.println(getService().InputQInfoAndRelations(qid, qname, relations));
//	}
}
