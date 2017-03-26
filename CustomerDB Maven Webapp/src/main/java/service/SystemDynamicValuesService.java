package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pojo.SystemDynamicValues;
import dao.SystemDynamicValuesDao;

@Service
public class SystemDynamicValuesService {
	@Autowired
	SystemDynamicValuesDao systemDynamicValuesDao;

	/*
	 * 根据name,得到所有的动态值,相当于系统初始化时使用
	 */
	public SystemDynamicValues getSystemDynamicValuesByName(String name) {
		return systemDynamicValuesDao.getSystemDynamicValuesByName(name);
	}

	/*
	 * 得到所有的动态值
	 */
	public List<SystemDynamicValues> getAllSystemDynamicValues() {
		return systemDynamicValuesDao.getAllSystemDynamicValues();
	}

	/*
	 * 根据id,得到对应动态值
	 */
	public SystemDynamicValues getSystemDynamicValuesById(int id) {
		return systemDynamicValuesDao.getSystemDynamicValuesById(id);
	}

	//根据id,修改动态值
	public boolean updateDynamicvalues(SystemDynamicValues systemDynamicValues) {
		return systemDynamicValuesDao.updateDynamicvalues(systemDynamicValues);
	}
}
