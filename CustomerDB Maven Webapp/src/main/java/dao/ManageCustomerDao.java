package dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import pojo.ManageCustomer;

@Repository
public class ManageCustomerDao {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月13日下午8:28:00
	 *
	 * 方法名：batchUpdateManagerid
	 * 方法描述：批量修改消费者的managerid,managername
	 */
	public int[] batchUpdateManagerid(List<ManageCustomer> manageCustomerList){
		String sql = "update managecustomer set managerid=:managerid,managername=:managername where customerid=:customerid";
		return namedParameterJdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(manageCustomerList.toArray()));  
	}
	
	/*
	 * 向managecustomer表中写入一条记录
	 */
	public boolean insertManageCustomer(ManageCustomer manageCustomer){
		String sql = "insert into managecustomer(managerid,managername,customerid,managercustomerrelation) values(:managerid,:managername,:customerid,:managercustomerrelation)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(manageCustomer);
		int count = namedParameterJdbcTemplate.update(sql, paramSource);
		return count > 0 ? true : false;
	}

	/*
	 * 根据customerid,找到关联记录
	 */
	public boolean existManageCustomerByCustomerId(String customerid) {

		String sql = "select count(*) from managecustomer where customerid='"+ customerid+"'";
		int count = namedParameterJdbcTemplate.getJdbcOperations().queryForInt(sql);
		return count > 0 ? true : false;
	}

	/*
	 * 根据managerid找到所有记录
	 */
	public List<ManageCustomer> getManageCustomersByManagerid(String managerid) {

		String sql = "select * from managecustomer where managerid='" + managerid+"'";
		RowMapper<ManageCustomer> rowMapper = new BeanPropertyRowMapper<>(ManageCustomer.class);
		List<ManageCustomer> manageCustomers = namedParameterJdbcTemplate.query(sql, rowMapper);
		return manageCustomers;
	}

	/*
	 * 根据customerid,找到ManageCustomer
	 * 这里最多找到一个
	 */
	public ManageCustomer getManageCustomerByCustomerId(String customerid) {

		String sql = "select * from managecustomer where customerid='"+ customerid+"'";
		ManageCustomer manageCustomer = new ManageCustomer();
		manageCustomer.setCustomerid(customerid);
		SqlParameterSource ps=new BeanPropertySqlParameterSource(manageCustomer);
		return (ManageCustomer)namedParameterJdbcTemplate.queryForObject(sql, ps, new BeanPropertyRowMapper(ManageCustomer.class));
	}
}
