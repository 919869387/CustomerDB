package util;

import java.util.UUID;

public class ToolCreateCustomerId {

	/*
	 * 生成customerid  
	 * 
	 * j0001_36位UUID - 唯一标示每个消费者(前五位中:第一位是公司拼音首字母)
	 * companyNumber_36位UUID - 唯一标示每个消费者(前五位中:第一位是公司拼音首字母)
	 * j0001_36位UUID
	 */
	public static String createCustomerId(String companyNumber) {  
		UUID uuid = UUID.randomUUID();
		String customerId =  companyNumber +"_"+ uuid.toString();
		return customerId;
	}

}