package controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pojo.Customer;
import pojo.ManageCustomer;
import pojo_out.Customer_Out;
import service.CustomerDataService;
import service.ManageCustomerService;
import util.ToolPojoTransform;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/customerdata")
public class CustomerDataController {
	
	@Autowired
	CustomerDataService customerDataService;
	@Autowired
	ManageCustomerService manageCustomerService;
	@Autowired
	ToolPojoTransform toolPojoTransform;
	
	
	/*
	 * 得到所有符合条件的消费者,不分页, 有可选参数
	 * 
	 * "necessaryParams : {"22": "xxx", "44": "xxxx"}"
	 * "optionalParams : {"13": "男", "23": "xxxx"}"
	 * "removeCustomerIds":[]
	 * 
	 * "infointegratedSwitch":true//对信息完整性是否筛选的开关
	 */
	@RequestMapping(value = "/getCustomersForOptionalParams", method = POST)
	@ResponseBody
	public JSONObject getCustomersForOptionalParams(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		JSONObject requestNecessaryParams = JSONObject.fromObject(map.get("necessaryParams"));//筛选条件
		JSONObject requestOptionalParams = JSONObject.fromObject(map.get("optionalParams"));//筛选条件
		JSONArray requestRemoveCustomerIds = JSONArray.fromObject(map.get("removeCustomerIds"));//要排除的消费者id
		
		boolean infointegratedSwitch = false;//默认开关是关的,就是要筛选信息不完整的消费者
		if(map.containsKey("infointegratedSwitch")){
			infointegratedSwitch = (boolean) map.get("infointegratedSwitch");
		}
		
		List<Customer> customers = customerDataService.getCustomersForOptionalParams(requestNecessaryParams, requestOptionalParams, requestRemoveCustomerIds,infointegratedSwitch);
		List<Customer_Out> customer_Outs = new ArrayList<>();
		for(Customer customer : customers){
			ManageCustomer manageCustomer = manageCustomerService.getManageCustomerByCustomerId(customer.getCustomerid());
			Customer_Out customerOut = toolPojoTransform.CustomerToCustomerOut(customer,manageCustomer);
			customer_Outs.add(customerOut);
		}
		
		responsejson.put("code", 1);
		responsejson.put("result", customer_Outs);
		return responsejson;
	}
	
	
	/*
	 * 得到所有符合条件的消费者，不分页，没有可选参数
	 * 
	 * "contentParams":"{"33": "男", "12": "xxxx"}"
	 * "removeCustomerIds":[]
	 * 
	 * "infointegratedSwitch":true//对信息完整性是否筛选的开关
	 */
	@RequestMapping(value = "/getCustomers", method = POST)
	@ResponseBody
	public JSONObject getCustomers(@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

		JSONObject requestContentParams = JSONObject.fromObject(map.get("contentParams"));//筛选条件
		JSONArray requestRemoveCustomerIds = JSONArray.fromObject(map.get("removeCustomerIds"));//要排除的消费者id

		boolean infointegratedSwitch = false;//默认开关是关的,就是要筛选信息不完整的消费者
		if(map.containsKey("infointegratedSwitch")){
			infointegratedSwitch = (boolean) map.get("infointegratedSwitch");
		}
		
		List<Customer> customers = customerDataService.getCustomers(requestContentParams, requestRemoveCustomerIds,infointegratedSwitch);
		List<Customer_Out> customer_Outs = new ArrayList<>();
		for(Customer customer : customers){
			ManageCustomer manageCustomer = manageCustomerService.getManageCustomerByCustomerId(customer.getCustomerid());
			Customer_Out customerOut = toolPojoTransform.CustomerToCustomerOut(customer,manageCustomer);
			customer_Outs.add(customerOut);
		}
		
		responsejson.put("code", 1);
		responsejson.put("result", customer_Outs);
		return responsejson;
	}
	
	
	/*
	 * 分页查询数据，有可选参数的情况
	 * 当有可选参数存在时，使用此方法分页查询
	 * 只分两种情况,全条件查询与必要条件查询
	 * 
	 * 分页数据在前面的为筛选条件最多的
	 * 分页数据在后面的为筛选条件少的,数据不是很精确
	 * 
	 * "necessaryParams = {"43": "xxx", "23": "xxxx"}"
	 * "optionalParams = {"14": "男", "66": "xxxx"}"
	 * "removeCustomerIds":[]
	 * "currentPageNum":1
	 * "eachPageRowNum":10
	 * 
	 * "infointegratedSwitch":true//对信息完整性是否筛选的开关
	 */
	@RequestMapping(value = "/getPageCustomerForOptionalParams", method = POST)
	@ResponseBody
	public JSONObject getPageCustomerForOptionalParams(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		int currentPageNum = (int) map.get("currentPageNum");// 第几页
		int eachPageRowNum = (int) map.get("eachPageRowNum");// 每页需要多少条记录
		int startPosition = (currentPageNum - 1) * eachPageRowNum;//startPosition 取值为 0  10 20
		JSONObject requestNecessaryParams = JSONObject.fromObject(map.get("necessaryParams"));//筛选条件
		JSONObject requestOptionalParams = JSONObject.fromObject(map.get("optionalParams"));//筛选条件
		JSONArray requestRemoveCustomerIds = JSONArray.fromObject(map.get("removeCustomerIds"));//要排除的消费者id

		boolean infointegratedSwitch = false;//默认开关是关的,就是要筛选信息不完整的消费者
		if(map.containsKey("infointegratedSwitch")){
			infointegratedSwitch = (boolean) map.get("infointegratedSwitch");
		}
		
		List<Customer> customers = customerDataService.getPageCustomerForOptionalParams(requestNecessaryParams, requestOptionalParams,requestRemoveCustomerIds, eachPageRowNum, startPosition,infointegratedSwitch);
		List<Customer_Out> customer_Outs = new ArrayList<>();
		for(Customer customer : customers){
			ManageCustomer manageCustomer = manageCustomerService.getManageCustomerByCustomerId(customer.getCustomerid());
			Customer_Out customerOut = toolPojoTransform.CustomerToCustomerOut(customer,manageCustomer);
			customer_Outs.add(customerOut);
		}
		
		responsejson.put("code", 1);
		responsejson.put("result", customer_Outs);
		return responsejson;
	}
	
	
	/*
	 * 统计总数，有可选参数的情况
	 * 用于有可选参数的情况  对消费者的content字段查询
	 * 实质上,只需要用necessaryParams参数进行筛选就可以
	 * 
	 * "necessaryParams":"{"34": "xxx", "12": "xxxx"}"
	 * "optionalParams":"{"55": "男", "11": "xxxx"}"
	 * "removeCustomerIds":[]
	 * 
	 * "infointegratedSwitch":true//对信息完整性是否筛选的开关
	 */
	@RequestMapping(value = "/getCustomerRecordCountForOptionalParams", method = POST)
	@ResponseBody
	public JSONObject getCustomerRecordCountForOptionalParams(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();
		
		JSONObject requestContentNecessaryParams = JSONObject.fromObject(map.get("necessaryParams"));//筛选条件
		JSONArray requestRemoveCustomerIds = JSONArray.fromObject(map.get("removeCustomerIds"));//要排除的消费者id
		
		boolean infointegratedSwitch = false;//默认开关是关的,就是要筛选信息不完整的消费者
		if(map.containsKey("infointegratedSwitch")){
			infointegratedSwitch = (boolean) map.get("infointegratedSwitch");
		} 
		
		int recordCount = customerDataService.getCustomerRecordCount(requestContentNecessaryParams,requestRemoveCustomerIds,infointegratedSwitch);

		responsejson.put("code", 1);
		responsejson.put("result", recordCount);
		return responsejson;
	}
	
	
	/*
	 * 统计总数，没有可选参数
	 * 对消费者的content字段查询
	 * "contentParams":"{"45": "男", "12": "xxxx"}"
	 * "removeCustomerIds":[]
	 * 
	 * "infointegratedSwitch":true//对信息完整性是否筛选的开关
	 */
	@RequestMapping(value = "/getCustomerRecordCount", method = POST)
	@ResponseBody
	public JSONObject getCustomerRecordCount(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		//String contentParams = "content->'name' ? '游秀强' and content->'telnumber' ? '0'";
		JSONObject requestContentParams = JSONObject.fromObject(map.get("contentParams"));//筛选条件
		JSONArray requestRemoveCustomerIds = JSONArray.fromObject(map.get("removeCustomerIds"));//要排除的消费者id
		//System.out.println(contentParams);

		boolean infointegratedSwitch = false;//默认开关是关的,就是要筛选信息不完整的消费者
		if(map.containsKey("infointegratedSwitch")){
			infointegratedSwitch = (boolean) map.get("infointegratedSwitch");
		}
		
		int recordCount = customerDataService.getCustomerRecordCount(requestContentParams,requestRemoveCustomerIds,infointegratedSwitch);

		responsejson.put("code", 1);
		responsejson.put("result", recordCount);
		return responsejson;
	}
	
	/*
	 * 分页得到筛选消费者,没有可选参数  
	 * 对消费者的content字段查询
	 * "currentPageNum":1
	 * "eachPageRowNum":10
	 * "contentParams":"{"12": "男", "33": "xxxx"}"
	 * "removeCustomerIds":[]
	 * 
	 * "infointegratedSwitch":true//对信息完整性是否筛选的开关
	 */
	@RequestMapping(value = "/getPageCustomer", method = POST)
	@ResponseBody
	public JSONObject getPageCustomer(@RequestBody Map<String, Object> map) {
		
		JSONObject responsejson = new JSONObject();

		int currentPageNum = (int) map.get("currentPageNum");// 第几页
		int eachPageRowNum = (int) map.get("eachPageRowNum");// 每页需要多少条记录
		int startPosition = (currentPageNum - 1) * eachPageRowNum;//startPosition 取值为 0  10 20
		JSONObject requestContentParams = JSONObject.fromObject(map.get("contentParams"));//筛选条件
		JSONArray requestRemoveCustomerIds = JSONArray.fromObject(map.get("removeCustomerIds"));//要排除的消费者id
		
		boolean infointegratedSwitch = false;//默认开关是关的,就是要筛选信息不完整的消费者
		if(map.containsKey("infointegratedSwitch")){
			infointegratedSwitch = (boolean) map.get("infointegratedSwitch");
		}
		
		List<Customer> customers = customerDataService.getPageCustomer(requestContentParams,requestRemoveCustomerIds,eachPageRowNum, startPosition,infointegratedSwitch);
		List<Customer_Out> customer_Outs = new ArrayList<>();
		for(Customer customer : customers){
			ManageCustomer manageCustomer = manageCustomerService.getManageCustomerByCustomerId(customer.getCustomerid());
			Customer_Out customerOut = toolPojoTransform.CustomerToCustomerOut(customer,manageCustomer);
			customer_Outs.add(customerOut);
		}
		
		responsejson.put("code", 1);
		responsejson.put("result", customer_Outs);
		return responsejson;
	}
	
//	/*
//	 * 根据customerid得到消费者,这里得到的是消费者的全部信息
//	 * "customerid":"xxxxx"
//	 */
//	@RequestMapping(value = "/getCustomerByCustomerid", method = POST)
//	@ResponseBody
//	public JSONObject getCustomerByCustomerid(@RequestBody Map<String, Object> map) {
//
//		JSONObject responsejson = new JSONObject();
//
//		Customer customer = customerDataService.getCustomerByCustomerid(map.get("customerid").toString());
//
//		responsejson.put("code", 1);
//		responsejson.put("result", customer);
//		return responsejson;
//	}
}
