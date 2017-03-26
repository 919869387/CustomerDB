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
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import pojo.Tag;
import pojo.TagValue;

@Repository
public class TagValueDao {
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月11日下午1:43:15
	 *
	 * 方法名：getTagValuesByTagid
	 * 方法描述：得到同一个tagid的所有记录
	 */
	public List<TagValue> getTagValuesByTagid(int tagid){
		String sql = "select * from tagvalue where tagid="+tagid+" order by id asc";
		RowMapper<TagValue> rowMapper = new BeanPropertyRowMapper<>(TagValue.class);
		List<TagValue> tagValues = namedParameterJdbcTemplate.query(sql, rowMapper);
		return tagValues;
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日上午11:23:30
	 *
	 * 方法名：getTagValueCountByQid
	 * 方法描述：根据qid得到问卷有值标签的数量
	 */
	public int getTagValueCountByQid(int qid){
		String sql = "select count(*) from tagvalue where qid=:qid";
		Map paramMap=new HashMap();
		paramMap.put("qid", qid);
		int count = namedParameterJdbcTemplate.queryForInt(sql, paramMap);
		return count;
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日上午11:01:43
	 *
	 * 方法名：deleteTagValueByQid
	 * 方法描述：根据qid删除问卷有值标签的标签值
	 */
	public int deleteTagValueByQid(int qid) {  
		String sql = "delete from tagvalue where qid=:qid";
		Map paramMap=new HashMap();
		paramMap.put("qid", qid);
		int count = namedParameterJdbcTemplate.update(sql, paramMap);
		return count;  
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月28日下午8:29:06
	 *
	 * 方法名：insertTagValue
	 * 方法描述：插入一条记录
	 */
	public boolean insertTagValue(TagValue tagValue) {
		String sql = "insert into tagvalue(tagid,qid,value) values(:tagid,:qid,:value)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(tagValue);
		int count = namedParameterJdbcTemplate.update(sql, paramSource);
		return count > 0 ? true : false;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年2月28日下午8:28:01
	 *
	 * 方法名：batchInsertTagValue
	 * 方法描述：批量插入tagValue,返回数组的每个元素值对应每条SQL影响的数据库的记录数。 
	 * 
	 */
	public int[] batchInsertTagValue(List<TagValue> tagValueList) {  
		String sql = "insert into tagvalue(tagid,qid,value) values(:tagid,:qid,:value)";
		return namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(tagValueList.toArray()));  
	}

}
