package controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pojo.SystemDynamicValues;
import service.SystemDynamicValuesService;
import util.ToolGlobalParams;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/systemdynamicvalues")
public class SystemDynamicValuesController {
	@Autowired
	SystemDynamicValuesService systemDynamicValuesService;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月13日下午9:30:56
	 *
	 * 方法名：getManagerCustomerRelationDynamicValues
	 * 方法描述：得到访员与消费者的所有关系
	 */
	@RequestMapping(value = "/getManagerCustomerRelationDynamicValues", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getManagerCustomerRelationDynamicValues() {
		JSONObject responsejson = new JSONObject();

		SystemDynamicValues systemDynamicValues = systemDynamicValuesService.getSystemDynamicValuesByName(ToolGlobalParams.managercustomerrelation);
		JSONArray dynamicValues= JSONArray.fromObject(systemDynamicValues.getDynamicvalues());

		responsejson.put("code", 1);
		responsejson.put("result", dynamicValues);
		return responsejson;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月6日下午5:15:05
	 * 
	 * 方法名：getTagTypeDynamicValues
	 * 方法描述：得到所有标签类型
	 */
	@RequestMapping(value = "/getTagTypeDynamicValues", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getTagTypeDynamicValues() {
		JSONObject responsejson = new JSONObject();

		SystemDynamicValues systemDynamicValues = systemDynamicValuesService.getSystemDynamicValuesByName(ToolGlobalParams.tagshowtype);
		JSONArray dynamicValues= JSONArray.fromObject(systemDynamicValues.getDynamicvalues());

		responsejson.put("code", 1);
		responsejson.put("result", dynamicValues);
		return responsejson;
	}

}
