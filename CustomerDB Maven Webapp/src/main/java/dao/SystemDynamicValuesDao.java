package dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import pojo.SystemDynamicValues;

@Repository
public class SystemDynamicValuesDao {
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/*
	 * 根据name,得到所有的动态值,相当于系统初始化时使用
	 */
	public SystemDynamicValues getSystemDynamicValuesByName(String name) {

		String sql = "select * from systemdynamicvalues where name='"+ name+"'";
		SystemDynamicValues systemDynamicValues = new SystemDynamicValues();
		systemDynamicValues.setName(name);
		SqlParameterSource ps=new BeanPropertySqlParameterSource(systemDynamicValues);
		return (SystemDynamicValues)namedParameterJdbcTemplate.queryForObject(sql, ps, new BeanPropertyRowMapper(SystemDynamicValues.class));
	}
	
	/*
	 * 得到所有的动态值
	 */
	public List<SystemDynamicValues> getAllSystemDynamicValues() {

		String sql = "select * from systemdynamicvalues ORDER BY id ASC";
		
		RowMapper<SystemDynamicValues> rowMapper = new BeanPropertyRowMapper<>(
				SystemDynamicValues.class);
		List<SystemDynamicValues> allSystemDynamicValues = namedParameterJdbcTemplate.query(sql, rowMapper);
		return allSystemDynamicValues;
	}
	
	/*
	 * 根据id,得到对应动态值
	 */
	public SystemDynamicValues getSystemDynamicValuesById(int id) {

		String sql = "select * from systemdynamicvalues where id="+id+"";
		SystemDynamicValues systemDynamicValues = new SystemDynamicValues();
		systemDynamicValues.setId(id);
		SqlParameterSource ps=new BeanPropertySqlParameterSource(systemDynamicValues);
		return (SystemDynamicValues)namedParameterJdbcTemplate.queryForObject(sql, ps, new BeanPropertyRowMapper(SystemDynamicValues.class));
	}
	
	//根据id,修改动态值
	public boolean updateDynamicvalues(SystemDynamicValues systemDynamicValues) {
		String sql = "update systemdynamicvalues set dynamicvalues=:dynamicvalues where id=:id";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(systemDynamicValues);
		int count = namedParameterJdbcTemplate.update(sql, paramSource);
		return count > 0 ? true : false;
	}
}
