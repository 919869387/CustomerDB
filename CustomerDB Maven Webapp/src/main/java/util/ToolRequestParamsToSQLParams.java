package util;

import java.util.Iterator;

import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository
public class ToolRequestParamsToSQLParams {
	
	/*
	 * 将removeCustomerIds的请求参数转化为SQL语句格式
	 */
	public String requestRemoveCustomerIdsToSQLRemoveCustomerIds(JSONArray requestRemoveCustomerIds){
		String sql_removeCustomerIds = "";
		int removeCustomerIdsSize = requestRemoveCustomerIds.size();
		for(int i=0;i<removeCustomerIdsSize;i++){
			sql_removeCustomerIds = sql_removeCustomerIds +"'" +requestRemoveCustomerIds.get(i) + "'" +",";
		}
		sql_removeCustomerIds = sql_removeCustomerIds.substring(0,sql_removeCustomerIds.length()-1);
		return sql_removeCustomerIds;
	}
	
	/*
	 * 将筛选参数转化为SQL语句格式
	 * 为查询CustomerData表准备
	 */
	public String requestParamsToSQLParamsForCustomerData(JSONObject requestContentParams){
		String sqlParams = "";
		Iterator it = requestContentParams.keys();  
		while (it.hasNext()) {
			String key = (String) it.next();  
			String value = requestContentParams.getString(key);
			sqlParams = sqlParams + "content->" +"'"+key+"'?'"+value+"' and ";
		}
		//String contentParams = "content->'name' ? '游秀强' and content->'telnumber' ? '0'";
		sqlParams = sqlParams.substring(0,sqlParams.length()-5);
		return sqlParams;
	}
	
	/*
	 * 将筛选参数转化为SQL语句格式
	 * 为查询QuestionData表准备
	 */
	public String requestParamsToSQLParamsForQuestionData(JSONObject requestContentParams){

		String sqlParams = "";
		Iterator it = requestContentParams.keys();  
		while (it.hasNext()) {
			String key = (String) it.next();  
			String value = requestContentParams.getString(key);
			sqlParams = sqlParams + "data->" +"'"+key+"'@>'\""+value+"\"' and ";
		}
		//data->'email_address' @> '\"123\"'
		sqlParams = sqlParams.substring(0,sqlParams.length()-5);
		return sqlParams;
	}

}
