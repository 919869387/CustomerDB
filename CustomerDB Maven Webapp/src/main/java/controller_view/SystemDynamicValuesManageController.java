package controller_view;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import pojo.SystemDynamicValues;
import service.SystemDynamicValuesService;
import util.ToolGlobalParams;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class SystemDynamicValuesManageController {
	
	@Autowired
	SystemDynamicValuesService systemDynamicValuesService;
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月13日下午9:30:56
	 *
	 * 方法名：getManagerCustomerRelationDynamicValues
	 * 方法描述：得到所有访员与消费者的关系
	 */
	@RequestMapping(value = "/getTagShowTypeDynamicValues", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getTagShowTypeDynamicValues() {
		JSONObject responsejson = new JSONObject();

		SystemDynamicValues systemDynamicValues = systemDynamicValuesService.getSystemDynamicValuesByName(ToolGlobalParams.tagshowtype);
		JSONArray dynamicValues= JSONArray.fromObject(systemDynamicValues.getDynamicvalues());

		responsejson.put("code", 1);
		responsejson.put("result", dynamicValues);
		return responsejson;
	}
	
	@RequestMapping(value = "/toUpdateDynamicvaluesPage", method = RequestMethod.GET)
	public ModelAndView toUpdateDynamicvaluesPage(@RequestParam String id){
		
		int int_id = Integer.valueOf(id).intValue();
		
		SystemDynamicValues systemDynamicValues = systemDynamicValuesService.getSystemDynamicValuesById(int_id);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("id", systemDynamicValues.getId());
		modelAndView.addObject("name", systemDynamicValues.getName());
		modelAndView.addObject("dynamicvalues", systemDynamicValues.getDynamicvalues());
        modelAndView.setViewName("jsp/updateDynamicvalues");
        
		return modelAndView;
	}
	
	//得到系统所有动态值
	@RequestMapping(value = "/getAllSystemDynamicValues", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getAllSystemDynamicValues() {
		JSONObject responsejson = new JSONObject();

		List<SystemDynamicValues> allSystemDynamicValues = systemDynamicValuesService.getAllSystemDynamicValues();

		responsejson.put("code", 1);
		responsejson.put("result", allSystemDynamicValues);
		return responsejson;
	}
	
	//修改动态值
	@RequestMapping(value = "/updateDynamicvalues", method = POST)
	@ResponseBody
	public JSONObject updateDynamicvalues(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();
		
		int id = Integer.valueOf(map.get("id").toString()).intValue();
		JSONArray dynamicvalues = JSONArray.fromObject(map.get("dynamicvalues"));
		
		SystemDynamicValues systemDynamicValues = new SystemDynamicValues();
		systemDynamicValues.setId(id);
		systemDynamicValues.setDynamicvalues(dynamicvalues.toString());
		
		if(systemDynamicValuesService.updateDynamicvalues(systemDynamicValues)){
			responsejson.put("code", 1);
			responsejson.put("resultURL", "systemdynamicvalues.do");
		}else{
			responsejson.put("code", 0);
			responsejson.put("resultURL", "toUpdateDynamicvaluesPage.do?id="+id);
		}
		return responsejson;
	}

}
