package test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import service.TagTreeService;
import service.TagValueService;

public class TestTagValue {
	
	public TagValueService getService(){
		ApplicationContext context = new
				ClassPathXmlApplicationContext("spring.xml");
		TagValueService service = (TagValueService)context.getBean("tagValueService");
		
		return service;
	}
	
//	@Test
//	public void getTagValuesByTagid(){
//		System.out.println(getService().getTagValuesByTagid(17));
//	}
	
//	@Test
//	public void deleteTagValueByQid(){
//		System.out.println(getService().deleteTagValueByQid(111));
//	}
	
//	@Test
//	public void batchInsertTagValue() {
//		
//		int qid = 1;
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
//		relate2.put("option",value1);
//		relations.add(relate2);
//		
//		JSONObject relate3 = new JSONObject();
//		relate3.put("tagId", 16);
//		relations.add(relate3);
//		
//		JSONObject relate4 = new JSONObject();
//		relate3.put("tagId", 23);
//		relations.add(relate4);
//
//		System.out.println(getService().batchInsertTagValue(qid, relations));
//	}

}
