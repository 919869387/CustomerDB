package test;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pojo.TagTree;
import service.TagStoreService;
import service.TagTreeService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestTagTree {
	
	public TagTreeService getService(){
		ApplicationContext context = new
				ClassPathXmlApplicationContext("spring.xml");
		TagTreeService service = (TagTreeService)context.getBean("tagTreeService");
		
		return service;
	}
	
	@Test
	public void getTagTree(){
		System.out.println(getService().getQuestionTagTreeByQid(2022));
	}
	
//	@Test
//	public void getQnameAndQidCount(){
//		System.out.println(getService().getQnameAndQidCount());
//	}
	
//	@Test
//	public void getCustomerDataTagTrees(){
//		System.out.println(getService().getCustomerDataTagTrees());
//	}
	
//	@Test
//	public void getQuestionTagTreeByQid(){
//		int qid = 124;
//		System.out.println(getService().getQuestionTagTreeByQid(qid));
//	}
	
//	@Test
//	public void getQnameAndQidPage(){
//		int eachPageRowNum=2;
//		int startPosition=1;
//		System.out.println(getService().getQnameAndQidPage(eachPageRowNum, startPosition));
//	}
	
//	@Test
//	public void deleteTagTree(){
//		System.out.println(getService().deleteTagTreeByQid(111));
//	}
	
	
//	@Test
//	public void getTagListByQid() {
//		int qid =1234;
//		System.out.println(getService().getTagListByQid(qid).size());
//	}
	
//	
//	@Test
//	public void updateRecordCountByQid() {
//		int qid = 2;
//		int addRecordCount = 5; 
//		System.out.println(getService().updateRecordCountByQid(qid,addRecordCount));
//	}
//	
//	@Test
//	public void getInputSignByQid(){
//		int qid = 2;
//		System.out.println(getService().getInputSignByQid(qid));
//	}
//	
//	@Test
//	public void insertTagTree() {
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
//		int qid = 1;
//		String qname = "测试问卷-没有问卷数据20170312";
//		
//		Date date = new Date(); 
//		Timestamp recordtime = new Timestamp(date.getTime());
//		
//		System.out.println(getService().insertTagTree(qid, qname, relations, recordtime));
//		
//	}
//	
//	
//	@Test
//	public void getTreesByRelation() {
//		
//		JSONArray relations = new JSONArray();
//		JSONObject relate = new JSONObject();
//		relate.put("tagId", 16);
//		JSONArray value = new JSONArray();
//		value.add("甘肃");
//		value.add("新疆");
//		relate.put("option",value);
//		relations.add(relate);
//		
//		JSONObject relate1 = new JSONObject();
//		relate1.put("tagId", 17);
//		JSONArray value1 = new JSONArray();
//		value1.add("甘肃");
//		value1.add("新疆");
//		relate1.put("option",value1);
//		relations.add(relate1);
//		
//		JSONObject relate2 = new JSONObject();
//		relate2.put("tagId", 102);
//		JSONArray value2 = new JSONArray();
//		value2.add("甘肃");
//		value2.add("新疆");
//		relate2.put("option",value2);
//		relations.add(relate2);
//		
//		JSONObject relate3 = new JSONObject();
//		relate3.put("tagId", 60);
//		JSONArray value3 = new JSONArray();
//		value3.add("甘肃");
//		value3.add("新疆");
//		relate3.put("option",value3);
//		relations.add(relate3);
//		
//		JSONArray tagTrees = getService().getTreesByRelation(relations);
//		System.out.println(tagTrees);
//	}
	

}
