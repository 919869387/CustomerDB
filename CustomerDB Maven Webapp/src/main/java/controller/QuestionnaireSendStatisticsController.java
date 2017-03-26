package controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.QuestionnaireSendStatisticsService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/questionnairesendstatistics")
public class QuestionnaireSendStatisticsController {
	
	@Autowired
	QuestionnaireSendStatisticsService questionnaireSendStatisticsService;
	
	/*
	 * 通过customerid,更新totalsendnum
	 * "customerid":"xxxxxx"
	 */
	@RequestMapping(value = "/updatetotalsendnum", method = POST)
	@ResponseBody
	public JSONObject updateQuestionnaireSendStatistics_ADDtotalsendnum(@RequestBody Map<String, Object> map) {
		
		JSONObject responsejson = new JSONObject();

		String customerid = map.get("customerid").toString();

		boolean result = questionnaireSendStatisticsService.updateQuestionnaireSendStatistics_ADDtotalsendnum(customerid);
		
		responsejson.put("code", 1);
		responsejson.put("result", result);
		return responsejson;
		
	}
	
	/*
	 * 通过customerid,更新successsendnum
	 * "customerid":"xxxxxx"
	 */
	@RequestMapping(value = "/updatesuccesssendnum", method = POST)
	@ResponseBody
	public JSONObject updateQuestionnaireSendStatistics_ADDsuccesssendnum(@RequestBody Map<String, Object> map) {
		
		JSONObject responsejson = new JSONObject();

		String customerid = map.get("customerid").toString();

		boolean result = questionnaireSendStatisticsService.updateQuestionnaireSendStatistics_ADDsuccesssendnum(customerid);
		
		responsejson.put("code", 1);
		responsejson.put("result", result);
		return responsejson;
		
	}
	
	/*
	 * 通过customerid,更新answernum
	 * "customerid":"xxxxxx"
	 */
	@RequestMapping(value = "/updateanswernum", method = POST)
	@ResponseBody
	public JSONObject updateQuestionnaireSendStatistics_ADDanswernum(@RequestBody Map<String, Object> map) {
		
		JSONObject responsejson = new JSONObject();

		String customerid = map.get("customerid").toString();

		boolean result = questionnaireSendStatisticsService.updateQuestionnaireSendStatistics_ADDanswernum(customerid);
		
		responsejson.put("code", 1);
		responsejson.put("result", result);
		return responsejson;
		
	}

}
