package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.SystemUserDao;

@Service
public class SystemUserService {
	@Autowired
	SystemUserDao systemUserDao;
	/*
	 * 根据用户名、密码进行验证登录
	 */
	public boolean existSystemUser(String username,String password) {
		
		if (username == null || "".equals(username)) {
			return false;
		}
		
		if (password == null || "".equals(password)) {
			return false;
		}
		return systemUserDao.existSystemUser(username, password);
	}


}
