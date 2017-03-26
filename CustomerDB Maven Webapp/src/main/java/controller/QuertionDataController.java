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
import pojo.QuestionData;
import pojo_out.QuestionData_Out;
import service.CustomerDataService;
import service.QuestionDataService;
import service.ManageCustomerService;
import util.ToolPojoTransform;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/quertiondata")
public class QuertionDataController {

	@Autowired
	QuestionDataService questionDataService;
	@Autowired
	ManageCustomerService manageCustomerService;
	@Autowired
	CustomerDataService customerDataService;
	@Autowired
	ToolPojoTransform toolPojoTransform;


	/*
	 * 根据条件找到所有QuestionDatas,不分页
	 * "qid":143
	 * "requestParams":"{"51": "男", "23": "xxxx"}"
	 * "removeCustomerIds":[]
	 */
	@RequestMapping(value = "/getQuestionDatas", method = POST)
	@ResponseBody
	public JSONObject getQuestionDatas(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		int qid = (int)map.get("qid"); //活动id
		JSONObject requestContentParams = JSONObject.fromObject(map.get("requestParams"));//筛选条件
		JSONArray requestRemoveCustomerIds = JSONArray.fromObject(map.get("removeCustomerIds"));//要排除的消费者id

		List<QuestionData> datas = questionDataService.getQuestionDatasByRequestParams(qid, requestContentParams, requestRemoveCustomerIds);
		List<QuestionData_Out> dataOuts = new ArrayList<>();
		for(QuestionData data : datas){
			ManageCustomer manageCustomer = manageCustomerService.getManageCustomerByCustomerId(data.getCustomerid());
			Customer customer = customerDataService.getCustomerByCustomerid(data.getCustomerid());//根据customerid得到customerdata表中 姓名 和 电话 等信息
			QuestionData_Out dataOut = toolPojoTransform.QuestionDataToQuestionDataOut(customer,manageCustomer);
			dataOuts.add(dataOut);
		}

		responsejson.put("code", 1);
		responsejson.put("result", dataOuts);
		return responsejson;
	}


	/*
	 * 满足条件的记录总数
	 * "qid":143
	 * "requestParams":"{"51": "男", "23": "xxxx"}"
	 * "removeCustomerIds":[]
	 */
	@RequestMapping(value = "/getQuestionDataCount", method = POST)
	@ResponseBody
	public JSONObject getQuestionDataCount(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		int qid = (int)map.get("qid"); //活动id
		JSONObject requestParams = JSONObject.fromObject(map.get("requestParams"));//筛选条件
		JSONArray requestRemoveCustomerIds = JSONArray.fromObject(map.get("removeCustomerIds"));//要排除的消费者id

		int recordCount = questionDataService.getQuestionDataCountByRequestParams(qid, requestParams, requestRemoveCustomerIds);

		responsejson.put("code", 1);
		responsejson.put("result", recordCount);
		return responsejson;
	}

	/*
	 * 根据条件分页找到QuestionData
	 * "qid":143
	 * "requestParams":"{"24": "男", "12": "xxxx"}" ||  "requestParams":""
	 * "currentPageNum":1  --第几页
	 * "eachPageRowNum":10 --每页需要多少条记录
	 * "removeCustomerIds":[]
	 */
	@RequestMapping(value = "/getQuestionDataPageByRequestParams", method = POST)
	@ResponseBody
	public JSONObject getQuestionDataPageByRequestParams(@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

		int qid = (int)map.get("qid"); //活动id
		int currentPageNum = (int) map.get("currentPageNum");// 第几页
		int eachPageRowNum = (int) map.get("eachPageRowNum");// 每页需要多少条记录
		int startPosition = (currentPageNum - 1) * eachPageRowNum;//startPosition 取值为 0  10 20
		JSONObject requestParams = JSONObject.fromObject(map.get("requestParams"));//筛选条件
		JSONArray requestRemoveCustomerIds = JSONArray.fromObject(map.get("removeCustomerIds"));//要排除的消费者id

		List<QuestionData> datas = questionDataService.getQuestionDataPageByRequestParams(qid, requestParams,requestRemoveCustomerIds,eachPageRowNum, startPosition);

		List<QuestionData_Out> dataOuts = new ArrayList<>();
		for(QuestionData data : datas){
			ManageCustomer manageCustomer = manageCustomerService.getManageCustomerByCustomerId(data.getCustomerid());
			Customer customer = customerDataService.getCustomerByCustomerid(data.getCustomerid());//根据customerid得到customerdata表中 姓名 和 电话 等信息
			QuestionData_Out dataOut = toolPojoTransform.QuestionDataToQuestionDataOut(customer,manageCustomer);
			dataOuts.add(dataOut);
		}

		responsejson.put("code", 1);
		responsejson.put("result", dataOuts);
		return responsejson;
	}


}
