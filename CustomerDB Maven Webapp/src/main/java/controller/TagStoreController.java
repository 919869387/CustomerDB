package controller;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.TagStoreService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/tagstore")
public class TagStoreController {

	@Autowired
	TagStoreService tagStoreService;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月22日下午5:13:08
	 *
	 * 方法名：getTagsForTagTransform
	 * 方法描述：得到所有可以用作标签对应的标签,将标签分为了text、radio两类
	 */
	@RequestMapping(value = "/getTagsForTagTransform", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getTagsForTagTransform() {
		
		JSONObject responsejson = new JSONObject();
		
		//这里将可以用来做对应的标签分成了：text radio两类
		JSONObject tagTrees  = tagStoreService.getTagsForTagTransform();
		
		responsejson.put("code",1);
		responsejson.put("result",tagTrees);
		return responsejson;
	}

}
