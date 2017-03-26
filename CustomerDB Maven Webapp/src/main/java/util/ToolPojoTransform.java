package util;

import org.springframework.stereotype.Repository;

import net.sf.json.JSONObject;
import pojo.Customer;
import pojo.ManageCustomer;
import pojo.QuestionnaireSendStatistics;
import pojo_out.Customer_Out;
import pojo_out.Customer_ToManagerShow;
import pojo_out.QuestionData_Out;

@Repository
public class ToolPojoTransform {
	
	public Customer_ToManagerShow customerTocustomer_ToManagerShow(Customer customer,QuestionnaireSendStatistics questionnaireSendStatistics){
		Customer_ToManagerShow customer_ToManagerShow = new Customer_ToManagerShow();
		
		JSONObject customerInfo = JSONObject.fromObject(customer.getContent());
		String name = customerInfo.getString(ToolGlobalParams.nameTagId);
		String telnumber = customerInfo.getString(ToolGlobalParams.telnumberTagId);
		
		customer_ToManagerShow.setCustomerid(customer.getCustomerid());
		customer_ToManagerShow.setName(name);
		customer_ToManagerShow.setTelnumber(telnumber);
		customer_ToManagerShow.setSuccesssendnum(questionnaireSendStatistics.getSuccesssendnum());
		customer_ToManagerShow.setAnswernum(questionnaireSendStatistics.getAnswernum());
		
		return customer_ToManagerShow;
	}
	
	/*
	 * QuestionData_Out为前端转换
	 */
	public QuestionData_Out QuestionDataToQuestionDataOut(Customer customer,ManageCustomer manageCustomer){
		
		QuestionData_Out dataOut = new QuestionData_Out();
		
		JSONObject customerInfo = JSONObject.fromObject(customer.getContent());
		String username = customerInfo.getString(ToolGlobalParams.nameTagId);//姓名的tagid为22
		String telnumber = customerInfo.getString(ToolGlobalParams.telnumberTagId);//电话号码的tagid为23
		
		dataOut.setUserName(username);
		dataOut.setContact(telnumber);
		dataOut.setCustomerId(customer.getCustomerid());
		dataOut.setSalesmanId(manageCustomer.getManagerid());
		dataOut.setSalesmanName(manageCustomer.getManagername());
		
		return dataOut;
	}
	
	/*
	 * Customer_Out为前端的对象
	 */
	public Customer_Out CustomerToCustomerOut(Customer customer,ManageCustomer manageCustomer){
		
		Customer_Out customerOut = new Customer_Out();
		
		JSONObject customerInfo = JSONObject.fromObject(customer.getContent());
		String username = customerInfo.getString(ToolGlobalParams.nameTagId);
		String telnumber = customerInfo.getString(ToolGlobalParams.telnumberTagId);
		
		customerOut.setUserName(username);
		customerOut.setContact(telnumber);
		customerOut.setCustomerId(customer.getCustomerid());
		customerOut.setSalesmanId(manageCustomer.getManagerid());
		customerOut.setSalesmanName(manageCustomer.getManagername());
		
		return customerOut;
	}

}
