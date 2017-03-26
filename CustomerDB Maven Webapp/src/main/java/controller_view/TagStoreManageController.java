package controller_view;

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

import pojo.Tag;
import service.TagStoreService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class TagStoreManageController {

	@Autowired
	TagStoreService tagStoreService;

	
	@RequestMapping(value = "/getTag", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getTag(@RequestBody Map<String, Object> map) {
		
		JSONObject responsejson = new JSONObject();
		
		String tagid_str =  map.get("tagid").toString();
		int tagid = Integer.valueOf(tagid_str).intValue();
		
		Tag tag = new Tag();
		tag.setId(tagid);
		tag = tagStoreService.getTag(tag);

		responsejson.put("code", 1);
		responsejson.put("result", tag);
		return responsejson;
	}
	
	
	/*
	 * 得到所有标签
	 * 根据被使用次数降序排序
	 */
	@RequestMapping(value = "/getALLTags", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getALLTags() {

		JSONObject responsejson = new JSONObject();

		List<Tag> tags = tagStoreService.getALLTags();

		responsejson.put("code", 1);
		responsejson.put("result", tags);
		return responsejson;
	}

	/*
	 * 得到所有可以做父标签的标签
	 * type=parent
	 */
	@RequestMapping(value = "/getALLParentTags", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getALLParentTags() {

		JSONObject responsejson = new JSONObject();

		List<Tag> tags = tagStoreService.getALLParentTags();

		responsejson.put("code", 1);
		responsejson.put("result", tags);
		return responsejson;
	}

	/*
	 * 这里根据父标签，得到所有可以做子标签的标签
	 * 这里要根据所选的父节点来实时获取可用的儿子节点，避免形成环
	 * 1.选择parent=0的标签
	 * 2.排除父标签所在树的最上层标签
	 */
	@RequestMapping(value = "/getSonTagsByParent", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getSonTagsByParent(@RequestBody Map<String, Object> map) {
		
		JSONObject responsejson = new JSONObject();
		
		int parent_id = Integer.valueOf(map.get("parent_id").toString()).intValue();
		List<Tag> tags = tagStoreService.getSonTagsByParent(parent_id);

		responsejson.put("code", 1);
		responsejson.put("result", tags);
		return responsejson;
	}


	@RequestMapping(value = "toShowTagPage", method = RequestMethod.GET)
	public ModelAndView toShowTagPage(@RequestParam String id){

		int int_id = Integer.valueOf(id).intValue();
		Tag tag = new Tag();
		tag.setId(int_id);

		tag = tagStoreService.getTag(tag);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("id", tag.getId());
		modelAndView.addObject("cnname", tag.getCnname());
		modelAndView.addObject("type", tag.getType());
		modelAndView.addObject("beused_times", tag.getBeused_times());
		modelAndView.addObject("son_ids", tag.getSon_ids());
		modelAndView.addObject("parent_id", tag.getParent_id());
		modelAndView.setViewName("jsp/showTag");

		return modelAndView;
	}
	
	@RequestMapping(value = "toManageTagRelationPage", method = RequestMethod.GET)
	public ModelAndView toManageTagRelationPage(@RequestParam String id){

		int int_id = Integer.valueOf(id).intValue();
		Tag tag = new Tag();
		tag.setId(int_id);

		tag = tagStoreService.getTag(tag);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("id", tag.getId());
		modelAndView.addObject("cnname", tag.getCnname());
		modelAndView.addObject("type", tag.getType());
		modelAndView.addObject("beused_times", tag.getBeused_times());
		modelAndView.addObject("son_ids", tag.getSon_ids());
		modelAndView.addObject("parent_id", tag.getParent_id());
		modelAndView.setViewName("jsp/manageTagRelation");

		return modelAndView;
	}
	
	@RequestMapping(value = "toManageTagPage", method = RequestMethod.GET)
	public ModelAndView toManageTagPage(@RequestParam String id){

		int int_id = Integer.valueOf(id).intValue();
		Tag tag = new Tag();
		tag.setId(int_id);

		tag = tagStoreService.getTag(tag);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("id", tag.getId());
		modelAndView.addObject("cnname", tag.getCnname());
		modelAndView.addObject("type", tag.getType());
		modelAndView.addObject("beused_times", tag.getBeused_times());
		modelAndView.addObject("son_ids", tag.getSon_ids());
		modelAndView.addObject("parent_id", tag.getParent_id());
		modelAndView.setViewName("jsp/manageTag");

		return modelAndView;
	}

	/*
	 * 只有当标签没有被使用的时候才可以修改标签，也就是 beused_times=0时
	 */
	@RequestMapping(value = "/updateTag", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateTag(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		int id = Integer.valueOf(map.get("id").toString()).intValue();

		Tag tag = new Tag();
		tag.setId(id);
		tag = tagStoreService.getTag(tag);
		if (tag.getBeused_times() != 0) {// 说明已经被使用了，不能被修改
			responsejson.put("code", 0);
		} else {//说明没有使用了，能被修改
			tag.setCnname(map.get("cnname").toString());
			tag.setType(map.get("type").toString());

			if (tagStoreService.updateTag(tag)) {
				responsejson.put("code", 1);
			} else {
				responsejson.put("code", 0);
			}
		}
		responsejson.put("resultURL", "toManageTagValuePage.do?id="+id);
		return responsejson;
	}

	/*
	 * 只有当标签没有被使用的时候才可以修改标签，也就是 beused_times=0时
	 */
	@RequestMapping(value = "/daleteTag", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject daleteTag(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		int id = Integer.valueOf(map.get("id").toString()).intValue();

		if(tagStoreService.deleteTag(id)){
			responsejson.put("code", 1);
		}else{
			responsejson.put("code", 0);
		}
		responsejson.put("resultURL", "tagstore.do");
		return responsejson;
	}

	/*
	 * 转到添加标签页面
	 */
	@RequestMapping(value = "addTag", method = RequestMethod.GET)
	public ModelAndView toMenuPage(){
		return new ModelAndView("jsp/addTag");
	}


	/*
	 * 添加标签(同时将标签的父子关系也维护好了)
	 */
	@RequestMapping(value = "/insertTag", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject insertTag(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		Tag tag = new Tag();
		tag.setCnname(map.get("cnname").toString());
		tag.setType(map.get("type").toString());
		int parent_id = (int) map.get("parent_id");
		JSONArray son_ids = JSONArray.fromObject(map.get("son_ids"));
		tag.setParent_id(parent_id);
		tag.setSon_ids(son_ids.toString());

		int id = tagStoreService.insertTag(tag);
		if (id > 0) {
			responsejson.put("code", 1);
			responsejson.put("resultURL", "tagstore.do");
		} else {
			responsejson.put("code", 0);
			responsejson.put("result", false);
		}

		return responsejson;
	}

}
