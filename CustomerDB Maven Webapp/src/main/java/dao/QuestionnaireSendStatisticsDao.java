package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import pojo.ManageCustomer;
import pojo.QuestionnaireSendStatistics;

@Repository
public class QuestionnaireSendStatisticsDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/*
	 * 向questionnaire_sendstatistics表中写入一条记录
	 */
	public boolean insertQuestionnaireSendStatistics(QuestionnaireSendStatistics questionnaireSendStatistics){
		String sql = "insert into questionnaire_sendstatistics(customerid) values(:customerid)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(questionnaireSendStatistics);
		int count = namedParameterJdbcTemplate.update(sql, paramSource);
		return count > 0 ? true : false;
	}
	
	/*
	 * 根据CustomerId验证QuestionnaireSendStatistics是否存在
	 */
	public boolean existQuestionnaireSendStatisticsByCustomerId(String customerid) {
		String sql = "select count(*) from questionnaire_sendstatistics where customerid='"+ customerid+"'";
		int count = namedParameterJdbcTemplate.getJdbcOperations().queryForInt(sql);
		return count > 0 ? true : false;
	}
	
	/*
	 * 根据customerid,将totalsendnum+1
	 */
	public boolean updateQuestionnaireSendStatistics_ADDtotalsendnum(String customerid) {
		String sql = "update questionnaire_sendstatistics set totalsendnum = totalsendnum + 1 where customerid='"+customerid+"'";
		int count = namedParameterJdbcTemplate.getJdbcOperations().update(sql);
		return count > 0 ? true : false;
	}
	
	/*
	 * 根据customerid,将successsendnum+1
	 */
	public boolean updateQuestionnaireSendStatistics_ADDsuccesssendnum(String customerid) {
		String sql = "update questionnaire_sendstatistics set successsendnum = successsendnum + 1 where customerid='"+customerid+"'";
		int count = namedParameterJdbcTemplate.getJdbcOperations().update(sql);
		return count > 0 ? true : false;
	}
	
	/*
	 * 根据customerid,将answernum+1
	 */
	public boolean updateQuestionnaireSendStatistics_ADDanswernum(String customerid) {
		String sql = "update questionnaire_sendstatistics set answernum = answernum + 1 where customerid='"+customerid+"'";
		int count = namedParameterJdbcTemplate.getJdbcOperations().update(sql);
		return count > 0 ? true : false;
	}
	
	/*
	 * 根据customerid,找到QuestionnaireSendStatistics
	 */
	public QuestionnaireSendStatistics getQuestionnaireSendStatisticsByCustomerId(String customerid) {

		String sql = "select * from questionnaire_sendstatistics where customerid='"+ customerid+"'";
		QuestionnaireSendStatistics questionnaireSendStatistics = new QuestionnaireSendStatistics();
		questionnaireSendStatistics.setCustomerid(customerid);
		SqlParameterSource ps=new BeanPropertySqlParameterSource(questionnaireSendStatistics);
		return (QuestionnaireSendStatistics)namedParameterJdbcTemplate.queryForObject(sql, ps, new BeanPropertyRowMapper(QuestionnaireSendStatistics.class));
	}

}
