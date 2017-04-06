package service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pojo.Tag;
import util.ExceptionTagStore;
import util.ToolGlobalParams;
import dao.TagStoreDao;

@Service
public class TagStoreService {

	@Autowired
	TagStoreDao dao;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月6日上午11:24:20
	 * 
	 * 方法名：getALLBeusedTagids
	 * 方法描述：得到所有被使用过的标签id
	 */
	public List getALLBeusedTagids() {
		List<Integer> beUsedTagids = new ArrayList<Integer>();
		List<Tag> beUsedTags = dao.getALLBeusedTagids();
		for(Tag tag:beUsedTags){
			beUsedTagids.add(tag.getId());
		}
		return beUsedTagids;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月11日下午1:25:24
	 *
	 * 方法名：getALLBeusedParentTags
	 * 方法描述：得到标签库里最上层的已经被使用了得标签
	 * 
	 * parent_id=0 and beused_times>0
	 */
	public List<Tag> getALLBeusedParentTags() {
		return dao.getALLBeusedParentTags();
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日下午9:47:51
	 *
	 * 方法名：batchUpdateBeused_timesAdd
	 * 方法描述：批量更新标签的使用次数加一
	 */
	public boolean batchUpdateBeused_timesAdd(List<Tag> tagList){
		int[] updateResult = dao.batchUpdateBeused_timesAdd(tagList);
		//遍历数组，如果数组中有《=0的值,说明批量操作失败
		for(int i=0;i<updateResult.length;i++){
			if(updateResult[i]<=0){
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月2日下午9:41:59
	 *
	 * 方法名：batchUpdateBeused_timesSub
	 * 方法描述：批量更新标签的使用次数减一
	 */
	public boolean batchUpdateBeused_timesSub(List<Tag> tagList){
		int[] updateResult = dao.batchUpdateBeused_timesSub(tagList);
		//遍历数组，如果数组中有《=0的值,说明批量操作失败
		for(int i=0;i<updateResult.length;i++){
			if(updateResult[i]<=0){
				return false;
			}
		}
		return true;
	}

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
	@Transactional
	public boolean deleteTag(int id) {
		Tag tag = new Tag();
		tag.setId(id);
		tag = dao.getTag(tag);

		JSONArray son_ids = JSONArray.fromObject(tag.getSon_ids());
		//1.如果这个标签没有儿子，没有被使用 才可以删除
		if(son_ids.size()==0 && tag.getBeused_times()==0){
			//2.将该标签的id,从父标签的son_ids中删除
			if(deleteIdOnParentTag(tag)){
				//3.删除该标签这条记录
				if(dao.deleteTag(id)){
					return true;
				}else{
					throw new ExceptionTagStore("删除标签,删除标签中文名等信息时出错");
				}
			}else{
				throw new ExceptionTagStore("删除标签,将标签的id,从父标签的son_ids中移除时出错");
			}
		}else{
			return false;
		}
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月22日下午4:58:43
	 *
	 * 递归找到标签树的最根标签
	 */
	public Tag getFinalRootTag(int parent_id){
		Tag parentTag = new Tag();
		parentTag.setId(parent_id);
		parentTag = dao.getTag(parentTag);
		if(parentTag.getParent_id()==0){
			return parentTag;
		}else{
			return getFinalRootTag(parentTag.getParent_id());
		}
	}

	/*
	 * 添加标签
	 *1.添加标签中名等信息
	 *2.将该标签的id,加入到父节点的son_ids中
	 */
	@Transactional
	public int insertTag(Tag tag) {
		//1.添加标签中英文名等信息
		int id = dao.insertTag(tag);
		if(id>0){
			tag.setId(id);
			//2.将该标签的id,加入父节点的son_ids中
			if(addIdToParentTag(tag,tag.getParent_id())){
				return id;
			}else{
				throw new ExceptionTagStore("添加标签,将标签的id,加入父标签的son_ids时出错");
			}
		}else{
			throw new ExceptionTagStore("添加标签,添加标签中英文名等信息时出错");
		}
	}

	/*
	 * 将该标签的id,加入父标签的son_ids中
	 * 
	 * 1.得到父标签
	 * 2.将id加入父标签的son_ids中
	 */
	public boolean addIdToParentTag(Tag tag,int newParent_id){
		if(newParent_id==0){
			return true;
		}
		//得到父标签
		Tag parentTag = new Tag();
		parentTag.setId(newParent_id);
		parentTag = dao.getTag(parentTag);
		if(parentTag.getType().equals(ToolGlobalParams.tagType_Parent)){
			//将id加入父标签的son_ids中
			JSONArray son_ids = JSONArray.fromObject(parentTag.getSon_ids());
			son_ids.add(tag.getId());
			parentTag.setSon_ids(son_ids.toString());

			return dao.updateTag(parentTag);
		}else{
			return false;
		}
	}
	/*
	 * 将该标签的id,从父标签的son_ids中移除
	 * 
	 * 1.得到父标签
	 * 2.将id从父标签的son_ids中移除
	 */
	public boolean deleteIdOnParentTag(Tag tag){
		if(tag.getParent_id()==0){
			return true;
		}
		//得到父标签
		Tag parentTag = new Tag();
		parentTag.setId(tag.getParent_id());
		parentTag = dao.getTag(parentTag);
		//将id从父标签的son_ids中移除
		JSONArray son_ids = JSONArray.fromObject(parentTag.getSon_ids());
		son_ids.remove(tag.getId());
		parentTag.setSon_ids(son_ids.toString());

		return dao.updateTag(parentTag);
	}

	public Tag getTag(Tag tag) {
		return dao.getTag(tag);
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月6日下午4:18:10
	 * 
	 * 方法名：updateTagName
	 * 方法描述：修改标签名
	 */
	public boolean updateTagName(Tag tag) {
		return dao.updateTag(tag);
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月6日下午4:20:09
	 * 
	 * 方法名：updateTagRelation
	 * 方法描述：修改标签的父子关系
	 * 		1.将该标签id从原来父标签的son_ids中删除
	 * 		2.将该标签id添加进,新父标签的son_ids中
	 * 		3.修改该标签的parent_id
	 */
	@Transactional
	public boolean updateTagRelation(Tag tag,int newParent_id) {
		if(tag.getParent_id()==newParent_id){
			//父节点没有变,不用修改
			return true;
		}else{
			if(deleteIdOnParentTag(tag)){
				if(addIdToParentTag(tag,newParent_id)){
					tag.setParent_id(newParent_id);
					if(dao.updateTag(tag)){
						return true;
					}else{
						throw new ExceptionTagStore("修改标签,修改该标签的parent_id时出错");
					}
				}else{
					throw new ExceptionTagStore("修改标签,将标签的id加入新父标签的son_ids时出错");
				}
			}else{
				throw new ExceptionTagStore("修改标签,将该标签id从原来父标签的son_ids中删除时出错");
			}
		}
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月24日上午11:28:30
	 *
	 * 方法名：getTagsForTagTransform
	 * 方法描述：得到所有可以用作标签对应的标签,将标签分为了text、radio两类
	 */
	public JSONObject getTagsForTagTransform(){
		//得到了所有最上层标签
		List<Tag> tags = dao.getALLSonTags();

		JSONArray textTrees = new JSONArray();
		JSONArray radioTrees = new JSONArray();

		//将不同类型的标签放入不同数组
		for(int i=0;i<tags.size();i++){
			Tag tag = tags.get(i);
			if(tag.getType().equals("text")){
				//如果是文本，添加到文本数组
				JSONObject node = new JSONObject();
				node.put("id", tag.getId());
				node.put("type", tag.getType());
				node.put("name", tag.getCnname());
				textTrees.add(node);
			}else if(tag.getType().equals("radio")){
				//如果是单选，添加到单选数组
				JSONObject node = new JSONObject();
				node.put("id", tag.getId());
				node.put("type", tag.getType());
				node.put("name", tag.getCnname());
				radioTrees.add(node);
			}else{
				//如果是父标签,并且父标签有子标签
				//这里一个树拆成text树和radio树
				JSONObject textTree = getParentTagTypeTree(tag,"text");
				JSONObject radioTree = getParentTagTypeTree(tag,"radio");
				if(textTree!=null){
					textTrees.add(textTree);
				}
				if(radioTree!=null){
					radioTrees.add(radioTree);
				}
			}
		}

		JSONObject tagTrees = new JSONObject();
		tagTrees.put("textTrees", textTrees);
		tagTrees.put("radioTrees", radioTrees);
		return tagTrees;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月24日上午9:30:34
	 *
	 * 方法名：getParentTagTypeTree
	 * 方法描述：得到树的所有Text节点结构,使用深度遍历
	 * 方法描述：得到树的所有Radio节点结构,使用深度遍历
	 * 
	 * 树的类型根据treeType决定
	 */
	public JSONObject getParentTagTypeTree(Tag parentTag,String treeType){
		JSONArray son_ids = JSONArray.fromObject(parentTag.getSon_ids());
		if(son_ids.size()==0){
			//如果这个父标签没有子标签,不做任何操作
			return null;
		}

		JSONArray children = new JSONArray();
		for(int i=0;i<son_ids.size();i++){
			int tagid = son_ids.getInt(i);
			Tag tag = new Tag();
			tag.setId(tagid);
			tag = dao.getTag(tag);
			if(tag.getType().equals(treeType)){
				JSONObject textTag = new JSONObject();
				textTag.put("id", tag.getId());
				textTag.put("type", tag.getType());
				textTag.put("name", tag.getCnname());
				children.add(textTag);
			}else if(tag.getType().equals("parent")){
				JSONObject textChildTree = getParentTagTypeTree(tag,treeType);
				if(textChildTree!=null){
					children.add(textChildTree);
				}
			}
		}

		if(children.size()==0){
			return null;
		}
		JSONObject typeTree = new JSONObject();
		typeTree.put("id", parentTag.getId());
		typeTree.put("type", parentTag.getType());
		typeTree.put("name", parentTag.getCnname());
		typeTree.put("children", children);
		return typeTree;
	}


}
