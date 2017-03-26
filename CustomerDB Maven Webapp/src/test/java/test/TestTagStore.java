package test;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pojo.Tag;
import service.TagStoreService;

public class TestTagStore {
	
	public TagStoreService getService(){
		ApplicationContext context = new
				ClassPathXmlApplicationContext("spring.xml");
		TagStoreService service = (TagStoreService)
				context.getBean("tagStoreService");
		
		return service;
	}
	

	
//	@Test
//	public void batchUpdateBeused_timesSub(){
//		List<Tag> tagList = new ArrayList<>();
//		Tag tag = new Tag();
//		tag.setId(23);
//		tagList.add(tag);
//
//		System.out.println(getService().batchUpdateBeused_timesSub(tagList));
//	}
	
//	@Test
//	public void batchUpdateBeused_timesAdd(){
//		List<Tag> tagList = new ArrayList<>();
//		Tag tag = new Tag();
//		tag.setId(60);
//		tagList.add(tag);
//		
//		Tag tag1 = new Tag();
//		tag1.setId(57);
//		tagList.add(tag1);
//		System.out.println(getService().batchUpdateBeused_timesAdd(tagList));
//	}
//	
//	@Test
//	public void getParentTagTextTree(){
//		Tag parentTag = new Tag();
//		parentTag.setId(100);
//		parentTag.setCnname("测试");
//		parentTag.setType("parent");
//		parentTag.setSon_ids("[98]");
//		
//		JSONObject textTree = getService().getParentTagTypeTree(parentTag,"text");
//		System.out.println(textTree);
//	}
//
//	@Test
//	public void deleteTag(){
//		ApplicationContext context = new
//				ClassPathXmlApplicationContext("spring.xml");
//		TagStoreService service = (TagStoreService)
//				context.getBean("tagStoreService");
//
//		System.out.println(service.deleteTag(64));
//	}
//
//	@Test
//	public void getTag(){
//		ApplicationContext context = new
//				ClassPathXmlApplicationContext("spring.xml");
//		TagStoreService service = (TagStoreService)
//				context.getBean("tagStoreService");
//		Tag tag = new Tag();
//		tag.setId(51);
//		tag = service.getTag(tag);
//		System.out.println(tag.getCnname());
//	}
//
//
//	// @Test
//	// public void getTagByEnname(){
//	// ApplicationContext context = new
//	// ClassPathXmlApplicationContext("spring.xml");
//	// TagStoreService service = (TagStoreService)
//	// context.getBean("tagService");
//	// Tag tag = new Tag();
//	// tag.setEnname("222");
//	// tag = service.getTagByEnname(tag);
//	// System.out.println(tag.getTagId());
//	// }
//	//
//
//	@Test
//	public void insertTag(){
//		ApplicationContext context = new
//				ClassPathXmlApplicationContext("spring.xml");
//		TagStoreService service = (TagStoreService)
//				context.getBean("tagStoreService");
//		Tag tag = new Tag();
//		tag.setCnname("工作年限");
//		tag.setType("radio");
//		JSONArray values = new JSONArray();
//		values.add("1年及以下");
//		values.add("2-3年");
//		values.add("4-5年");
//		values.add("6-9年");
//		values.add("10年及以上");
//		System.out.println(service.insertTag(tag));
//	}
//
//	
//	@Test
//	public void findRootTag(){
//		ApplicationContext context = new
//				ClassPathXmlApplicationContext("spring.xml");
//		TagStoreService service = (TagStoreService)
//				context.getBean("tagStoreService");
//		int parent_id = 86;
//		Tag tag = service.getFinalRootTag(parent_id);
//		System.out.println(tag.getId());
//	}

}
