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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import pojo.Tag;

@Repository
public class TagStoreDao {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月6日上午11:22:17
	 * 
	 * 方法名：getALLBeusedTags
	 * 方法描述：得到所有被使用过的标签(为了得到id)
	 */
	public List<Tag> getALLBeusedTagids() {
		String sql = "select id from tagstore where beused_times>0";
		RowMapper<Tag> rowMapper = new BeanPropertyRowMapper<>(Tag.class);
		List<Tag> tags = namedParameterJdbcTemplate.query(sql, rowMapper);
		return tags;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月6日上午11:22:41
	 * 
	 * 方法名：getTag
	 * 方法描述：得到一个标签
	 */
	public Tag getTag(Tag tag) {
		String sql = "select * from tagstore where id=:id";
		RowMapper<Tag> rowMapper = new BeanPropertyRowMapper<>(Tag.class);
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(tag);
		tag = namedParameterJdbcTemplate.queryForObject(sql, paramSource, rowMapper);
		return tag;
	}
	
	/*
	 * 这里添加标签,只添加标签的名字类型等
	 */
	public int insertTag(Tag tag) {
		String sql = "insert into tagstore(cnname,type,parent_id,son_ids,beused_times) values(:cnname,:type,:parent_id,:son_ids,:beused_times)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(tag);
		KeyHolder keyholder = new GeneratedKeyHolder();// 加上KeyHolder可以得到添加后主键的值
		namedParameterJdbcTemplate.update(sql, paramSource, keyholder);
		int key = (int) keyholder.getKeys().get("id");
		return key;
	}
	
	/*
	 * 修改标签
	 * 这里不能修改beused_times
	 */
	public boolean updateTag(Tag tag) {
		String sql = "update tagstore set cnname=:cnname,type=:type,parent_id=:parent_id,son_ids=:son_ids where id=:id";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(tag);
		int count = namedParameterJdbcTemplate.update(sql, paramSource);
		return count > 0 ? true : false;
	}
	
	/*
	 * 得到所有可以做父标签的标签
	 * type=parent
	 */
	public List<Tag> getALLParentTags() {
		String sql = "select * from tagstore where type='parent' order by id desc";
		RowMapper<Tag> rowMapper = new BeanPropertyRowMapper<>(Tag.class);
		List<Tag> tags = namedParameterJdbcTemplate.query(sql, rowMapper);
		return tags;
	}
	
	/*
	 * 得到所有可以做子标签的标签
	 * parent_id=0
	 */
	public List<Tag> getALLSonTags() {
		String sql = "select * from tagstore where parent_id=0 order by id desc";
		RowMapper<Tag> rowMapper = new BeanPropertyRowMapper<>(Tag.class);
		List<Tag> tags = namedParameterJdbcTemplate.query(sql, rowMapper);
		return tags;
	}
	
	/*
	 * 得到标签库里最上层的已经被使用了得标签
	 * parent_id=0 and beused_times>0
	 */
	public List<Tag> getALLBeusedParentTags() {
		String sql = "select * from tagstore where parent_id=0 and beused_times>0 order by beused_times desc";
		RowMapper<Tag> rowMapper = new BeanPropertyRowMapper<>(Tag.class);
		List<Tag> tags = namedParameterJdbcTemplate.query(sql, rowMapper);
		return tags;
	}

	/*
	 * 删除没有被使用的标签
	 */
	public boolean deleteTag(int id) {
		String sql="delete from tagstore where id=:id and beused_times=0";
		Map paramMap=new HashMap();
		paramMap.put("id", id);
		int count = namedParameterJdbcTemplate.update(sql, paramMap);
		return count > 0 ? true : false;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日下午9:47:51
	 *
	 * 方法名：batchUpdateBeused_timesAdd
	 * 方法描述：批量更新标签的使用次数加一
	 */
	public int[] batchUpdateBeused_timesAdd(List<Tag> tagList){
		String sql = "update tagstore set beused_times = beused_times+1 where id=:id";
		return namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(tagList.toArray()));  
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月2日下午9:40:50
	 *
	 * 方法名：batchUpdateBeused_timesSub
	 * 方法描述：批量更新标签的使用次数减一
	 */
	public int[] batchUpdateBeused_timesSub(List<Tag> tagList){
		String sql = "update tagstore set beused_times = beused_times-1 where id=:id and beused_times>0";
		return namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(tagList.toArray()));  
	}

}
