package service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pojo.Customer;
import pojo.QuestionData;
import util.ToolGlobalParams;
import util.ToolRequestParamsToSQLParams;
import dao.CustomerDataDao;

@Service
public class CustomerDataService {
	@Autowired
	CustomerDataDao customerDataDao;
	@Autowired
	ToolRequestParamsToSQLParams toolRequestParamsToSQLParams;
	@Autowired
	QuestionDataService questionDataService;

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月23日下午10:36:31
	 *
	 * 方法名：getAllCustomerDataContent
	 * 方法描述：得到所有用户的数据
	 */
	public List<Customer> getAllCustomerData() {
		List<Customer> customers = customerDataDao.getAllCustomerData();
		for(Customer customer : customers){
			//对每一个消费者进行数据选择
			//选值的方法是：将每个标签时间最近的值取出来
			customer = customerSelectValue(customer);
		}
		//由于List中每个对象都是引用，所以相当于是引用传值，已经将值修改了
		return customers;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月7日下午9:30:15
	 *
	 * 方法名：daleteValueByCustomeridsAndRecordtime
	 * 方法描述：根据customerid和时间戳，删除消费者信息中不需要的值
	 */
	public boolean daleteValueByCustomeridsAndRecordtime(List<String> customerids,List<String> recordTimes){
		for(int i=0;i<customerids.size();i++){
			String customerid = customerids.get(i);
			Customer customer = customerDataDao.getCustomerByCustomerid(customerid);

			JSONObject newContent = new JSONObject();
			//得到原始content
			JSONObject content = JSONObject.fromObject(customer.getContent());
			Iterator it = content.keys();  
			while (it.hasNext()) {  
				String key = (String) it.next();  
				JSONObject value_datetime = content.getJSONObject(key);
				JSONObject newValue_datetime = daleteValue(value_datetime,recordTimes);
				if(newValue_datetime.size()!=0){
					newContent.put(key, newValue_datetime);
				}
			}
			//查看删除后customer信息是否完全
			if(newContent.size()==0){
				//说明这个消费者是从问卷添加而来,此时他的信息为空,就将他删除
				if(!customerDataDao.deleteNoContentCustomer(customerid)){
					return false;
				}
			}else{
				if(!newContent.containsKey(ToolGlobalParams.telnumberTagId)){
					//将QuestionData表中Integrated置为False
					if(!questionDataService.updateQuestionDataIntegratedToFalse(customerid)){
						return false;
					}
					customer.setIntegrated(false);
					customer.setInfointegrated(false);
				}
				customer.setContent(newContent.toString());
				if(!updateCustomer(customer)){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月7日下午9:18:27
	 *
	 * 方法名：daleteValue
	 * 方法描述：删除时间戳相等的值
	 */
	public JSONObject daleteValue(JSONObject value_datetime,List<String> recordTimes){
		JSONObject newValue_datetime = new JSONObject();
		Iterator it = value_datetime.keys();  
		while (it.hasNext()) {  
			String value = (String) it.next();//这里的value相当于key
			String datetime = value_datetime.getString(value);
			if(!recordTimes.contains(datetime)){
				newValue_datetime.put(value, datetime);
			}
		}
		return newValue_datetime;
	}

	/*
	 * 不分页查询,用于数据下载
	 * 
	 * 当有可选参数存在时，使用此方法
	 * 只分两种情况,全条件查询与必要条件查询
	 * 
	 * 分页数据在前面的为筛选条件最多的
	 * 分页数据在后面的为筛选条件少的,数据不是很精确
	 * 
	 * "necessaryParams = {"22": "xxx", "33": "xxxx"}"
	 * "optionalParams = {"42": "男", "11": "xxxx"}"
	 * "removeCustomerIds":[]
	 */
	public List<Customer> getCustomersForOptionalParams(JSONObject requestNecessaryParams,JSONObject requestOptionalParams,JSONArray requestRemoveCustomerIds,boolean infointegratedSwitch) {

		List<Customer> customers = null;

		if(requestOptionalParams.size()==0){
			//如果用户没有写可选参数
			customers = getCustomers(requestNecessaryParams,requestRemoveCustomerIds,infointegratedSwitch);
			//System.out.println("没有可选参数");
		}else{

			JSONObject requestTotalParams = new JSONObject();
			requestTotalParams.putAll(requestNecessaryParams);
			requestTotalParams.putAll(requestOptionalParams);

			List<Customer> customers_FitstPart = getCustomers(requestTotalParams,requestRemoveCustomerIds,infointegratedSwitch);
			List<Customer> customers_SecondPart = getCustomersExtra(requestTotalParams, requestNecessaryParams,requestRemoveCustomerIds,infointegratedSwitch);

			customers_FitstPart.addAll(customers_SecondPart);//这句话是将第二部分添加进来,这样第一部分就变成完整的了
			customers = customers_FitstPart;
		}
		return customers;
	}


	/*
	 * 不分页,用于数据下载
	 * 没有可选条件的情况
	 * 整表查询
	 * 对于整表查询，需要数据归并
	 */
	public List<Customer> getCustomers(JSONObject requestContentParams,JSONArray requestRemoveCustomerIds,boolean infointegratedSwitch) {

		List<Customer> customers = null;
		if(requestContentParams.size()!=0 && requestRemoveCustomerIds.size()!=0){
			//说明有请求参数,将筛选参数转化为SQL语句格式
			String contentParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestContentParams);
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			customers = customerDataDao.getCustomerDatas(contentParams,removeCustomerIds_SQL,infointegratedSwitch);
		}else if(requestContentParams.size()==0 && requestRemoveCustomerIds.size()!=0){
			String contentParams = "";
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			customers = customerDataDao.getCustomerDatas(contentParams,removeCustomerIds_SQL,infointegratedSwitch);
		}else if(requestContentParams.size()!=0 && requestRemoveCustomerIds.size()==0){
			String contentParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestContentParams);
			String removeCustomerIds_SQL = "";
			customers = customerDataDao.getCustomerDatas(contentParams,removeCustomerIds_SQL,infointegratedSwitch);
		}else{
			String contentParams = "";
			String removeCustomerIds_SQL = "";
			customers = customerDataDao.getCustomerDatas(contentParams,removeCustomerIds_SQL,infointegratedSwitch);
		}

		for(Customer customer : customers){
			//对同一个消费者进行数据选择
			//选值的方法是：将每个标签最近的值取出来
			customer = customerSelectValue(customer);
		}
		//由于List中每个对象都是引用，所以相当于是引用传值，已经将值修改了
		return customers;
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年4月8日上午11:25:08
	 * 
	 * 方法名：insertOrUpdateQuestionDataToCustomer
	 * 方法描述：将问卷数据更新就消费者表中。根据Customerdata表是否有这个消费者,来决定是更新还是插入
	 */
	public boolean insertOrUpdateQuestionDataToCustomer(QuestionData data) {
		if(existCustomerByCustomerid(data.getCustomerid())){
			//原来Customerdata表有这个消费者
			//将原来数据更新
			Customer customer = customerDataDao.getCustomerByCustomerid(data.getCustomerid());
			String oldContent = customer.getContent();
			String newContent = updateCustomerContent(oldContent,data.getData(),data.getRecordtime().toString());
			customer.setContent(newContent);
			customer.setInfointegrated(true);
			return updateCustomer(customer);
		}else{
			//原来Customerdata表没有这个消费者
			Customer customer = new Customer();
			customer.setCustomerid(data.getCustomerid());
			customer.setIntegrated(data.isIntegrated());
			if(data.isIntegrated()){
				customer.setInfointegrated(true);
			}
			String oldContent = new JSONObject().toString();
			String content = updateCustomerContent(oldContent,data.getData(),data.getRecordtime().toString());
			customer.setContent(content);
			return insertCustomer(customer);
		}
	}


	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日下午8:21:38
	 *
	 * 方法名：updateCustomerData
	 * 方法描述：将新的data内容,更新进去   
	 */
	public String updateCustomerContent(String oldContent,String addContent,String datetime) {
		JSONObject json = JSONObject.fromObject(oldContent);
		JSONObject addjson = JSONObject.fromObject(addContent);
		Iterator it = addjson.keys();  
		while (it.hasNext()) {
			String key = (String) it.next();  
			JSONArray values = addjson.getJSONArray(key);
			if(json.containsKey(key)){
				//如果原来含有
				JSONObject valuejson = json.getJSONObject(key);
				for(int i=0;i<values.size();i++){
					String value = values.getString(i).trim();
					if(valuejson.containsKey(value)){//这个if else 逻辑可以保证key都不一样
						valuejson.put(value, datetime);//如果这个值已经存在,就把时间替换成新的
					}else{
						valuejson.accumulate(value,datetime);//如果这个值不存在，accumulate是增加
					}
				}
				json.put(key, valuejson);//put是替换   key : {value : datetime,value : datetime}
			}else{
				//如果不含有
				JSONObject valuejson = new JSONObject();
				for(int i=0;i<values.size();i++){
					String value = values.getString(i).trim();
					if(valuejson.containsKey(value)){//这个也判断的原因是，防止array里面的值相同
						valuejson.put(value, datetime);//如果这个值已经存在,就把时间替换成新的
					}else{
						valuejson.accumulate(value,datetime);//如果这个值不存在，accumulate是增加
					}
				}
				json.accumulate(key, valuejson);//accumulate是增加  key : {value : datetime}
			}
		}
		return json.toString();
	}

	/*
	 * 这个是新增消费者时必须检查的
	 * 根据电话号码，验证Customer是否存在
	 * customerTel:"{\"telnumber\": \"13113111564\"}"
	 */
	public boolean existCustomerByCustomerTel(JSONObject customerTel) {
		String sql_customerTel = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(customerTel);
		return customerDataDao.existCustomerByCustomerTel(sql_customerTel);
	}

	/*
	 * 根据CustomerId,验证Customer是否存在
	 */
	public boolean existCustomerByCustomerid(String customerid) {
		return customerDataDao.existCustomerByCustomerid(customerid);
	}

	/*
	 * 分页查询
	 * 当有可选参数存在时，使用此方法
	 * 只分两种情况,全条件查询与必要条件查询
	 * 
	 * 分页数据在前面的为筛选条件最多的
	 * 分页数据在后面的为筛选条件少的,数据不是很精确
	 * 
	 * "necessaryParams = {"name": "xxx", "xx": "xxxx"}"
	 * "optionalParams = {"sex": "男", "xx": "xxxx"}"
	 * "removeCustomerIds":[]
	 */
	public List<Customer> getPageCustomerForOptionalParams(JSONObject requestNecessaryParams,JSONObject requestOptionalParams,JSONArray requestRemoveCustomerIds,int eachPageRowNum, int startPosition,boolean infointegratedSwitch) {

		List<Customer> customers = null;

		if(requestOptionalParams.size()==0){
			//如果用户没有写可选参数
			customers = getPageCustomer(requestNecessaryParams,requestRemoveCustomerIds,eachPageRowNum, startPosition,infointegratedSwitch);
			//System.out.println("没有可选参数");
		}else{

			JSONObject requestTotalParams = new JSONObject();
			requestTotalParams.putAll(requestNecessaryParams);
			requestTotalParams.putAll(requestOptionalParams);

			//筛选数据的结束位置
			int endPosition = startPosition + eachPageRowNum;

			//得到所有条件下查询的总数(数量少)
			int countTotalParamsCustomer = getCustomerRecordCount(requestTotalParams,requestRemoveCustomerIds,infointegratedSwitch);

			//1.确定分页数据在哪个结果集中查取
			if(countTotalParamsCustomer >= endPosition){
				//在所有条件结果中找
				//SSystem.out.println("在所有条件结果中找");
				customers= getPageCustomer(requestTotalParams,requestRemoveCustomerIds,eachPageRowNum, startPosition,infointegratedSwitch);			
			}else if(countTotalParamsCustomer > startPosition && countTotalParamsCustomer < endPosition){
				//这个情况只会出现一次，就是在这一个分页中既有前一段的数据又有后一段的数据这种情况
				//在所有条件结果中找,与必要条件结果中共同查找
				//确定每一部分需要的个数
				//System.out.println("这个情况只会出现一次，就是在这一个分页中既有前一段的数据又有后一段的数据这种情况");
				int rowNum_FitstPart = countTotalParamsCustomer - startPosition;
				int rowNum_SecondPart = endPosition - countTotalParamsCustomer;
				List<Customer> customers_FitstPart = getPageCustomer(requestTotalParams,requestRemoveCustomerIds,rowNum_FitstPart, startPosition,infointegratedSwitch);
				List<Customer> customers_SecondPart = getPageCustomerExtra(requestTotalParams, requestNecessaryParams,requestRemoveCustomerIds, rowNum_SecondPart, 0,infointegratedSwitch);

				customers_FitstPart.addAll(customers_SecondPart);//这句话是将第二部分添加进来,这样第一部分就变成完整的了
				customers = customers_FitstPart;
			}else if(countTotalParamsCustomer <= startPosition){
				//只在必要条件结果中共同查找
				//System.out.println("只在必要条件结果中共同查找");
				int startPositionExtra = startPosition - countTotalParamsCustomer;//将起始位置进行转换
				customers = getPageCustomerExtra(requestTotalParams, requestNecessaryParams, requestRemoveCustomerIds,eachPageRowNum, startPositionExtra,infointegratedSwitch);
			}
		}
		return customers;
	}




	/*
	 * 根据消费者电话 查找到消费者
	 * 用于消费者录入时使用
	 */
	public Customer getCustomerByTel(JSONObject customerTel) {
		String sql_customerTel = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(customerTel);
		return customerDataDao.getCustomerByTel(sql_customerTel);
	}


	/*
	 * 向customerdata表中写入一条记录
	 */
	public boolean insertCustomer(Customer customer){
		return customerDataDao.insertCustomer(customer);
	}

	/**
	 * 
	 * 作者：杨潇
	 * 创建时间：2017年3月1日下午8:22:59
	 *
	 * 方法名：updateCustomer
	 * 方法描述：根据customerid,更新消费者
	 */
	public boolean updateCustomer(Customer customer) {
		return customerDataDao.updateCustomer(customer);
	}

	/*
	 * 根据customerid找到消费者,这里的消费者的值已经做过了处理
	 */
	public Customer getCustomerByCustomerid(String customerid) {
		Customer customer = new Customer();
		if(customerDataDao.existCustomerByCustomerid(customerid)){
			customer = customerSelectValue(customerDataDao.getCustomerByCustomerid(customerid));
		}
		return customer;
	}



	/*
	 * 统计总数  对jsonb用户数据的查询
	 * 整表查询
	 */
	public int getCustomerRecordCount(JSONObject requestContentParams,JSONArray requestRemoveCustomerIds,boolean infointegratedSwitch) {

		int recordCount = 0;

		if(requestContentParams.size()!=0 && requestRemoveCustomerIds.size()!=0){
			//说明有请求参数,将筛选参数转化为SQL语句格式
			String contentParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestContentParams);
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			recordCount = customerDataDao.getCustomerRecordCount(contentParams,removeCustomerIds_SQL,infointegratedSwitch);
		}else if(requestContentParams.size()==0 && requestRemoveCustomerIds.size()!=0){
			String contentParams = "";
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			recordCount = customerDataDao.getCustomerRecordCount(contentParams,removeCustomerIds_SQL,infointegratedSwitch);
		}else if(requestContentParams.size()!=0 && requestRemoveCustomerIds.size()==0){
			String contentParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestContentParams);
			String removeCustomerIds_SQL = "";
			recordCount = customerDataDao.getCustomerRecordCount(contentParams,removeCustomerIds_SQL,infointegratedSwitch);
		}else{
			String contentParams = "";
			String removeCustomerIds_SQL = "";
			recordCount = customerDataDao.getCustomerRecordCount(contentParams,removeCustomerIds_SQL,infointegratedSwitch);
		}
		return recordCount;
	}


	/*
	 * getPageCustomerForOptionalParams调用此方法
	 * 
	 * 有可选条件的情况
	 * 为包含可选条件的情况查找分页数据
	 * 当用户所给条件筛选出的样本不够用时，需要通过去可选条件来进行筛选
	 * contentTotalParams > contentNecessaryParams
	 */
	public List<Customer> getPageCustomerExtra(JSONObject requestTotalParams,JSONObject requestNecessaryParams,JSONArray requestRemoveCustomerIds,int eachPageRowNum, int startPosition,boolean infointegratedSwitch) {

		List<Customer> customers = null;
		if(requestNecessaryParams.size()!=0 && requestRemoveCustomerIds.size()!=0){
			//说明有请求参数,将筛选参数转化为SQL语句格式
			String contentTotalParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestTotalParams);
			String contentNecessaryParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestNecessaryParams);
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			customers = customerDataDao.getPageCustomerDataExtra(contentTotalParams, contentNecessaryParams, removeCustomerIds_SQL, eachPageRowNum, startPosition,infointegratedSwitch);
		}else if(requestNecessaryParams.size()==0 && requestRemoveCustomerIds.size()!=0){
			String contentTotalParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestTotalParams);
			String contentNecessaryParams = "";
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			customers = customerDataDao.getPageCustomerDataExtra(contentTotalParams, contentNecessaryParams, removeCustomerIds_SQL, eachPageRowNum, startPosition,infointegratedSwitch);
		}else if(requestNecessaryParams.size()!=0 && requestRemoveCustomerIds.size()==0){
			String contentTotalParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestTotalParams);
			String contentNecessaryParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestNecessaryParams);
			String removeCustomerIds_SQL = "";
			customers = customerDataDao.getPageCustomerDataExtra(contentTotalParams, contentNecessaryParams, removeCustomerIds_SQL, eachPageRowNum, startPosition,infointegratedSwitch);
		}else{
			String contentTotalParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestTotalParams);
			String contentNecessaryParams = "";
			String removeCustomerIds_SQL = "";
			customers = customerDataDao.getPageCustomerDataExtra(contentTotalParams, contentNecessaryParams, removeCustomerIds_SQL, eachPageRowNum, startPosition,infointegratedSwitch);
		}

		for(Customer customer : customers){
			//对同一个消费者进行数据选择
			//选值的方法是：将每个标签最近的值取出来
			customer = customerSelectValue(customer);
		}
		//由于List中每个对象都是引用，所以相当于是引用传值，已经将值修改了
		return customers;
	}

	/*
	 * 不分页
	 * getCustomersForOptionalParams调用此方法
	 * 
	 * 有可选条件的情况
	 * 为包含可选条件的情况查找分页数据
	 * 当用户所给条件筛选出的样本不够用时，需要通过去可选条件来进行筛选
	 * contentTotalParams > contentNecessaryParams
	 */
	public List<Customer> getCustomersExtra(JSONObject requestTotalParams,JSONObject requestNecessaryParams,JSONArray requestRemoveCustomerIds,boolean infointegratedSwitch) {

		List<Customer> customers = null;
		if(requestNecessaryParams.size()!=0 && requestRemoveCustomerIds.size()!=0){
			//说明有请求参数,将筛选参数转化为SQL语句格式
			String contentTotalParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestTotalParams);
			String contentNecessaryParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestNecessaryParams);
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			customers = customerDataDao.getCustomerDatasExtra(contentTotalParams, contentNecessaryParams, removeCustomerIds_SQL,infointegratedSwitch);
		}else if(requestNecessaryParams.size()==0 && requestRemoveCustomerIds.size()!=0){
			String contentTotalParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestTotalParams);
			String contentNecessaryParams = "";
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			customers = customerDataDao.getCustomerDatasExtra(contentTotalParams, contentNecessaryParams, removeCustomerIds_SQL,infointegratedSwitch);
		}else if(requestNecessaryParams.size()!=0 && requestRemoveCustomerIds.size()==0){
			String contentTotalParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestTotalParams);
			String contentNecessaryParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestNecessaryParams);
			String removeCustomerIds_SQL = "";
			customers = customerDataDao.getCustomerDatasExtra(contentTotalParams, contentNecessaryParams, removeCustomerIds_SQL,infointegratedSwitch);
		}else{
			String contentTotalParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestTotalParams);
			String contentNecessaryParams = "";
			String removeCustomerIds_SQL = "";
			customers = customerDataDao.getCustomerDatasExtra(contentTotalParams, contentNecessaryParams, removeCustomerIds_SQL,infointegratedSwitch);
		}

		for(Customer customer : customers){
			//对同一个消费者进行数据选择
			//选值的方法是：将每个标签最近的值取出来
			customer = customerSelectValue(customer);
		}
		//由于List中每个对象都是引用，所以相当于是引用传值，已经将值修改了
		return customers;
	}


	/*
	 * 没有可选条件的情况
	 * 得到分页数据  对jsonb用户数据的查询
	 * 整表查询
	 * 对于整表查询，需要数据归并
	 */
	public List<Customer> getPageCustomer(JSONObject requestContentParams,JSONArray requestRemoveCustomerIds,int eachPageRowNum, int startPosition,boolean infointegratedSwitch) {

		List<Customer> customers = null;
		if(requestContentParams.size()!=0 && requestRemoveCustomerIds.size()!=0){
			//说明有请求参数,将筛选参数转化为SQL语句格式
			String contentParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestContentParams);
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			customers = customerDataDao.getPageCustomerData(contentParams,removeCustomerIds_SQL,eachPageRowNum,startPosition,infointegratedSwitch);
		}else if(requestContentParams.size()==0 && requestRemoveCustomerIds.size()!=0){
			String contentParams = "";
			String removeCustomerIds_SQL = toolRequestParamsToSQLParams.requestRemoveCustomerIdsToSQLRemoveCustomerIds(requestRemoveCustomerIds);
			customers = customerDataDao.getPageCustomerData(contentParams,removeCustomerIds_SQL,eachPageRowNum,startPosition,infointegratedSwitch);
		}else if(requestContentParams.size()!=0 && requestRemoveCustomerIds.size()==0){
			String contentParams = toolRequestParamsToSQLParams.requestParamsToSQLParamsForCustomerData(requestContentParams);
			String removeCustomerIds_SQL = "";
			customers = customerDataDao.getPageCustomerData(contentParams,removeCustomerIds_SQL,eachPageRowNum,startPosition,infointegratedSwitch);
		}else{
			String contentParams = "";
			String removeCustomerIds_SQL = "";
			customers = customerDataDao.getPageCustomerData(contentParams,removeCustomerIds_SQL,eachPageRowNum,startPosition,infointegratedSwitch);
		}

		for(Customer customer : customers){
			//对同一个消费者进行数据选择
			//选值的方法是：将每个标签最近的值取出来
			customer = customerSelectValue(customer);
		}
		//由于List中每个对象都是引用，所以相当于是引用传值，已经将值修改了
		return customers;
	}

	/*
	 * 这里选值的策略是：时间最近的
	 */
	public Customer customerSelectValue(Customer customer){

		JSONObject newcontent = new JSONObject();
		JSONObject oldcontent = JSONObject.fromObject(customer.getContent());
		Iterator it = oldcontent.keys();
		while (it.hasNext()) {
			String key = (String) it.next();  
			String value = oldcontent.getString(key);
			String lastValue = getLastTimeValue(value);
			newcontent.accumulate(key, lastValue);
		}
		customer.setContent(newcontent.toString());
		return customer;
	}

	/**
	 * 
	 * @param valuestr
	 * @return 得到最近时间值对应的所有值
	 */
	public String getLastTimeValue(String valuestr){

		String lastValue = "";
		JSONObject valueJson = JSONObject.fromObject(valuestr);

		//这次遍历得到最近的时间值
		Iterator it = valueJson.keys();
		String lastTime = "0000-00-00 00:00:00";//最近的时间,初始化
		while (it.hasNext()) {
			String key = (String) it.next();  
			String value = valueJson.getString(key);
			if(!compareDateTime(lastTime,value)){
				lastTime = value;
			}
		}

		//这次遍历得到最近时间值对应的所有值
		Iterator it1 = valueJson.keys();
		while(it1.hasNext()){
			String key = (String) it1.next();
			if(valueJson.getString(key).equals(lastTime)){
				if(lastValue.length()!=0){
					lastValue = lastValue + ":" +key;
				}else{
					lastValue = lastValue + key;
				}
			}
		}
		return lastValue;
	}

	/*
	 * string转成date类型,并比较
	 * 返回true,说明tagdate时间更近
	 * 返回false,说明tagdate时间更远
	 */
	public boolean compareDateTime(String tagvalue,String value){

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  

		try {
			Date tagdate = format.parse(tagvalue);
			Date date=format.parse(value);
			//比较的结果如果大于0，说明tagdate时间更近
			//比较的结果如果小于0，说明tagdate时间更远
			return tagdate.compareTo(date) > 0 ? true : false;
		} catch (Exception e) {
			System.out.println("时间比较时发生错误,返回ture");
			return true;
		}

	}


	/*
	 * 生成新的CustomerDataContent内容  
	 */
	public String createNewCustomerDataContent(String content,String datetime) {
		JSONObject json = JSONObject.fromObject(content);
		JSONObject newJson = new JSONObject();
		Iterator it = json.keys();  
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = json.getString(key);

			JSONObject valuejson = new  JSONObject();
			valuejson.accumulate(value,datetime);// {value : datetime}

			newJson.accumulate(key, valuejson);//{key : {value : datetime}}
		}
		return newJson.toString();
	}
}
