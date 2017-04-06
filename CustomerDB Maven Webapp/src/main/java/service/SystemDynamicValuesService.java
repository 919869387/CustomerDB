package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pojo.SystemDynamicValues;
import dao.SystemDynamicValuesDao;

@Service
public class SystemDynamicValuesService {
	@Autowired
	SystemDynamicValuesDao systemDynamicValuesDao;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月6日下午5:14:44
	 * 
	 * 方法名：getSystemDynamicValuesByName
	 * 方法描述：根据name,得到系统动态值
	 */
	public SystemDynamicValues getSystemDynamicValuesByName(String name) {
		return systemDynamicValuesDao.getSystemDynamicValuesByName(name);
	}

}
