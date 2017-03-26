package pojo;

/*
 * 对应customerdata数据表
 */
public class Customer {
	
	int id;
	String content;
	String customerid;
	boolean integrated = true;
	
	
	public boolean isIntegrated() {
		return integrated;
	}
	public void setIntegrated(boolean integrated) {
		this.integrated = integrated;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
}
