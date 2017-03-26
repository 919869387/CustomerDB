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
import pojo.QuestionnaireSendStatistics;
import pojo_out.Customer_ToManagerShow;
import service.CustomerDataService;
import service.ManageCustomerService;
import service.QuestionnaireSendStatisticsService;
import util.ToolGlobalParams;
import util.ToolPojoTransform;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/managecustomer")
public class ManageCustomerController {

	@Autowired
	ManageCustomerService manageCustomerService;
	@Autowired
	QuestionnaireSendStatisticsService questionnaireSendStatisticsService;
	@Autowired
	ToolPojoTransform toolPojoTransform;
	@Autowired
	CustomerDataService customerDataService;


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月13日下午8:57:05
	 *
	 * 方法名：moveCustomersToManager
	 * 方法描述：批量将消费者转移给其他访员
	 */
	@RequestMapping(value = "/moveCustomersToManager", method = POST)
	@ResponseBody
	public JSONObject moveCustomersToManager(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		String managerid = map.get("managerid").toString();
		String managername = map.get("managername").toString();
		JSONArray customerids = JSONArray.fromObject(map.get("customerids"));
		
		if(manageCustomerService.batchUpdateManageridAndManagername(managerid, managername, customerids)){
			JSONObject result = questionnaireSendStatisticsService.getCustomeridsTotalsendnumAndSuccesssendnumAndAnswernum(customerids);
			responsejson.put("code", 1);
			responsejson.put("result", result);
		}
		
		return responsejson;
	}
	
	/*
	 * 根据访员输入,批量添加消费者
	 * "managerid":"xxxxxx"
	 * "managername":"xxxxxxxxx"
	 * "customersInfo":[{"name": "xxxx", "telnumber": "xxxx","managercustomerrelation":"xxxxx"},{"name": "xxxx", "telnumber": "xxxx","managercustomerrelation":"xxxxx"}]
	 */
	@RequestMapping(value = "/inputManagerCollectInfoToCustomerDB", method = POST)
	@ResponseBody
	public JSONObject inputManagerCollectInfoToCustomerDB(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();
		//每个消费者的添加情况
		JSONArray result = new JSONArray();

		String managerid = map.get("managerid").toString();
		String managername = map.get("managername").toString();
		JSONArray customersInfo = JSONArray.fromObject(map.get("customersInfo"));
		if(customersInfo.size()>0){
			for(int i=0;i<customersInfo.size();i++){
				JSONObject singleCustomerInfo = JSONObject.fromObject(customersInfo.get(i));
				String managercustomerrelation = singleCustomerInfo.getString("managercustomerrelation");
				
				JSONObject customerNameTel = new JSONObject();
				customerNameTel.put(ToolGlobalParams.nameTagId, singleCustomerInfo.getString("name"));
				customerNameTel.put(ToolGlobalParams.telnumberTagId, singleCustomerInfo.getString("telnumber"));
				
				JSONObject addresult = manageCustomerService.inputManagerCollectInfoToCustomerDB(managerid, managername, managercustomerrelation, customerNameTel);
				result.add(addresult);
			}
			responsejson.put("code", 1);
			responsejson.put("result", result);
		}else{
			responsejson.put("code", 0);
			responsejson.put("result", "没有添加的消费者信息");
		}
		return responsejson;
	}


	/*
	 * 根据customerid得到消费者的姓名、电话、问卷接收信息
	 * "customerid":"xxxxx"
	 */
	@RequestMapping(value = "/getCustomerForManageByCustomerid", method = POST)
	@ResponseBody
	public JSONObject getCustomerForManageByCustomerid(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		String customerid = map.get("customerid").toString();
		Customer customer = customerDataService.getCustomerByCustomerid(customerid);
		QuestionnaireSendStatistics questionnaireSendStatistics = 
				questionnaireSendStatisticsService.getQuestionnaireSendStatisticsByCustomerId(customerid);
		Customer_ToManagerShow customer_ToManagerShow = toolPojoTransform.customerTocustomer_ToManagerShow(customer, questionnaireSendStatistics);

		responsejson.put("code", 1);
		responsejson.put("result", customer_ToManagerShow);
		return responsejson;

	}


	/*
	 * 通过managerid,找到访员管理的所有的消费者
	 * "managerid":"xxxxxx"
	 */
	@RequestMapping(value = "/getCustomersByManagerid", method = POST)
	@ResponseBody
	public JSONObject getCustomersByManagerid(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		String managerid = map.get("managerid").toString();

		List<Customer> customers = manageCustomerService.getCustomersByManagerid(managerid);
		List<Customer_ToManagerShow> customer_ToManagerShows = new ArrayList<>();
		for(Customer customer : customers){
			QuestionnaireSendStatistics questionnaireSendStatistics = 
					questionnaireSendStatisticsService.getQuestionnaireSendStatisticsByCustomerId(customer.getCustomerid());
			Customer_ToManagerShow customer_ToManagerShow = toolPojoTransform.customerTocustomer_ToManagerShow(customer, questionnaireSendStatistics);
			customer_ToManagerShows.add(customer_ToManagerShow);
		}

		responsejson.put("code", 1);
		responsejson.put("result", customer_ToManagerShows);
		return responsejson;

	}

	/*
	 * 通过customerid,找到管理该消费者的访员信息
	 * "customerid":"xxxxxx"
	 */
	@RequestMapping(value = "/getManagerInfoByCustomerId", method = POST)
	@ResponseBody
	public JSONObject getManagerInfoByCustomerId(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		String customerid = map.get("customerid").toString();

		ManageCustomer manageCustomer = manageCustomerService.getManageCustomerByCustomerId(customerid);
		if(manageCustomer.getManagerid() == null){
			responsejson.put("code", 0);
			responsejson.put("result", "此消费者没有被访员管理");
		}else{
			//如果存在值，那么只有一个
			responsejson.put("code", 1);
			responsejson.put("result", manageCustomer);
		}
		return responsejson;

	}

	/*
	 * 修改消费者信息,只有给这个消费者发送问卷之前才可以修改他的信息。
	 * 这时他的信息只有访员录入的姓名、电话信息
	 * 在QuestionnaireSendStatistics表中看是否有这个customerid,没有说明可以修改
	 
	 * "customerid":"xxxxxx"
	 * "customerInfo":"{"name": "xxxx", "telnumber": "xxxx","managercustomerrelation":"xxxxx"}"
	 */
	@RequestMapping(value = "/updateCustomerBaesdInfoByManager", method = POST)
	@ResponseBody
	public JSONObject updateCustomerBaesdInfoByManager(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		String customerid = map.get("customerid").toString();
		JSONObject customerInfo = JSONObject.fromObject(map.get("customerInfo"));
		
		JSONObject customerNameTel = new JSONObject();
		customerNameTel.put(ToolGlobalParams.nameTagId, customerInfo.getString("name"));
		customerNameTel.put(ToolGlobalParams.telnumberTagId, customerInfo.getString("telnumber"));
		
		if(!questionnaireSendStatisticsService.existQuestionnaireSendStatisticsByCustomerId(customerid)){
			//在QuestionnaireSendStatistics表中没有这个customerid,没有给这个消费者发送过问卷，可以修改姓名和电话
			if(manageCustomerService.updateCustomerBaesdInfoByManager(customerid, customerNameTel.toString())){
				//如果修改成功,返回消费者信息
				Customer customer = customerDataService.getCustomerByCustomerid(customerid);
				QuestionnaireSendStatistics questionnaireSendStatistics = 
						questionnaireSendStatisticsService.getQuestionnaireSendStatisticsByCustomerId(customerid);
				Customer_ToManagerShow customer_ToManagerShow = toolPojoTransform.customerTocustomer_ToManagerShow(customer, questionnaireSendStatistics);

				responsejson.put("code", 1);
				responsejson.put("result", customer_ToManagerShow);
			}else{
				responsejson.put("code", 0);
				responsejson.put("result", "可以更新消费者信息，但是更新失败");
			}

		}else{
			responsejson.put("code", 0);
			responsejson.put("result", "已经给此消费者发送过问卷,不能修改信息");
		}
		return responsejson;
	}

}
