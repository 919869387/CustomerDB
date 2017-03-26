package test;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pojo.Customer;
import pojo.ManageCustomer;
import service.ManageCustomerService;

public class TestManageCustomer {
	
	public ManageCustomerService getService() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring.xml");
		ManageCustomerService service = (ManageCustomerService) context
				.getBean("manageCustomerService");
		return service;
	}
	
	@Test
	public void batchUpdateManageridAndManagername(){
		String managerid = "1233456";
		String managername = "ssss";
		JSONArray customerids = new JSONArray();
		customerids.add("jinpai0001_2e1644e5-d643-46f6-9eeb-3807cad157ff");
		customerids.add("jinpai0001_ab317e80-a470-4463-84b4-d34a509ce11d");
		customerids.add("jinpai0001_21cda45b-9220-435e-a0eb-65a7272c3175");
		
		System.out.println(getService().batchUpdateManageridAndManagername(managerid, managername, customerids));
	}
	
	
//	@Test
//	public void inputManagerCollectInfoToCustomerDB() {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"spring.xml");
//		ManageCustomerService service = (ManageCustomerService) context
//				.getBean("manageCustomerService");
//		
//		String managerid = "wangcui";
//		String managername = "王翠";
//		String managercustomerrelation = "业务";
//		//String customerInfo = "{\"name\": \"李广007\",\"telnumber\": \"13113111564\"}";
//
//		for(int i=0;i<10;i++){
//			JSONObject customerInfo = new JSONObject();
//			customerInfo.accumulate("name", TestTool.DynamicCreateChineseName());
//			customerInfo.accumulate("telnumber", TestTool.DynamicCreateTel());
//			//System.out.println(customerInfo.toString());
//			JSONObject result = service.inputManagerCollectInfoToCustomerDB(managerid, managername, managercustomerrelation,customerInfo);
//			System.out.println(result.get("resultCode"));
//		}
//		
//		
//	}
//
//	@Test
//	public void getCustomersByManagerid() {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"spring.xml");
//		ManageCustomerService service = (ManageCustomerService) context
//				.getBean("manageCustomerService");
//		
//		String managerid = "aa37-c74cc48050bd";
//		List<Customer> customers = service.getCustomersByManagerid(managerid);
//		for(Customer customer : customers){
//			System.out.println(customer.getContent());
//		}
//		
//	}
//	
//	@Test
//	public void getManageCustomerByCustomerId() {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"spring.xml");
//		ManageCustomerService service = (ManageCustomerService) context
//				.getBean("manageCustomerService");
//		
//		String customerid = "inpai0001_9fb0781b-7b2c-4ce7-be74-b5ab86063e8e";
//		ManageCustomer manageCustomer = service.getManageCustomerByCustomerId(customerid);
//		
//		System.out.println(manageCustomer.getManagerid());
//		
//	}
//	
//	@Test
//	public void updateCustomerBaesdInfoByManager() {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"spring.xml");
//		ManageCustomerService service = (ManageCustomerService) context
//				.getBean("manageCustomerService");
//		
//		String customerid = "jinpai0001_81431d6b-2871-4c36-bbbd-a0a4f5a5f695";
//		String customerInfo = "{\"name\": \"李广\",\"telnumber\": \"13113111564\"}";
//		
//		System.out.println(service.updateCustomerBaesdInfoByManager(customerid, customerInfo));
//		
//	}
}
