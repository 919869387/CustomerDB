package service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.TagTreeDao;
import pojo.Tag;
import pojo.TagTree;

@Service
public class TagTreeService {

	@Autowired
	TagStoreService tagStoreService;
	@Autowired
	TagTreeDao tagTreeDao;
	@Autowired
	TagValueService tagValueService;
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月11日下午3:38:52
	 *
	 * 方法名：getQnameAndQidCount
	 * 方法描述：得到有数据导入的问卷总数
	 */
	public int getQnameAndQidCount(){
		return tagTreeDao.getTagTreeCount();
	}
	
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月11日下午2:10:15
	 *
	 * 方法名：getCustomerDataTagTrees
	 * 方法描述：得到全标签库的标签树(含标签值)
	 * 
	 * 步骤：1.得到标签库里最上层的已经被使用了得标签
	 * 	   2.构造得到全库的标签树数组(添加叶子时，只有被使用了得叶子才会被添加)
	 */
	public JSONArray getCustomerDataTagTrees(){
		JSONArray tagTrees = new JSONArray();
		List<Tag> parentTags = tagStoreService.getALLBeusedParentTags();//得到标签库里最上层的已经被使用了得标签
		for(int i=0;i<parentTags.size();i++){
			Tag parentTag = parentTags.get(i);
			if(parentTag.getType().equals("parent")){
				JSONObject tagTree = getTreeByParentTag(parentTag);
				if(tagTree!=null){
					tagTrees.add(tagTree);
				}
			}else{
				JSONObject tagTree = new JSONObject();
				tagTree.put("id", parentTag.getId());
				tagTree.put("type", parentTag.getType());
				tagTree.put("name", parentTag.getCnname());
				if(parentTag.getType().equals("radio")){
					JSONArray value = tagValueService.getTagValuesByTagid(parentTag.getId());
					tagTree.put("value", value);
				}
				tagTrees.add(tagTree);
			}
		}
		return tagTrees;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月11日下午2:14:37
	 *
	 * 方法名：getTreeByParentTag
	 * 方法描述：得到类型是parent标签的标签树(含标签值)
	 */
	public JSONObject getTreeByParentTag(Tag parentTag){
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
			tag = tagStoreService.getTag(tag);
			if(!tag.getType().equals("parent")){//如果这个子标签的id在对应关系中，就添加进标签树
				if(tag.getBeused_times()>0){//只有是被使用过的叶子才会被添加
					JSONObject node = new JSONObject();
					node.put("id", tag.getId());
					node.put("type", tag.getType());
					node.put("name", tag.getCnname());
					if(tag.getType().equals("radio")){
						JSONArray value = tagValueService.getTagValuesByTagid(tag.getId());
						node.put("value", value);
					}
					children.add(node);
				}
			}else{
				JSONObject childTree = getTreeByParentTag(tag);
				if(childTree!=null){
					children.add(childTree);
				}
			}
		}

		if(children.size()==0){
			return null;
		}
		JSONObject tagTree = new JSONObject();
		tagTree.put("id", parentTag.getId());
		tagTree.put("type", parentTag.getType());
		tagTree.put("name", parentTag.getCnname());
		tagTree.put("children", children);
		return tagTree;
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月9日上午11:34:17
	 *
	 * 方法名：getQuestionTagTreeByQid
	 * 方法描述：根据qid得到问卷的标签树
	 */
	public JSONArray getQuestionTagTreeByQid(int qid){
		return JSONArray.fromObject(getTagTree(qid).getTree());
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月9日上午11:11:48
	 *
	 * 方法名：getQnameAndQidPage
	 * 方法描述：分页得到问卷名与问卷id
	 */
	public JSONArray getQnameAndQidPage(int eachPageRowNum, int startPosition){
		JSONArray qnameAndqids = new JSONArray();
		List<TagTree> tagTrees = tagTreeDao.getTagTreePage(eachPageRowNum, startPosition);
		for(int i=0;i<tagTrees.size();i++){
			TagTree tagtree = tagTrees.get(i);
			JSONObject qnameAndqid = new JSONObject();
			qnameAndqid.put("qname", tagtree.getQname());
			qnameAndqid.put("qid", tagtree.getQid());
			qnameAndqids.add(qnameAndqid);
		}
		return qnameAndqids;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日下午4:28:08
	 *
	 * 方法名：existQid
	 * 方法描述：根据qid查看这条记录是否存在
	 */
	public boolean existQid(int qid){
		return tagTreeDao.existQid(qid);
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日上午11:48:17
	 *
	 * 方法名：deleteTagTreeByQid
	 * 方法描述：根据qid删除对应的记录
	 */
	public boolean deleteTagTreeByQid(int qid) {
		int getCount = tagTreeDao.getTagTreeCountByQid(qid);
		int deleteCount = tagTreeDao.deleteTagTreeByQid(qid);
		if(getCount==deleteCount){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月7日下午8:37:25
	 *
	 * 方法名：getTagTree
	 * 方法描述：根据qid得到tagtree表里面的一条记录
	 */
	public TagTree getTagTree(int qid) {
		TagTree tagtree = new TagTree();
		tagtree.setQid(qid);
		tagtree = tagTreeDao.getTagTree(tagtree);
		return tagtree;
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月2日下午8:49:40
	 *
	 * 方法名：getTagListByQid
	 * 方法描述：根据qid得到tagtrees，
	 * 		     再根据tagtrees得到树中的所有节点
	 */
	public List<Tag> getTagListByQid(int qid){
		TagTree tagTree = getTagTree(qid);
		JSONArray tagTrees = JSONArray.fromObject(tagTree.getTree());

		List<Tag> tagList = new ArrayList();
		tagList = getTagListByTagTrees(tagList, tagTrees);
		return tagList;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月2日下午8:34:00
	 *
	 * 方法名：getTagListByTagTrees
	 * 方法描述：根据tagtrees,得到所有节点
	 */
	public List<Tag> getTagListByTagTrees(List<Tag> tagList,JSONArray tagTrees){
		for(int i=0;i<tagTrees.size();i++){
			JSONObject tree = tagTrees.getJSONObject(i);
			int tagid = tree.getInt("id");
			Tag tag = new Tag();
			tag.setId(tagid);
			tagList.add(tag);
			if(tree.containsKey("children")){
				getTagListByTagTrees(tagList,tree.getJSONArray("children"));
			}
		}
		return tagList;
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日下午8:50:18
	 *
	 * 方法名：updateRecordCountByQid
	 * 方法描述：根据qid修改RecordCount
	 */
	public boolean updateRecordCountByQid(int qid,int addRecordCount) {
		return tagTreeDao.updateRecordCountByQid(qid,addRecordCount);
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日上午11:16:11
	 *
	 * 方法名：getInputSignByQid
	 * 方法描述：根据问卷id找到该问卷的导入进度标识
	 */
	public int getInputSignByQid(int qid) {
		int inputSign = 0;
		if(tagTreeDao.existQid(qid)){
			inputSign =  tagTreeDao.getInputSignByQid(qid);
		}
		return inputSign;
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月27日上午11:45:58
	 *
	 * 方法名：insertTagTree
	 * 方法描述：生成问卷标签树，并插入表中
	 * 
	 * 这里同时插入了：qid,qname,tagTrees,recordtime
	 */
	public boolean insertTagTree(int qid,String qname,JSONArray relations,Timestamp recordtime) {
		TagTree tagTree = new TagTree();
		tagTree.setQid(qid);
		tagTree.setQname(qname);
		JSONArray tagTrees = getTreesByRelation(relations);
		tagTree.setTree(tagTrees.toString());
		tagTree.setRecordtime(recordtime);

		return tagTreeDao.insertTagTree(tagTree);
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月26日下午9:42:56
	 *
	 * 方法名：getTreesByRelation
	 * 方法描述：根据问卷标签对应关系得到标签树
	 * 步骤：1.将对应关系中相同根父标签的标签归类Map<parentid,[sonids]>
	 * 	   2.遍历Map,据根标签以及子标签，构造得到标签树
	 */
	public JSONArray getTreesByRelation(JSONArray relations){
		JSONArray tagTrees = new JSONArray();
		//把问卷标签对应关系数组中的标签,根据父节点相同归纳
		Map<Integer,JSONArray> parentSonMap = getParentSonMapByRelations(relations);
		//遍历Map,据根标签以及子标签，构造得到标签树
		for (Integer key : parentSonMap.keySet()) {  
			if(key==0){
				//没有父节点的标签
				JSONArray ids = parentSonMap.get(key);
				for(int i=0;i<ids.size();i++){
					Tag tag = new Tag();
					tag.setId(ids.getInt(i));
					tag = tagStoreService.getTag(tag);
					JSONObject tree = new JSONObject();
					tree.put("id", tag.getId());
					tree.put("name", tag.getCnname());
					tree.put("type", tag.getType());
					if(tag.getType().equals("radio")){
						//如果是radio类型,从relations中得到标签的value
						JSONArray value = getTagValueFromRelations(relations, tag.getId());
						if(value!=null && value.size()!=0){
							tree.put("value", value);
						}
					}
					tagTrees.add(tree);
				}
			}else{
				//有父节点的标签
				JSONArray parentSonIds = parentSonMap.get(key);
				Tag parentTag = new Tag();
				parentTag.setId(key);
				parentTag = tagStoreService.getTag(parentTag);
				JSONObject tree = getTagTreeByParentSonTag(parentTag, parentSonIds, relations);
				if(tree!=null){
					tagTrees.add(tree);
				}
			}
		} 
		return tagTrees;
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月25日下午10:51:10
	 *
	 * 方法名：relationsToParentSonMap
	 * 方法描述：把问卷标签对应关系数组中的标签，转化成Map<Integer,JSONArray>
	 * 		  Integer为共同的最根父节点，JSONArray为有相同父节点的子节点数组
	 */
	public Map<Integer,JSONArray> getParentSonMapByRelations(JSONArray relations){
		//这个map,key为最根结点 value为最末端子节点
		Map<Integer,JSONArray> parentSonMap=new HashMap<Integer,JSONArray>();   
		//遍历每个对应关系
		for(int i=0;i<relations.size();i++){
			JSONObject relate = JSONObject.fromObject(relations.get(i));
			int sonId = relate.getInt("tagId");
			Tag sonTag = new Tag();
			sonTag.setId(sonId);
			sonTag = tagStoreService.getTag(sonTag);
			if(sonTag.getParent_id()==0){
				//自己就是最根结点，它们的key都为0
				if(parentSonMap.containsKey(0)){
					JSONArray sonIds = parentSonMap.get(0);
					sonIds.add(sonTag.getId());
					parentSonMap.put(0, sonIds);
				}else{
					JSONArray sonIds = new JSONArray();
					sonIds.add(sonTag.getId());
					parentSonMap.put(0, sonIds);
				}
			}else{
				//如果该标签有最根结点
				Tag finalRootTag = tagStoreService.getFinalRootTag(sonTag.getParent_id());//找到它的最根结点
				int finalRootTagId = finalRootTag.getId();
				if(parentSonMap.containsKey(finalRootTagId)){
					JSONArray sonIds = parentSonMap.get(finalRootTagId);
					sonIds.add(sonTag.getId());
					parentSonMap.put(finalRootTagId, sonIds);
				}else{
					JSONArray sonIds = new JSONArray();
					sonIds.add(sonTag.getId());
					parentSonMap.put(finalRootTagId, sonIds);
				}
			}

		}
		return parentSonMap;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月26日下午8:20:54
	 *
	 * 方法名：getTagValueFromRelations
	 * 方法描述：在对应关系中，根据tagId找到标签的值。标签对应里面，同一个标签只能使用一次。
	 */
	public JSONArray getTagValueFromRelations(JSONArray relations,int tagId){
		JSONArray value = null;
		for(int i=0;i<relations.size();i++){
			JSONObject relation = relations.getJSONObject(i);
			if(relation.getInt("tagId")==tagId && relation.containsKey("option")){
				value = relation.getJSONArray("option");
				break;
			}
		}
		return value;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月26日下午9:28:35
	 *
	 * 方法名：getTagTreeByParentSonTag
	 * 方法描述：据根标签以及子标签，构造得到标签树
	 * 
	 * parentTag：根标签
	 * parentSonIds:这个标签下面的叶子标签id
	 * relations：问卷标签对应关系,用来得到子标签的值
	 */
	public JSONObject getTagTreeByParentSonTag(Tag parentTag,JSONArray parentSonIds,JSONArray relations){
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
			tag = tagStoreService.getTag(tag);
			if(parentSonIds.contains(tag.getId())){//如果这个子标签的id在对应关系中，就添加进标签树
				JSONObject node = new JSONObject();
				node.put("id", tag.getId());
				node.put("type", tag.getType());
				node.put("name", tag.getCnname());
				if(tag.getType().equals("radio")){
					JSONArray value = getTagValueFromRelations(relations,tag.getId());
					node.put("value", value);
				}
				children.add(node);
			}else if(tag.getType().equals("parent")){
				JSONObject childTree = getTagTreeByParentSonTag(tag,parentSonIds,relations);
				if(childTree!=null){
					children.add(childTree);
				}
			}
		}

		if(children.size()==0){
			return null;
		}
		JSONObject tagTree = new JSONObject();
		tagTree.put("id", parentTag.getId());
		tagTree.put("type", parentTag.getType());
		tagTree.put("name", parentTag.getCnname());
		tagTree.put("children", children);
		return tagTree;
	}

}
