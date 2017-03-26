package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.TagValueDao;
import pojo.Tag;
import pojo.TagValue;

@Service
public class TagValueService {
	@Autowired
	TagValueDao tagValueDao;
	@Autowired
	TagStoreService tagStoreService;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月11日下午1:53:49
	 *
	 * 方法名：getTagValuesByTagid
	 * 方法描述：得到tagid标签的所有不重复的标签值
	 */
	public JSONArray getTagValuesByTagid(int tagid){
		JSONArray values = new JSONArray();
		List<TagValue> tagValues = tagValueDao.getTagValuesByTagid(tagid);
		for(int i=0;i<tagValues.size();i++){
			JSONArray value = JSONArray.fromObject(tagValues.get(i).getValue());
			for(int j=0;j<value.size();j++){
				if(!values.contains(value.get(j))){
					values.add(value.get(j));
				}
			}
		}
		return values;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日上午11:04:13
	 *
	 * 方法名：deleteTagValueByQid
	 * 方法描述：根据qid删除问卷有值标签的标签值
	 */
	public boolean deleteTagValueByQid(int qid) {
		int getCount = tagValueDao.getTagValueCountByQid(qid);
		int deleteCount = tagValueDao.deleteTagValueByQid(qid);
		if(getCount==deleteCount){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月28日下午5:02:37
	 *
	 * 方法名：insertTagValue
	 * 方法描述：将对应关系中有option标签值得标签筛选出来,将其批量插入表中
	 * 
	 * relations结构：[{tagId option},{tagId option}]
	 */
	public boolean batchInsertTagValue(int qid,JSONArray relations) {
		Map<Integer,JSONArray> tagValueMap = getTagValueByRelations(relations);
		if(tagValueMap.size()>0){
			List<TagValue> tagValueList = new ArrayList<>();
			for (Integer key : tagValueMap.keySet()) {
				TagValue tagValue = new TagValue();
				tagValue.setTagid(key);
				tagValue.setQid(qid);
				tagValue.setValue(tagValueMap.get(key).toString());
				tagValueList.add(tagValue);
			}
			int[] insertResult = tagValueDao.batchInsertTagValue(tagValueList);
			//遍历数组，如果数组中有《=0的值,说明批量操作失败
			for(int i=0;i<insertResult.length;i++){
				if(insertResult[i]<=0){
					return false;
				}
			}
		}
		return true;
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月28日下午5:14:04
	 *
	 * 方法名：getTagValueByRelations
	 * 方法描述：将对应关系中有option标签值得标签筛选出来
	 */
	public Map<Integer,JSONArray> getTagValueByRelations(JSONArray relations){
		Map<Integer,JSONArray> tagValue = new HashMap();
		for(int i=0;i<relations.size();i++){
			JSONObject relate = relations.getJSONObject(i);
			//得到对应关系里面的这个标签，判断是否是radio类型
			Tag tag = new Tag();
			tag.setId(relate.getInt("tagId"));
			tag = tagStoreService.getTag(tag);
			if(tag.getType().equals("radio") && relate.containsKey("option")){
				if(relate.getJSONArray("option").size()!=0){
					tagValue.put(relate.getInt("tagId"), relate.getJSONArray("option"));
				}
			}
		}
		return tagValue;
	}

}
