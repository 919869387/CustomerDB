package controller;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.TagTreeService;

@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域请求的注解
@Controller
@RequestMapping(value = "/tagtree")
public class TagTreeController {

	@Autowired
	TagTreeService tagTreeService;
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月11日下午2:51:18
	 *
	 * 方法名：getCustomerDataTagTrees
	 * 方法描述：得到全标签库的标签树(含标签值),为搜索CustomerData表
	 */
	@RequestMapping(value = "/getCustomerDataTagTrees", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getCustomerDataTagTrees() {
		
		JSONObject responsejson = new JSONObject();
		
		JSONArray customerDataTagTrees  = tagTreeService.getCustomerDataTagTrees();
		
		responsejson.put("code",1);
		responsejson.put("result",customerDataTagTrees);
		return responsejson;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月9日上午11:36:12
	 *
	 * 方法名：getQuestionTagTreeByQid
	 * 方法描述：根据qid得到问卷的标签树
	 */
	@RequestMapping(value = "/getQuestionTagTreeByQid", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getQuestionTagTreeByQid(@RequestBody Map<String, Object> map) {
		
		JSONObject responsejson = new JSONObject();
		
		int qid = (int)map.get("qid"); //问卷id
		
		JSONArray questionTagTree  = tagTreeService.getQuestionTagTreeByQid(qid);
		
		responsejson.put("code",1);
		responsejson.put("result",questionTagTree);
		return responsejson;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月9日上午11:12:19
	 *
	 * 方法名：getQnameAndQidPage
	 * 方法描述：分页得到问卷名与问卷id
	 */
	@RequestMapping(value = "/getQnameAndQidPage", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getQnameAndQidPage(@RequestBody Map<String, Object> map) {
		
		JSONObject responsejson = new JSONObject();
		
		int currentPageNum = (int) map.get("currentPageNum");// 第几页
		int eachPageRowNum = (int) map.get("eachPageRowNum");// 每页需要多少条记录
		int startPosition = (currentPageNum - 1) * eachPageRowNum;//startPosition 取值为 0  10 20
		
		JSONArray qnameAndqids  = tagTreeService.getQnameAndQidPage(eachPageRowNum, startPosition);
		
		responsejson.put("code",1);
		responsejson.put("result",qnameAndqids);
		return responsejson;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月11日下午3:47:20
	 *
	 * 方法名：getQnameAndQidCount
	 * 方法描述：得到有数据导入的问卷总数
	 */
	@RequestMapping(value = "/getQnameAndQidCount", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getQnameAndQidCount() {

		JSONObject responsejson = new JSONObject();

		int recordCount = tagTreeService.getQnameAndQidCount();

		responsejson.put("code", 1);
		responsejson.put("result", recordCount);
		return responsejson;
	}

}
