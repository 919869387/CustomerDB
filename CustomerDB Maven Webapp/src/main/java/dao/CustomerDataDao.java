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

import pojo.Customer;

@Repository
public class CustomerDataDao {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月23日下午10:34:23
	 *
	 * 方法名：getAllCustomerDataContent
	 * 方法描述：得到表中所有用户的数据
	 */
	public List<Customer> getAllCustomerData() {
		String sql = "select * from customerdata order by id desc";
		RowMapper<Customer> rowMapper = new BeanPropertyRowMapper<>(Customer.class);
		List<Customer> customers = namedParameterJdbcTemplate.getJdbcOperations().query(sql, rowMapper);
		return customers;
	}
	
	/*
	 * 删除信息不完整的消费者记录
	 */
	public boolean deleteNoContentCustomer(String customerid){
		String sql="delete from customerdata where customerid=:customerid";
		Map paramMap=new HashMap();
		paramMap.put("customerid", customerid);
		int count = namedParameterJdbcTemplate.update(sql, paramMap);
		return count > 0 ? true : false;
	}

	/*
	 * 向customerdata表中写入一条记录
	 */
	public boolean insertCustomer(Customer customer){
		String sql = "insert into customerdata(content,customerid,integrated,infointegrated) values(:content,:customerid,:integrated,:infointegrated)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(customer);
		int count = namedParameterJdbcTemplate.update(sql, paramSource);
		return count > 0 ? true : false;
	}

	/*
	 * 根据CustomerId,验证Customer是否存在
	 */
	public boolean existCustomerByCustomerid(String customerid) {
		String sql = "select count(*) from customerdata where customerid='"+ customerid+"'";
		int count = namedParameterJdbcTemplate.getJdbcOperations().queryForInt(sql);
		return count > 0 ? true : false;
	}

	/*
	 * 根据电话号码，验证Customer是否存在
	 */
	public boolean existCustomerByCustomerTel(String contentParams) {
		String sql = "select count(*) from customerdata where "+ contentParams;
		int count = namedParameterJdbcTemplate.getJdbcOperations().queryForInt(sql);
		return count > 0 ? true : false;
	}

	/*
	 * 根据customerid找到消费者,已经保证这个消费者存在才这样写
	 */
	public Customer getCustomerByCustomerid(String customerid) {

		String sql = "select * from customerdata where customerid='" + customerid+"'";
		Customer customer = new Customer();
		customer.setCustomerid(customerid);
		SqlParameterSource ps=new BeanPropertySqlParameterSource(customer);
		return (Customer)namedParameterJdbcTemplate.queryForObject(sql, ps, new BeanPropertyRowMapper(Customer.class));
	}

	/*
	 * 根据customerid,更新消费者
	 */
	public boolean updateCustomer(Customer customer) {
		String sql = "update customerdata set content=:content,integrated=:integrated,infointegrated=:infointegrated where customerid=:customerid";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(customer);
		int count = namedParameterJdbcTemplate.update(sql, paramSource);
		return count > 0 ? true : false;
	}

	/*
	 * 根据条件统计总数
	 * 整表查询
	 */
	public int getCustomerRecordCount(String contentParams,String removeCustomerIds,boolean infointegratedSwitch) {

		String sql = "";
		if(contentParams.length()!=0 && removeCustomerIds.length()!=0){
			sql = "select count(*) from customerdata where integrated and "+ contentParams 
					+ " and customerid not in ("+ removeCustomerIds +")";
		}else if(contentParams.length()!=0 && removeCustomerIds.length()==0){
			sql = "select count(*) from customerdata where integrated and "+ contentParams;
		}else if(contentParams.length()==0 && removeCustomerIds.length()!=0){
			sql = "select count(*) from customerdata where integrated and"
					+ " customerid not in ("+ removeCustomerIds +")";
		}else{
			sql = "select count(*) from customerdata where integrated";
		}

		if(infointegratedSwitch==true){
			//开关是开的,说明只筛选信息完整的消费者
			sql = sql + " and infointegrated";
		}
		
		int count = namedParameterJdbcTemplate.getJdbcOperations().queryForInt(sql);
		return count;
	}


	/*
	 * 不分页查询
	 * 根据条件查找数据,没有可选条件
	 */
	public List<Customer> getCustomerDatas(String contentParams,String removeCustomerIds,boolean infointegratedSwitch) {

		String sql = "";
		if(contentParams.length()!=0 && removeCustomerIds.length()!=0){
			sql = "select * from customerdata where integrated and "+ contentParams 
					+ " and customerid not in ("+ removeCustomerIds +")";
		}else if(contentParams.length()!=0 && removeCustomerIds.length()==0){
			sql = "select * from customerdata where integrated and "+ contentParams;
		}else if(contentParams.length()==0 && removeCustomerIds.length()!=0){
			sql = "select * from customerdata where integrated and"
					+ " customerid not in ("+ removeCustomerIds +")";
		}else{
			sql = "select * from customerdata where integrated";
		}
		
		if(infointegratedSwitch==true){
			//开关是开的,说明只筛选信息完整的消费者
			sql = sql + " and infointegrated";
		}
		
		RowMapper<Customer> rowMapper = new BeanPropertyRowMapper<>(Customer.class);
		List<Customer> customers = namedParameterJdbcTemplate.getJdbcOperations().query(sql, rowMapper);
		return customers;
	}


	/*
	 * 根据条件查找分页数据,没有可选条件
	 * 整表查询
	 */
	public List<Customer> getPageCustomerData(String contentParams,String removeCustomerIds,int eachPageRowNum, int startPosition,boolean infointegratedSwitch) {

		String sql = "";
		if(contentParams.length()!=0 && removeCustomerIds.length()!=0){
			sql = "select * from customerdata where integrated and "+ contentParams 
					+ " and customerid not in ("+ removeCustomerIds +")";
		}else if(contentParams.length()!=0 && removeCustomerIds.length()==0){
			sql = "select * from customerdata where integrated and "+ contentParams;
		}else if(contentParams.length()==0 && removeCustomerIds.length()!=0){
			sql = "select * from customerdata where integrated and"
					+ " customerid not in ("+ removeCustomerIds +")";
		}else{
			sql = "select * from customerdata where integrated";
		}
		
		if(infointegratedSwitch==true){
			//开关是开的,说明只筛选信息完整的消费者
			sql = sql + " and infointegrated";
		}
		
		sql = sql + " order by id asc limit " + eachPageRowNum + " offset " + startPosition;

		RowMapper<Customer> rowMapper = new BeanPropertyRowMapper<>(Customer.class);
		List<Customer> customers = namedParameterJdbcTemplate.getJdbcOperations().query(sql, rowMapper);
		return customers;
	}

	/*
	 * 根据消费者电话 查找到消费者
	 * 
	 * 这里用List接收数据，但是一定只有一个
	 */
	public Customer getCustomerByTel(String contentParams) {
		String sql = "select * from customerdata where "+ contentParams;
		RowMapper<Customer> rowMapper = new BeanPropertyRowMapper<>(Customer.class);
		List<Customer> customers = namedParameterJdbcTemplate.getJdbcOperations().query(sql, rowMapper);
		return customers.get(0);
	}


	/*
	 * 根据条件查找分页数据
	 * 当用户所给条件筛选出的样本不够用时，需要通过去可选条件来进行筛选
	 * contentTotalParams > contentNecessaryParams
	 */
	public List<Customer> getPageCustomerDataExtra(String contentTotalParams,String contentNecessaryParams,String removeCustomerIds,int eachPageRowNum, int startPosition,boolean infointegratedSwitch) {

		String sql = "";

		if(contentNecessaryParams.length()!=0 && removeCustomerIds.length()!=0){
			sql = "select * from customerdata where integrated and customerid not in(select customerid from customerdata where "+contentTotalParams+") and " + contentNecessaryParams
					+ " and customerid not in ("+ removeCustomerIds +")";

		}else if(contentNecessaryParams.length()!=0 && removeCustomerIds.length()==0){
			sql = "select * from customerdata where integrated and customerid not in(select customerid from customerdata where "+contentTotalParams+") and " + contentNecessaryParams;

		}else if(contentNecessaryParams.length()==0 && removeCustomerIds.length()!=0){
			sql = "select * from customerdata where integrated and customerid not in(select customerid from customerdata where "+contentTotalParams+")"
					+ " and customerid not in ("+ removeCustomerIds +")";

		}else{
			sql = "select * from customerdata where integrated and customerid not in(select customerid from customerdata where "+contentTotalParams+")";

		}
		
		if(infointegratedSwitch==true){
			//开关是开的,说明只筛选信息完整的消费者
			sql = sql + " and infointegrated";
		}
		
		sql = sql + " order by id asc limit " + eachPageRowNum + " offset " + startPosition;

		//System.out.println(sql);

		RowMapper<Customer> rowMapper = new BeanPropertyRowMapper<>(Customer.class);
		List<Customer> customers = namedParameterJdbcTemplate.getJdbcOperations().query(sql, rowMapper);
		return customers;
	}

	/*
	 * 不分页查询数据
	 * 当用户所给条件筛选出的样本不够用时，需要通过去可选条件来进行筛选
	 * contentTotalParams > contentNecessaryParams
	 */
	public List<Customer> getCustomerDatasExtra(String contentTotalParams,String contentNecessaryParams,String removeCustomerIds,boolean infointegratedSwitch) {

		String sql = "";

		if(contentNecessaryParams.length()!=0 && removeCustomerIds.length()!=0){
			sql = "select * from customerdata where integrated and customerid not in(select customerid from customerdata where "+contentTotalParams+") and " + contentNecessaryParams
					+ " and customerid not in ("+ removeCustomerIds +")";

		}else if(contentNecessaryParams.length()!=0 && removeCustomerIds.length()==0){
			sql = "select * from customerdata where integrated and customerid not in(select customerid from customerdata where "+contentTotalParams+") and " + contentNecessaryParams;

		}else if(contentNecessaryParams.length()==0 && removeCustomerIds.length()!=0){
			sql = "select * from customerdata where integrated and customerid not in(select customerid from customerdata where "+contentTotalParams+")"
					+ " and customerid not in ("+ removeCustomerIds +")";

		}else{
			sql = "select * from customerdata where integrated and customerid not in(select customerid from customerdata where "+contentTotalParams+")";

		}
		//System.out.println(sql);
		if(infointegratedSwitch==true){
			//开关是开的,说明只筛选信息完整的消费者
			sql = sql + " and infointegrated";
		}
		RowMapper<Customer> rowMapper = new BeanPropertyRowMapper<>(Customer.class);
		List<Customer> customers = namedParameterJdbcTemplate.getJdbcOperations().query(sql, rowMapper);
		return customers;
	}


}
