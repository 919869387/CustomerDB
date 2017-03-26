package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pojo.QuestionData;
import pojo.TagValue;
import util.ToolRequestParamsToSQLParams;
import dao.QuestionDataDao;

@Service
public class QuestionDataService {

	@Autowired
	QuestionDataDao questionDataDao;
	@Autowired
	ToolRequestParamsToSQLParams toolRequestParamsToSQLParams;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日上午10:44:52
	 *
	 * 方法名：deleteQuestionDatasByQid
	 * 方法描述：根据qid删除QuestionData数据
	 */
	public boolean deleteQuestionDatasByQid(int qid) {
		int getCount = questionDataDao.getQuestionDataCountByQid(qid);
		int deleteCount = questionDataDao.deleteQuestionDatasByQid(qid);
		if(getCount==deleteCount){
			return true;
		}else{
			return false;
		}
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月7日下午8:46:43
	 *
	 * 方法名：getCustomeridsByQid
	 * 方法描述：根据qid得到对应的所有customerid
	 */
	public List<String> getCustomeridsByQid(int qid) {
		List<QuestionData> questionDatas = questionDataDao.getQuestionDatasByQid(qid);
		List<String> customerids = new ArrayList<>();
		for(int i=0;i<questionDatas.size();i++){
			customerids.add(questionDatas.get(i).getCustomerid());
		}
		return customerids;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日下午7:09:30
	 *
	 * 方法名：insertQuestionData
	 * 方法描述：导入一条记录
	 * 
	 * 步骤：要先检查是否存在,如果已经存在就更新操作，保证数据唯一性
	 */
	public boolean insertQuestionData(QuestionData questionData){
		if(questionDataDao.existQuestionDataByCustomeridAndQid(questionData)){
			return questionDataDao.updateQuestionData(questionData);
		}else{
			return questionDataDao.insertQuestionData(questionData);
		}
	}

	/*
	 * 对用户数据的不分页查询,一次性全部查处
	 */
	public List<QuestionData> getQuestionDatasByRequestParams(int qid,JSONObject requestParams,JSONArray requestRemoveCustomerIds) {

		List<QuestionData> datas = null;

		if(requestParams.size()!=0 && requestRemoveCustomerIds.size()!=0){
			//说明有请求参数,将筛选参数转化为SQL语句格式
			String sqlParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForQuestionData(requestParams);
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			datas = questionDataDao.getQuestionDatasByRequestParams(qid, sqlParams, removeCustomerIds_SQL);
		}else if(requestParams.size()==0 && requestRemoveCustomerIds.size()!=0){
			String sqlParams = "";
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			datas = questionDataDao.getQuestionDatasByRequestParams(qid, sqlParams, removeCustomerIds_SQL);
		}else if(requestParams.size()!=0 && requestRemoveCustomerIds.size()==0){
			String sqlParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForQuestionData(requestParams);
			String removeCustomerIds_SQL = "";
			datas = questionDataDao.getQuestionDatasByRequestParams(qid, sqlParams, removeCustomerIds_SQL);
		}else{
			String sqlParams = "";
			String removeCustomerIds_SQL = "";
			datas = questionDataDao.getQuestionDatasByRequestParams(qid, sqlParams, removeCustomerIds_SQL);
		}
		return datas;
	}

	/*
	 * 统计总数  对jsonb用户数据的模糊查询(有source_id)
	 */
	public int getQuestionDataCountByRequestParams(int qid,JSONObject requestParams,JSONArray requestRemoveCustomerIds) {

		int recordCount = 0;

		if(requestParams.size()!=0 && requestRemoveCustomerIds.size()!=0){
			//说明有请求参数,将筛选参数转化为SQL语句格式
			String sqlParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForQuestionData(requestParams);
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			recordCount = questionDataDao.getQuestionDataCountByRequestParams(qid,sqlParams,removeCustomerIds_SQL);
		}else if(requestParams.size()==0 && requestRemoveCustomerIds.size()!=0){
			String sqlParams = "";
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			recordCount = questionDataDao.getQuestionDataCountByRequestParams(qid, sqlParams,removeCustomerIds_SQL);
		}else if(requestParams.size()!=0 && requestRemoveCustomerIds.size()==0){
			String sqlParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForQuestionData(requestParams);
			String removeCustomerIds_SQL = "";
			recordCount = questionDataDao.getQuestionDataCountByRequestParams(qid,sqlParams,removeCustomerIds_SQL);
		}else{
			String sqlParams = "";
			String removeCustomerIds_SQL = "";
			recordCount = questionDataDao.getQuestionDataCountByRequestParams(qid,sqlParams,removeCustomerIds_SQL);
		}
		return recordCount;
	}

	/*
	 * 得到分页数据  对jsonb用户数据的模糊查询(有source_id)
	 * 对于跟踪调查，只需要根据条件筛选出分页数据即可，不需要数据归并（每个sourceid对应的数据中没有重复）
	 */
	public List<QuestionData> getQuestionDataPageByRequestParams(int qid,JSONObject requestParams,JSONArray requestRemoveCustomerIds,int eachPageRowNum, int startPosition) {
		List<QuestionData> datas = null;
		if(requestParams.size()!=0 && requestRemoveCustomerIds.size()!=0){
			//说明有请求参数,将筛选参数转化为SQL语句格式
			String sqlParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForQuestionData(requestParams);
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			datas = questionDataDao.getQuestionDataPageByRequestParams(qid, sqlParams,removeCustomerIds_SQL,eachPageRowNum,startPosition);
		}else if(requestParams.size()==0 && requestRemoveCustomerIds.size()!=0){
			String sqlParams = "";
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			datas = questionDataDao.getQuestionDataPageByRequestParams(qid, sqlParams,removeCustomerIds_SQL,eachPageRowNum,startPosition);
		}else if(requestParams.size()!=0 && requestRemoveCustomerIds.size()==0){
			String sqlParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForQuestionData(requestParams);
			String removeCustomerIds_SQL = "";
			datas = questionDataDao.getQuestionDataPageByRequestParams(qid, sqlParams,removeCustomerIds_SQL,eachPageRowNum,startPosition);
		}else{
			String sqlParams = "";
			String removeCustomerIds_SQL = "";
			datas = questionDataDao.getQuestionDataPageByRequestParams(qid, sqlParams,removeCustomerIds_SQL,eachPageRowNum,startPosition);
		}
		return datas;
	}


}
