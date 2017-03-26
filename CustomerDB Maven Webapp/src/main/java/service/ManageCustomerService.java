package service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pojo.Customer;
import pojo.ManageCustomer;
import util.ExceptionCustomerData;
import util.ExceptionManageCustomer;
import util.ToolRequestParamsToSQLParams;
import util.ToolCreateCustomerId;
import util.ToolGlobalParams;
import dao.ManageCustomerDao;

@Service
public class ManageCustomerService {
	@Autowired
	ManageCustomerDao manageCustomerDao;
	@Autowired
	CustomerDataService customerDataService;
	@Autowired
	ToolRequestParamsToSQLParams toolRequestParamsToSQLParams;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月13日下午8:36:47
	 *
	 * 方法名：batchUpdateManagerid
	 * 方法描述：批量修改消费者的managerid,managername
	 */
	@Transactional
	public boolean batchUpdateManageridAndManagername(String managerid,String managername,JSONArray customerids){
		if(customerids.size()>0){
			List<ManageCustomer> manageCustomerList = new ArrayList<>();
			for(int i=0;i<customerids.size();i++){
				ManageCustomer manageCustomer = new ManageCustomer();
				manageCustomer.setCustomerid(customerids.getString(i));
				manageCustomer.setManagerid(managerid);
				manageCustomer.setManagername(managername);
				manageCustomerList.add(manageCustomer);
			}
			int[] updateResult = manageCustomerDao.batchUpdateManagerid(manageCustomerList);
			//遍历数组，如果数组中有《=0的值,说明批量操作失败
			for(int i=0;i<updateResult.length;i++){
				if(updateResult[i]<=0){
					throw new ExceptionManageCustomer("批量访员id、姓名时失败");
				}
			}
		}
		return true;
	}
	
	
	/*
	 * 向managecustomer表中写入一条记录
	 */
	public boolean insertManageCustomer(ManageCustomer manageCustomer){
		return manageCustomerDao.insertManageCustomer(manageCustomer);
	}


	/*
	 * 将访员输入的消费者姓名、电话,导入进Customer数据库中
	 * 包括3个步骤：
	 * 1.通过电话号码,检查消费者是否存在
	 * 2.Customer表中添加消费者初始信息(姓名、电话)
	 * 3.ManageCustomer表中添加访员消费者关联信息
	 */
	@Transactional
	public JSONObject inputManagerCollectInfoToCustomerDB(String managerid,String managername,String managercustomerrelation,JSONObject customerNameTel) {
		//每个消费者添加之后需要返回customerid和是否成功标示
		JSONObject result = new JSONObject();
		String addCustomerid = "";
		boolean addResult = false;
		
		//1.通过电话号码,检查消费者是否存在
		JSONObject customerTel = new JSONObject();
		customerTel.accumulate(ToolGlobalParams.telnumberTagId, customerNameTel.get(ToolGlobalParams.telnumberTagId));
		if(!customerDataService.existCustomerByCustomerTel(customerTel)){
			//说明此消费者不存在,访员关联不存在,都需要添加
			//2.Customer表中添加信息 
			Customer customer = new Customer();
			String content = customerDataService.createNewCustomerDataContent(customerNameTel.toString(), new Timestamp(new Date().getTime()).toString());
			customer.setContent(content);
			String customerid = ToolCreateCustomerId.createCustomerId(ToolGlobalParams.companyNumber_JinPai);
			customer.setCustomerid(customerid);
			if(!customerDataService.insertCustomer(customer)){
				throw new ExceptionCustomerData("向CustomerData插入数据失败");
			}
			//3.ManageCustomer表中添加信息
			ManageCustomer manageCustomer = new ManageCustomer();
			manageCustomer.setManagerid(managerid);
			manageCustomer.setManagername(managername);
			manageCustomer.setCustomerid(customer.getCustomerid());
			manageCustomer.setManagercustomerrelation(managercustomerrelation);
			if(!insertManageCustomer(manageCustomer)){
				throw new ExceptionManageCustomer("访员与消费者关联失败");  
			}
			
			addCustomerid = customer.getCustomerid();
			addResult = true;
		}else{
			Customer customer = customerDataService.getCustomerByTel(customerTel);
			if(manageCustomerDao.existManageCustomerByCustomerId(customer.getCustomerid())){
				//说明Customer表与ManageCustomer表的信息都是完整的不需要添加
				addCustomerid = customer.getCustomerid();
				addResult = false;
			}else{
				//说明Customer表有消费者信息,ManageCustomer表没有访员关联信息
				ManageCustomer manageCustomer = new ManageCustomer();
				manageCustomer.setManagerid(managerid);
				manageCustomer.setManagername(managername);
				manageCustomer.setCustomerid(customer.getCustomerid());
				manageCustomer.setManagercustomerrelation(managercustomerrelation);
				if(!insertManageCustomer(manageCustomer)){
					throw new ExceptionManageCustomer("访员与消费者关联失败");  
				}
				addCustomerid = customer.getCustomerid();
				addResult = true;
			}
		}
		result.accumulate("resultCode", addResult);
		result.accumulate("customerid", addCustomerid);
		return result;
	}


	/*
	 * 根据managerid,得到该访员所管理的所有消费者
	 */
	public List<Customer> getCustomersByManagerid(String managerid) {
		List<ManageCustomer> manageCustomers= getManageCustomersByManagerid(managerid);
		List<Customer> customers= new ArrayList<>();
		for(ManageCustomer manageCustomer : manageCustomers){
			Customer customer = customerDataService.getCustomerByCustomerid(manageCustomer.getCustomerid());
			customers.add(customer);
		}
		return customers;
	}

	/*
	 * 根据customerid,找到ManageCustomer
	 * 这里最多找到一个
	 */
	public ManageCustomer getManageCustomerByCustomerId(String customerid) {
		ManageCustomer manageCustomer = new ManageCustomer();
		if(manageCustomerDao.existManageCustomerByCustomerId(customerid)){//如果这条记录存在，再去查这个对象
			manageCustomer =  manageCustomerDao.getManageCustomerByCustomerId(customerid);
		}
		return manageCustomer;
	}

	/*
	 * 根据managerid找到所有记录
	 */
	public List<ManageCustomer> getManageCustomersByManagerid(String managerid) {
		return manageCustomerDao.getManageCustomersByManagerid(managerid);
	}

	/*
	 * 在还没有给这个消费者发送问卷之前,可以修改这个消费者的信息
	 */
	public boolean updateCustomerBaesdInfoByManager(String customerid,String customerNameTel){
		String content = customerDataService.createNewCustomerDataContent(customerNameTel, new Timestamp(new Date().getTime()).toString());
		Customer customer = new Customer();
		customer.setCustomerid(customerid);
		customer.setContent(content);
		return customerDataService.updateCustomer(customer);
	}

}
