package controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.DataInputService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/datainput")
public class DataInputController {

	@Autowired
	DataInputService dataInputService;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日下午9:28:19
	 *
	 * 方法名：deleteAllInfoByQid
	 * 方法描述：根据qid删除TagTree、TagValue、QuestionData、CustomerData所有相关的数据
	 */
	@RequestMapping(value = "/deleteAllInfoByQid", method = POST)
	@ResponseBody
	public JSONObject deleteAllInfoByQid(@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

		int qid = (int)map.get("qid"); //问卷id
		boolean result = dataInputService.deleteAllInfoByQid(qid);

		responsejson.put("code", 1);
		responsejson.put("result", result);

		return responsejson;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日下午8:54:56
	 *
	 * 方法名：InputQuertionDataAndCustomerData
	 * 方法描述： 将问卷数据导入quertiondata与customerdata
	 *
	 * 步骤：1.将data数据放入quertiondata
	 * 	   2.将data数据放入customerdata表中
	 *	   3.修改tagtree表中该问卷对应的RecordCount值
	 * 
	 * qid:int
	 * sampleData：[]
	 * 其中sampleData的格式 key：value  eg.[{"51": ["2万","5万"],"23": ["男"],"customerId":["xxxx"]},{"51": ["7万","9万"],"23": ["女"],"customerId":["xxxx"]}]
	 * 
	 */
	@RequestMapping(value = "/inputQuertionDataAndCustomerData", method = POST)
	@ResponseBody
	public JSONObject inputQuertionDataAndCustomerData(@RequestBody Map<String, Object> map) {
		JSONObject responsejson = new JSONObject();

		int qid = (int)map.get("qid"); //问卷id
		JSONArray sampleData = JSONArray.fromObject(map.get("sampleData"));
		int insertRecordCount = dataInputService.InputQuertionDataAndCustomerData(qid,sampleData);

		responsejson.put("code", 1);
		responsejson.put("result", insertRecordCount);

		return responsejson;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日下午7:18:20
	 *
	 * 方法名：InputQInfoAndRelations
	 * 方法描述：插入问卷id,问卷名,以及根据对应关系生成问卷的标签树(含标签值)
	 * 
	 * 步骤：1.删除所有信息消费者数据、标签树等信息
	 * 	   2.根据relations得到问卷的标签树，将qid、qname、标签树、时间戳 放入标签树表中
	 * 	   3.对tagstore中使用了的标签使用次数+1
	 *	   4.根据relations得到radio标签的值，将tagid、qid、标签值 放入标签值表中
	 *
	 *qid:int
	 *qname:string
	 *relations：[{tagId：int,option:["",""]},{tagId:int,option:[]}]
	 */
	@RequestMapping(value = "/inputQInfoAndRelations", method = POST)
	@ResponseBody
	public JSONObject inputQInfoAndRelations(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		int qid = Integer.valueOf(map.get("qid").toString()).intValue();
		String qname = map.get("qname").toString();
		JSONArray relations = JSONArray.fromObject(map.get("relations"));

		boolean result = dataInputService.InputQInfoAndRelations(qid, qname, relations);

		if(result){
			responsejson.put("code", 1);
			responsejson.put("result", result);
		}else{
			responsejson.put("code", 0);
			responsejson.put("result", "插入失败,已经存在qid="+qid+"的这条记录");
		}
		return responsejson;
	}

}
