package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import pojo.SystemDynamicValues;

@Repository
public class SystemDynamicValuesDao {
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月6日下午5:14:30
	 * 
	 * 方法名：getSystemDynamicValuesByName
	 * 方法描述：根据name,得到系统动态值
	 */
	public SystemDynamicValues getSystemDynamicValuesByName(String name) {

		String sql = "select * from systemdynamicvalues where name='"+ name+"'";
		SystemDynamicValues systemDynamicValues = new SystemDynamicValues();
		systemDynamicValues.setName(name);
		SqlParameterSource ps=new BeanPropertySqlParameterSource(systemDynamicValues);
		return (SystemDynamicValues)namedParameterJdbcTemplate.queryForObject(sql, ps, new BeanPropertyRowMapper(SystemDynamicValues.class));
	}
	
}
