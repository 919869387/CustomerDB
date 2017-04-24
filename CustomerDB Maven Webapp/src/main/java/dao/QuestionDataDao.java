package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import pojo.QuestionData;

@Repository
public class QuestionDataDao {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月24日下午1:11:13
	 * 
	 * 方法名：updateQuestionDataIntegratedToFalse
	 * 方法描述：修改uestionData的Integrated为false
	 */
	public boolean updateQuestionDataIntegratedToFalse(String customerid) {
		String sql = "update questiondata set integrated=false where customerid=:customerid";
		Map paramMap=new HashMap();
		paramMap.put("customerid", customerid);
		int count = namedParameterJdbcTemplate.update(sql, paramMap);
		return count >= 0 ? true : false;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月11日上午11:27:06
	 *
	 * 方法名：updateQuestionData
	 * 方法描述：根据CustomeridAndQid更新消费者信息
	 */
	public boolean updateQuestionData(QuestionData questionData){
		String sql = "update questiondata set data=:data where qid=:qid and customerid=:customerid";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(questionData);
		int count = namedParameterJdbcTemplate.update(sql, paramSource);
		return count > 0 ? true : false;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月11日上午11:23:22
	 *
	 * 方法名：existQuestionDataByCustomeridAndQid
	 * 方法描述：根据CustomeridAndQid查看消费者数据是否存在
	 */
	public boolean existQuestionDataByCustomeridAndQid(QuestionData questionData){
		String sql = "select count(*) from questiondata where qid=:qid and customerid=:customerid";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(questionData);
		int count = namedParameterJdbcTemplate.queryForInt(sql, paramSource);
		return count > 0 ? true : false;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日上午11:36:50
	 *
	 * 方法名：getQuestionDataCountByQid
	 * 方法描述：根据qid得到对应的数据量
	 */
	public int getQuestionDataCountByQid(int qid){
		String sql = "select count(*) from questiondata where qid=:qid";
		Map paramMap=new HashMap();
		paramMap.put("qid", qid);
		int count = namedParameterJdbcTemplate.queryForInt(sql, paramMap);
		return count;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日上午10:35:49
	 *
	 * 方法名：batchDeleteQuestionData
	 * 方法描述：根据qid批量删除QuestionData数据
	 */
	public int deleteQuestionDatasByQid(int qid) {  
		String sql = "delete from questiondata where qid=:qid";
		Map paramMap=new HashMap();
		paramMap.put("qid", qid);
		int count = namedParameterJdbcTemplate.update(sql, paramMap);
		return count;  
	}
	
	/*
	 * 向QuestionData表中写入一条记录
	 */
	public boolean insertQuestionData(QuestionData questionData){
		String sql = "insert into questiondata(data,qid,customerid,integrated) values(:data,:qid,:customerid,:integrated)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(questionData);
		int count = namedParameterJdbcTemplate.update(sql, paramSource);
		return count > 0 ? true : false;
	}

	/*
	 * 对jsonb用户数据的模糊查询，统计总数(有source_id)
	 */
	public int getQuestionDataCountByRequestParams(int qid,String sqlParams,String removeCustomerIds) {

		String sql = "";

		if(sqlParams.length()!=0 && removeCustomerIds.length()!=0){
			sql = "select count(*) from questiondata where integrated and qid=" + qid 
					+ " and "+ sqlParams 
					+ " and customerid not in ("+ removeCustomerIds +")";
		}else if(sqlParams.length()!=0 && removeCustomerIds.length()==0){
			sql = "select count(*) from questiondata where integrated and qid=" + qid 
					+" and "+ sqlParams;
		}else if(sqlParams.length()==0 && removeCustomerIds.length()!=0){
			sql = "select count(*) from questiondata where integrated and qid=" + qid
					+ " and customerid not in ("+ removeCustomerIds +")";
		}else{
			sql = "select count(*) from questiondata where integrated and qid=" + qid;
		}

		int count = namedParameterJdbcTemplate.getJdbcOperations().queryForInt(sql);
		return count;
	}

	/*
	 * 对jsonb用户数据的模糊查询(有source_id)
	 */
	public List<QuestionData> getQuestionDataPageByRequestParams(int qid,String sqlParams,String removeCustomerIds,int eachPageRowNum, int startPosition) {

		String sql = "";
		if(sqlParams.length()!=0 && removeCustomerIds.length()!=0){
			sql = "select * from questiondata where integrated and qid=" + qid + " and "+ sqlParams 
					+ " and customerid not in ("+ removeCustomerIds +")";
		}else if(sqlParams.length()!=0 && removeCustomerIds.length()==0){
			sql = "select * from questiondata where integrated and qid=" + qid + " and "+ sqlParams;
		}else if(sqlParams.length()==0 && removeCustomerIds.length()!=0){
			sql = "select * from questiondata where integrated and qid=" + qid 
					+ " and customerid not in ("+ removeCustomerIds +")";
		}else{
			sql = "select * from questiondata where integrated and qid=" + qid;
		}

		sql = sql + " order by id asc limit " + eachPageRowNum + " offset " + startPosition;

		//System.out.println(sql);

		RowMapper<QuestionData> rowMapper = new BeanPropertyRowMapper<>(QuestionData.class);
		List<QuestionData> datas = namedParameterJdbcTemplate.query(sql, rowMapper);
		return datas;
	}

	/*
	 * 对用户数据的不分页查询,一次性全部查处
	 */
	public List<QuestionData> getQuestionDatasByRequestParams(int qid,String sqlParams,String removeCustomerIds) {

		String sql = "";
		if(sqlParams.length()!=0 && removeCustomerIds.length()!=0){
			sql = "select * from questiondata where integrated and qid=" + qid + " and "+ sqlParams 
					+ " and customerid not in ("+ removeCustomerIds +")";
		}else if(sqlParams.length()!=0 && removeCustomerIds.length()==0){
			sql = "select * from questiondata where integrated and qid=" + qid + " and "+ sqlParams;
		}else if(sqlParams.length()==0 && removeCustomerIds.length()!=0){
			sql = "select * from questiondata where integrated and qid=" + qid 
					+ " and customerid not in ("+ removeCustomerIds +")";
		}else{
			sql = "select * from questiondata where integrated and qid=" + qid;
		}

		//System.out.println(sql);
		RowMapper<QuestionData> rowMapper = new BeanPropertyRowMapper<>(QuestionData.class);
		List<QuestionData> datas = namedParameterJdbcTemplate.query(sql, rowMapper);
		return datas;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月7日下午8:43:22
	 *
	 * 方法名：getQuestionDatasByQid
	 * 方法描述：根据qid得到QuestionData
	 */
	public List<QuestionData> getQuestionDatasByQid(int qid) {

		String sql = "select * from questiondata where qid="+qid;
	
		RowMapper<QuestionData> rowMapper = new BeanPropertyRowMapper<>(QuestionData.class);
		List<QuestionData> datas = namedParameterJdbcTemplate.query(sql, rowMapper);
		return datas;
	}

}
