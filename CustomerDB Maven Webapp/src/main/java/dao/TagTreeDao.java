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

import pojo.TagTree;

@Repository
public class TagTreeDao {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;	
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月11日下午3:36:34
	 *
	 * 方法名：getTagTreeCount
	 * 方法描述：得到有数据导入的TagTree记录数
	 */
	public int getTagTreeCount() {

		String sql = "select count(*) from tagtree where recordcount>0";
		int count = namedParameterJdbcTemplate.getJdbcOperations().queryForInt(sql);
		return count;
	}
	
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月9日上午11:11:21
	 *
	 * 方法名：getTagTreePage
	 * 方法描述：分页得到TagTree表中的对象
	 */
	public List<TagTree> getTagTreePage(int eachPageRowNum, int startPosition){
		String sql = "select * from tagtree order by id desc limit " + eachPageRowNum + " offset " + startPosition;

		RowMapper<TagTree> rowMapper = new BeanPropertyRowMapper<>(TagTree.class);
		List<TagTree> tagTrees = namedParameterJdbcTemplate.query(sql, rowMapper);
		return tagTrees;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日上午11:45:55
	 *
	 * 方法名：getTagTreeCountByQid
	 * 方法描述：根据qid得到记录的个数，一般只有一个
	 */
	public int getTagTreeCountByQid(int qid){
		String sql = "select count(*) from tagtree where qid=:qid";
		Map paramMap=new HashMap();
		paramMap.put("qid", qid);
		int count = namedParameterJdbcTemplate.queryForInt(sql, paramMap);
		return count;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月8日上午11:46:45
	 *
	 * 方法名：deleteTagTreeByQid
	 * 方法描述：根据qid删除对应得记录
	 */
	public int deleteTagTreeByQid(int qid) {  
		String sql = "delete from tagtree where qid=:qid";
		Map paramMap=new HashMap();
		paramMap.put("qid", qid);
		int count = namedParameterJdbcTemplate.update(sql, paramMap);
		return count;  
	}
	
	
	public boolean insertTagTree(TagTree tagTree) {
		String sql = "insert into tagtree(qid,qname,tree) values(:qid,:qname,:tree)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(tagTree);
		int count = namedParameterJdbcTemplate.update(sql, paramSource);
		return count > 0 ? true : false;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日上午11:26:01
	 *
	 * 方法名：existQid
	 * 方法描述：查看是否存在qid这条记录
	 */
	public boolean existQid(int qid){
		String sql="select count(*) from tagtree where qid=:qid";
		Map paramMap=new HashMap();
		paramMap.put("qid", qid);
		int count=namedParameterJdbcTemplate.queryForInt(sql, paramMap);
		return count > 0 ? true : false;
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日上午11:15:00
	 *
	 * 方法名：getInputSignByQid
	 * 方法描述：根据问卷id找到该问卷的导入进度标识
	 */
	public int getInputSignByQid(int qid) {
		String sql="select inputsign from tagtree where qid=:qid";
		Map paramMap=new HashMap();
		paramMap.put("qid", qid);
		return namedParameterJdbcTemplate.queryForInt(sql, paramMap);

	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日下午8:49:17
	 *
	 * 方法名：updateInputSignByQid
	 * 方法描述：根据qid修改inputSign
	 */
	public boolean updateRecordCountByQid(int qid,int addRecordcount) {
		String sql = "update tagtree set recordcount=recordcount+"+addRecordcount+" where qid=:qid";
		Map paramMap=new HashMap();
		paramMap.put("qid", qid);
		int count = namedParameterJdbcTemplate.update(sql, paramMap);
		return count > 0 ? true : false;
	}
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月2日下午8:39:46
	 *
	 * 方法名：getTagTree
	 * 方法描述：根据qid得到标签树
	 */
	public TagTree getTagTree(TagTree tagTree) {
		String sql = "select * from tagtree where qid=:qid";
		RowMapper<TagTree> rowMapper = new BeanPropertyRowMapper<>(TagTree.class);
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(tagTree);
		tagTree = namedParameterJdbcTemplate.queryForObject(sql, paramSource,rowMapper);
		return tagTree;
	}



}
