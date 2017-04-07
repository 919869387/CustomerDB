package controller;

import java.util.Map;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pojo.Tag;
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
	 * 创建时间：2017年4月6日下午12:07:50
	 * 
	 * 方法名：deleteTag
	 * 方法描述:1.检查这个标签是否有儿子和被使用次数，没有儿子并且没有被使用才可以删除
	 * 		 2.将该标签的id,从父标签的son_ids中删除
	 *       3.删除该标签这条记录
	 */
	@RequestMapping(value = "/daleteTag", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject daleteTag(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		int id = Integer.valueOf(map.get("id").toString()).intValue();

		if(tagStoreService.deleteTag(id)){
			responsejson.put("code", 1);
			responsejson.put("result", "删除成功");
		}else{
			responsejson.put("code", 0);
			responsejson.put("result", "该标签存在儿子节点或者已经被使用,删除失败");
		}

		return responsejson;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月6日上午11:25:19
	 * 
	 * 方法名：getTagsForTagManage
	 * 方法描述：得到标签树（为标签管理显示标签使用）
	 */
	@RequestMapping(value = "/getTagsForTagManage", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getTagsForTagManage() {

		JSONObject responsejson = new JSONObject();

		//这里将可以用来做对应的标签分成了：text radio两类
		JSONObject tagTrees  = tagStoreService.getTagsForTagManage();

		JSONObject result = new JSONObject();
		result.put("textTrees", tagTrees.get("textTrees"));
		result.put("radioTrees", tagTrees.get("radioTrees"));
		result.put("beusedTagids", tagStoreService.getALLBeusedTagids());

		responsejson.put("code",1);
		responsejson.put("result",result);
		return responsejson;
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月5日下午9:45:26
	 * 
	 * 方法名：updateTagName
	 * 方法描述：修改标签名（只有当标签没有被使用的时候才可以修改标签）
	 * 
	 * 请求参数：id、cnname
	 */
	@RequestMapping(value = "/updateTagName", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateTagName(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		int id = Integer.valueOf(map.get("id").toString());

		Tag tag = new Tag();
		tag.setId(id);
		tag = tagStoreService.getTag(tag);
		if (tag.getBeused_times() != 0) {//说明已经被使用了，不能被修改
			responsejson.put("code", 0);
			responsejson.put("result", "标签不能被修改");
		} else {//说明没有使用了，能被修改
			tag.setCnname(map.get("cnname").toString());

			if (tagStoreService.updateTagName(tag)) {
				responsejson.put("code", 1);
				responsejson.put("result", "修改标签名成功");
			} else {
				responsejson.put("code", 0);
				responsejson.put("result", "修改标签名失败");
			}
		}
		return responsejson;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月6日下午4:18:34
	 * 
	 * 方法名：updateTagRelation
	 * 方法描述：修改标签父子关系
	 * 
	 * 请求参数：id、parent_id
	 */
	@RequestMapping(value = "/updateTagRelation", produces = "application/json; charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateTagRelation(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		int id = Integer.valueOf(map.get("id").toString());

		Tag tag = new Tag();
		tag.setId(id);
		tag = tagStoreService.getTag(tag);
		if (tag.getBeused_times() != 0) {//说明已经被使用了，不能被修改
			responsejson.put("code", 0);
			responsejson.put("result", "标签不能被修改");
		} else {//说明没有使用了，能被修改
			int newParent_id = Integer.valueOf(map.get("parent_id").toString());

			if(tagStoreService.updateTagRelation(tag, newParent_id)) {
				responsejson.put("code", 1);
				responsejson.put("result", "修改标签父子关系成功");
			}
		}
		return responsejson;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月22日下午5:13:08
	 *
	 * 方法名：getTagsForTagTransform
	 * 方法描述：得到所有可以用作标签对应的标签,将标签分为了text、radio两类
	 * 
	 * 这个方法也用在标签管理！！
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

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月5日下午8:54:48
	 * 
	 * 方法名：insertTag
	 * 方法描述：添加标签(同时维护父子关系)
	 * 
	 * 请求参数：cnname、type、parent_id
	 */
	@RequestMapping(value = "/insertTag", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject insertTag(@RequestBody Map<String, Object> map) {

		JSONObject responsejson = new JSONObject();

		Tag tag = new Tag();
		tag.setCnname(map.get("cnname").toString());
		tag.setType(map.get("type").toString());
		int parent_id = Integer.valueOf(map.get("parent_id").toString());
		tag.setParent_id(parent_id);

		int newTagid = tagStoreService.insertTag(tag);

		responsejson.put("code", 1);
		responsejson.put("result", newTagid);
		return responsejson;
	}

}
