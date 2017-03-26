package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SystemUserDao {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/*
	 * 根据用户名、密码进行验证登录
	 */
	public boolean existSystemUser(String username,String password) {

		String sql = "select count(*) from systemuser where username='"+ username+"' and password='"+password+"'";
		int count = namedParameterJdbcTemplate.getJdbcOperations().queryForInt(sql);
		return count > 0 ? true : false;
	}
}
