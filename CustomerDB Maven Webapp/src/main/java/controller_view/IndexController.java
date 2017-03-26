package controller_view;

import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import service.SystemUserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class IndexController {

	@Autowired 
	SystemUserService systemUserService;


	@RequestMapping(value = "index", method = RequestMethod.GET)
	public ModelAndView toLoginPage(){
		return new ModelAndView("jsp/login");
	}

	@RequestMapping(value = "login", method = RequestMethod.POST,consumes="application/json",produces="application/json")
	@ResponseBody
	public JSONObject doLogin(@RequestBody Map<String, Object> map){
		JSONObject responsejson = new JSONObject();

		String error = "index.do";
		String success = "menu.do";

		String username = (String) map.get("username");
		String password = (String) map.get("password");
		//System.out.print(username);

		boolean result = systemUserService.existSystemUser(username, password);

		if(result){
			responsejson.put("code", 1);
			responsejson.put("result", success);
		}else{
			responsejson.put("code", 0);
			responsejson.put("result", error);
		}

		return responsejson;
	}

}
